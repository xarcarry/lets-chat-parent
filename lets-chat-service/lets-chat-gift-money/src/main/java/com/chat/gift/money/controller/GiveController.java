package com.chat.gift.money.controller;

import com.alibaba.fastjson.JSON;
import com.chat.gift.money.model.GiftMoney;
import com.chat.gift.money.service.GiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发红包接口
 * @author xar
 */
@RestController
public class GiveController {

    @Autowired
    private GiveService giveService;

    /**
     * 接口防刷
     * @param giftMoney
     * @return
     */
    @PostMapping("/give")
    public String giveGiftMoney(GiftMoney giftMoney){
        return JSON.toJSONString(giveService.giveGiftMoney(giftMoney));
    }
}
