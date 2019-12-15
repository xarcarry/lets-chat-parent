package com.chat.gift.money.service;

import com.chat.gift.money.model.SnatchGiftMoney;

/**
 * 红包相关的数据库操作
 * @author xar
 */
public interface GiftMoneyService {
    /**
     * 查询剩余红包数量
     * @param orderId 红包订单号
     * @return 剩余红包数量
     */
    int getGiftMoneyStock(String orderId);
    /**
     * 使用seata框架开启分布式事务
     * @param snatchGiftMoney 存在redis中的用户领取记录
     * @param orderId 红包订单号
     */
    void insertSnatchHistoryBySeata(SnatchGiftMoney snatchGiftMoney, String orderId, int stock);
}
