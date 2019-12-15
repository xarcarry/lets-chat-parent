package com.chat.gift.money.handler;

import com.alibaba.fastjson.JSON;
import com.chat.common.model.ResponseMsg;
import com.chat.gift.money.exception.GiftMoneyExpiredException;
import com.chat.gift.money.exception.GiftMoneyOverException;
import com.chat.gift.money.exception.GiftMoneySnatchedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author xar
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GiftMoneySnatchedException.class)
    public String handleGiftMoneySnatchedException(GiftMoneySnatchedException e){
        return JSON.toJSONString(new ResponseMsg<String>(200, e.getMsg()));
    }

    @ExceptionHandler(GiftMoneyExpiredException.class)
    public String handleGiftMoneyExpiredException(GiftMoneyExpiredException e){
        return JSON.toJSONString(new ResponseMsg<String>(600, e.getMsg()));
    }

    @ExceptionHandler(GiftMoneyOverException.class)
    public String handleGiftMoneyOverException(GiftMoneyOverException e){
        return JSON.toJSONString(new ResponseMsg<String>(601, e.getMsg()));
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        return JSON.toJSONString(new ResponseMsg<String>(401, "系统异常，请重试"));
    }
}
