package com.chat.gift.money.dao;

import com.chat.gift.money.model.GiftMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 红包的数据库操作
 * @author xar
 */
@Mapper
public interface GiftMoneyDao {
    /**
     * 添加一条红包记录
     * @param giftMoney 红包对象
     * @return
     */
    int addGiftMoney(GiftMoney giftMoney);

    /**
     * 添加库存
     * @param orderId 红包id
     * @param stock 库存
     * @return
     */
    int addStock(@Param("order_id") String orderId, @Param("stock") int stock);

    /**
     * 获取红包库存
     * @param orderId 红包订单号
     * @return 剩余红包数量
     */
    int getGiftMoneyStock(String orderId);

    /**
     * 扣减库存
     * @param orderId 红包订单号
     * @param stock 新的库存
     * @return
     */
    int updateGiftMoneyStock(@Param("order_id") String orderId, @Param("stock") int stock);
}
