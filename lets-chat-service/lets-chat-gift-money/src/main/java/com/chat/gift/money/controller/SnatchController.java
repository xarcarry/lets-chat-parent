package com.chat.gift.money.controller;

import com.alibaba.fastjson.JSON;
import com.chat.gift.money.service.SnatchService;
import com.chat.user.model.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SnatchController {

    @Autowired
    private SnatchService snatchService;

    @GetMapping("/snatch")
    public String snatchGiftMoney(String orderId){
        String userId = CurrentUserUtil.userDto.getId();
        return JSON.toJSONString(snatchService.snatchGiftMoney(userId, orderId));
    }
}
