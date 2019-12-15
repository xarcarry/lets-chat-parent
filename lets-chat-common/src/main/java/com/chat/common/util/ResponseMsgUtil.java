package com.chat.common.util;

import com.chat.common.model.ResponseMsg;

/**
 * 系统响应
 */
public class ResponseMsgUtil {
    /**
     * 没抢到红包应该返回的信息
     * @return
     */
    public static ResponseMsg<String> noSnatchResponse(){
        try {
            return new ResponseMsg<String>(0, "来晚了，没抢到");
        }catch (Exception e){
            return exceptionResponse();
        }
    }

    /**
     * 系统发生异常应该返回的信息
     * @return
     */
    public static ResponseMsg<String> exceptionResponse(){
        try{
            return new ResponseMsg<String>(-1, "服务器异常，请重试");
        }catch (Exception e){
            return new ResponseMsg<String>(-1, "服务器异常，请重试");
        }
    }
}
