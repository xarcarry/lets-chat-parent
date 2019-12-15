package com.chat.gift.money.service;

import com.chat.common.model.ResponseMsg;
import com.chat.gift.money.model.GiftMoney;

/**
 * 发红包业务处理
 * @author xar
 */
public interface GiveService {
    /**
     * 发红包逻辑处理
     * @param giftMoney 用户设置的红包对象
     * @return
     */
    ResponseMsg giveGiftMoney(GiftMoney giftMoney);
}
