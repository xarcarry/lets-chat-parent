package com.chat.gift.money.exception;

import lombok.Data;

/**
 * 自定义红包已经领完异常类
 * @author xar
 */
@Data
public class GiftMoneyOverException extends RuntimeException {
    private String msg;
    public GiftMoneyOverException(String msg){
        this.msg = msg;
    }
}
