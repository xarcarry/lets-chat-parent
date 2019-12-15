package com.chat.gift.money.exception;

import lombok.Data;

/**
 * 自定义红包已经过期的异常类
 * @author xar
 */
@Data
public class GiftMoneyExpiredException extends RuntimeException {
    private String msg;
    public GiftMoneyExpiredException(String msg){
        this.msg = msg;
    }
}
