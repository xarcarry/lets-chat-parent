package com.chat.gift.money.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chat.common.util.redis.RedisUtil;
import com.chat.gift.money.dao.GiftMoneyDao;
import com.chat.gift.money.exception.GiftMoneySnatchedException;
import com.chat.gift.money.model.SnatchGiftMoney;
import com.chat.gift.money.service.GiftMoneyService;
import com.rabbitmq.client.Channel;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 红包领取完的后续处理
 * @author xar
 */
@Component
public class GiftMoneyMqListener {

    private Logger log = LoggerFactory.getLogger(GiftMoneyMqListener.class);

    @Autowired
    private GiftMoneyService giftMoneyService;

    @Autowired
    RedisUtil redisUtil;

    @RabbitListener(queues = "queue-test")
    public void giftMoneyFinishedHandle(Message message, Channel channel) throws IOException {
        //采用手动应答模式，更安全稳定
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        //从mq接受消息对象
        JSONObject jsonObject = JSONObject.parseObject(new String(message.getBody()));
        //如果消息对象为空，直接退出方法
        if (jsonObject == null){
            log.debug("类名：GiftMoneyMqListener，方法名：giftMoneyFinishedHandle，对象为空！");
            return;
        }
        String userId = jsonObject.get("userId").toString();
        String orderId = jsonObject.get("orderId").toString();
        String snatchGiftMoneyStr = jsonObject.get("snatchGiftMoney").toString();
        SnatchGiftMoney snatchGiftMoney = JSON.parseObject(snatchGiftMoneyStr, SnatchGiftMoney.class);
        log.info("userId:{}" + userId + ", orderId:{}"+ orderId + ", snatchGiftMoney:{}"+ snatchGiftMoney);
        //判断库存是否足够
        int stock = giftMoneyService.getGiftMoneyStock(orderId);
        if (stock <= 0){
            log.debug("类名：GiftMoneyMqListener，方法名：giftMoneyFinishedHandle，用户：" + userId + "领取红包：" + orderId + "时，库存不够！");
            return;
        }
        //开启分布式事务，写入用户领取记录，扣减库存
        giftMoneyService.insertSnatchHistoryBySeata(snatchGiftMoney, orderId, stock - 1);
    }

}