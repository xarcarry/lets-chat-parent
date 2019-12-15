package com.chat.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户领取红包记录的实体类
 * @author xar
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnatchHistory {
    private String userId;
    private String orderId;
    private String snatchAmount;
    private String snatchTime;
}
