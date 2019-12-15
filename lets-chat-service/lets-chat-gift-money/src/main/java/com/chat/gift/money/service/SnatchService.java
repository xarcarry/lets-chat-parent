package com.chat.gift.money.service;

import com.chat.common.model.ResponseMsg;

/**
 * 抢红包业务
 * @author xar
 */
public interface SnatchService {
    /**
     * 抢红包处理
     * @param userId 用户id
     * @param orderId 红包订单号
     * @return
     */
    ResponseMsg snatchGiftMoney(String userId, String orderId);
}
