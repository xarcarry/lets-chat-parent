package com.chat.gift.money.exception;

import lombok.Data;

/**
 * 已经领过红包的异常
 * @author
 */
@Data
public class GiftMoneySnatchedException extends RuntimeException {
    private String msg;
    public GiftMoneySnatchedException(String msg){
        this.msg = msg;
    }
}
