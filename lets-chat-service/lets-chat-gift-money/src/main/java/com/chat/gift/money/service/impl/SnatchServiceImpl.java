package com.chat.gift.money.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chat.common.model.ResponseMsg;
import com.chat.common.util.RedissonUtil;
import com.chat.common.util.TimeUtil;
import com.chat.common.util.redis.RedisUtil;
import com.chat.gift.money.exception.GiftMoneyExpiredException;
import com.chat.gift.money.exception.GiftMoneyOverException;
import com.chat.gift.money.exception.GiftMoneySnatchedException;
import com.chat.gift.money.model.SnatchGiftMoney;
import com.chat.gift.money.service.GiftMoneyService;
import com.chat.gift.money.service.SnatchService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抢红包业务处理
 * @author  xar
 */
@Service
public class SnatchServiceImpl implements SnatchService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GiftMoneyService giftMoneyService;

    @Override
    public ResponseMsg snatchGiftMoney(String userId, String orderId) {
        //判断是否领取过红包
        isSnatched(userId, orderId);
        //判断红包是否过期
        isExpired(orderId);
        //判断红包是否已领完
        isOver(orderId);
        //加锁
        String key = "gift_money_" + orderId + "_lock";
        RedissonUtil.acquire(key);
        //判断是否领取过红包
        isSnatched(userId, orderId);
        //拆红包
        SnatchGiftMoney snatchGiftMoney = getSnatchAmount(orderId, userId, key);
        //解锁
        RedissonUtil.release(key);
        //通知mq处理后续业务
        //notifyMq(userId, orderId, snatchGiftMoney);
        //测试seata
        //testSeata(orderId, snatchGiftMoney);
        //返回数据
        return new ResponseMsg<SnatchGiftMoney>(200, snatchGiftMoney);
    }

    /**
     * 判断是否已经领过红包，领过直接返回领取记录，没领过再执行下一步
     * @param userId 用户id
     * @param orderId 红包订单号
     */
    private void isSnatched(String userId, String orderId){
        String snatchKey = "gift_money_" + orderId + "_snatched_user_" + userId;
        Object snatchGiftMoney = redisUtil.get(snatchKey);
        if (snatchGiftMoney != null){
            throw new GiftMoneySnatchedException(JSON.toJSONString((SnatchGiftMoney)snatchGiftMoney));
        }
    }

    /**
     * 判断红包是否已过期
     * @param orderId 红包订单号
     */
    private void isExpired(String orderId){
        String isExpired = orderId + "_expired";
        Object flag = redisUtil.get(isExpired);
        if (flag == null){
            throw new GiftMoneyExpiredException("该红包已过期");
        }
    }

    /**
     * 判断红包是否已经领完
     * @param orderId 红包订单号
     */
    private void isOver(String orderId){
        String snatchKey = orderId + "_snatch";
        long len = redisUtil.lLen(snatchKey);
        if (len == 0){
            throw new GiftMoneyOverException("来晚了，红包已领完");
        }
    }

    /**
     * 拆红包
     * @param orderId 红包订单号
     * @return 返回金额
     */
    private SnatchGiftMoney getSnatchAmount(String orderId, String userId, String key){
        String snatchKey = orderId + "_snatch";
        Object obj = redisUtil.rPop(snatchKey);
        if (obj == null){
            RedissonUtil.release(key);
            throw new GiftMoneyOverException("来晚了，红包已领完");
        }
        BigDecimal snatchAmount = (BigDecimal)obj;
        SnatchGiftMoney snatchGiftMoney = new SnatchGiftMoney(userId, snatchAmount.toString(), TimeUtil.formatDateWithCharacter(new Date()));
        String snatchedKey = "gift_money_" + orderId + "_snatched_user_" + userId;
        redisUtil.set(snatchedKey, snatchGiftMoney);
        return snatchGiftMoney;
    }

    /**
     * 通知消息队列处理后续业务
     * @param userId 用户id
     * @param orderId 订单号
     */
    private void notifyMq(String userId, String orderId, SnatchGiftMoney snatchGiftMoney){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("orderId", orderId);
        jsonObject.put("snatchGiftMoney", snatchGiftMoney);
        rabbitTemplate.convertAndSend("queue-test", JSONObject.toJSONString(jsonObject));
    }

    /*private void testSeata(String orderId, SnatchGiftMoney snatchGiftMoney){
        //判断库存是否足够
        int stock = giftMoneyService.getGiftMoneyStock(orderId);
        if (stock <= 0){
           *//* logg.debug("类名：GiftMoneyMqListener，方法名：giftMoneyFinishedHandle，用户：" + userId + "领取红包：" + orderId + "时，库存不够！");*//*
            return;
        }
        //开启分布式事务，写入用户领取记录，扣减库存
        giftMoneyService.insertSnatchHistoryBySeata(snatchGiftMoney, orderId, stock - 1);
    }*/
}
