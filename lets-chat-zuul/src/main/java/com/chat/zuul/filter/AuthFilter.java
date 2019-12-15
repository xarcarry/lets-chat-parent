package com.chat.zuul.filter;


import com.alibaba.fastjson.JSON;
import com.chat.common.util.Base64Utils;
import com.chat.common.util.ResponseCode;
import com.chat.common.util.redis.RedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthFilter extends ZuulFilter {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //表示解析令牌是否成功
        if (!(authentication instanceof OAuth2Authentication)){
            return false;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        Boolean isRepeat = redisUtil.setnx(userAuthentication.getName(), true, 10);
        if (!isRepeat){
            //过滤该请求，不往下级服务去转发请求，到此结束
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(ResponseCode.REPEAT_REQUEST.getCode());
            ctx.setResponseBody("{\"result\":" + ResponseCode.REPEAT_REQUEST.getMsg() + "}");
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            return null;
        }
        //2.组装明文token，转发给微服务，放入header，名称为json-token
        List<String> authorities = new ArrayList<>();
        userAuthentication.getAuthorities().stream().forEach(s -> authorities.add(((GrantedAuthority) s).getAuthority()));
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> jsonToken = new HashMap<>(requestParameters);
        if (userAuthentication != null){
            jsonToken.put("principal", userAuthentication.getName());
            jsonToken.put("authorities", authorities);
        }
        System.out.println(jsonToken);
        ctx.addZuulRequestHeader("json-token", Base64Utils.encode(JSON.toJSONString(jsonToken)));
        return null;
    }
}