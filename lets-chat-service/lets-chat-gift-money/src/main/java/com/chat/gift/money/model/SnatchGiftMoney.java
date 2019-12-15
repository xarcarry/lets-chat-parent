package com.chat.gift.money.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抢到红包后的相关信息
 * @author xar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnatchGiftMoney {
    private String userId;
    private String snatchAmount;
    private String snatchTime;
}
