package com.chat.gift.money.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 红包实体类
 * @author xar
 */
@Data
public class GiftMoney {
    /**
     * 数据库流水号
     */
    private String id;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 发红包用户id
     */
    private String userId;
    /**
     * 红包个数
     */
    private int count;
    /**
     * 红包金额
     */
    private String amount;
    /**
     * 红包是否领完
     */
    private boolean isOver;
    /**
     * 红包是否过期
     */
    private boolean isExpired;
    /**
     * 红包生成时间
     */
    private String createTime;
}
