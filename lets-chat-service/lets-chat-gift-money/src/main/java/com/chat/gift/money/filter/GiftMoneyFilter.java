package com.chat.gift.money.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chat.common.util.Base64Utils;
import com.chat.user.model.CurrentUserUtil;
import com.chat.user.model.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理网关传来的身份信息
 */
@Component
public class GiftMoneyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("json-token");
        if (token != null){
            //1.解析token
            String json = Base64Utils.decode(token);
            JSONObject userJson = JSON.parseObject(json);
            String principal = userJson.getString("principal");
            UserDto user = JSON.parseObject(principal, UserDto.class);
            CurrentUserUtil.userDto = user;
        }
        filterChain.doFilter(request, response);
    }
}