package com.chat.gift.money.service.impl;

import com.chat.common.model.ResponseMsg;
import com.chat.common.util.redis.RedisUtil;
import com.chat.common.util.TimeUtil;
import com.chat.gift.money.dao.GiftMoneyDao;
import com.chat.gift.money.model.GiftMoney;
import com.chat.gift.money.service.GiveService;
import com.chat.gift.money.util.RedPacketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 发红包业务处理
 * @author xar
 */
@Service
public class GiveServiceImpl implements GiveService{

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private GiftMoneyDao giftMoneyDao;

    @Override
    public ResponseMsg giveGiftMoney(GiftMoney giftMoney) {
        //设置红包属性
        giftMoney = completeGiftMoney(giftMoney);
        //根据红包个数和金额预先生成领取金额,和红包库存一起放入redis中
        generateSnatchAmount(giftMoney);
        //红包相关数据写入到数据库中
        giftMoneyDao.addGiftMoney(giftMoney);
        giftMoneyDao.addStock(giftMoney.getOrderId(), giftMoney.getCount());
        return new ResponseMsg<String>(200, "红包发送成功");
    }

    /**
     * 完善红包信息
     * @param giftMoney 红包对象
     * @return 返回完善后的红包对象
     */
    private GiftMoney completeGiftMoney(GiftMoney giftMoney){
        giftMoney.setId(generateId());
        Date now = new Date();
        giftMoney.setOrderId(generateOrderId(now));
        giftMoney.setCreateTime(TimeUtil.formatDateWithCharacter(now));
        giftMoney.setExpired(false);
        giftMoney.setOver(false);
        return giftMoney;
    }

    /**
     * 生成红包id
     * @return 返回红包id
     */
    private String generateId(){
        return UUID.randomUUID().toString();
    }

    /**
     * 生成红包订单号
     * @param now 系统当前时间
     * @return
     */
    private String generateOrderId(Date now){
        return "gift_money_" + TimeUtil.formatDateWithNoCharacter(now);
    }

    /**
     * 预先生成每个红包的金额
     * @param giftMoney 红包对象
     */
    private void generateSnatchAmount(GiftMoney giftMoney){
        List<BigDecimal> snatchAmount = RedPacketUtil.getRedBags(new BigDecimal(giftMoney.getAmount()), giftMoney.getCount());
        RedPacketUtil.checkAmount(new BigDecimal(giftMoney.getAmount()), snatchAmount);
        giftMoneyToRedis(giftMoney.getOrderId(), snatchAmount, giftMoney.getCount());
    }

    /**
     * 将红包金额和库存放入redis中
     * @param orderId 红包订单号
     * @param snatchAmount 领取金额
     * @param count 库存
     */
    private void giftMoneyToRedis(String orderId, List<BigDecimal> snatchAmount, int count){
        String snatchKey = orderId + "_snatch";
        for (BigDecimal bigDecimal : snatchAmount) {
            redisUtil.lPush(snatchKey, bigDecimal);
        }
        String stockKey = orderId + "_stock";
        redisUtil.set(stockKey, count, 60000);
        //插入红包过期的标识，存在即没过期，不存在表示过期了
        String isExpired = orderId + "_expired";
        redisUtil.set(isExpired, "Identification to determine whether the red packet has expired");
    }
}
