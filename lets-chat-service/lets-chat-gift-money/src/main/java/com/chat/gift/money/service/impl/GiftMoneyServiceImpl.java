package com.chat.gift.money.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chat.gift.money.dao.GiftMoneyDao;
import com.chat.gift.money.model.SnatchGiftMoney;
import com.chat.gift.money.service.GiftMoneyService;
import com.chat.user.feign.UserService;
import com.chat.user.model.SnatchHistory;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 红包的相关数据库操作
 * @author xar
 */
@Service
public class GiftMoneyServiceImpl implements GiftMoneyService {

    Log log = LogFactory.getLog(GiftMoneyServiceImpl.class);

    @Autowired
    GiftMoneyDao giftMoneyDao;
    @Autowired
    UserService userService;

    @Override
    public int getGiftMoneyStock(String orderId) {
        return giftMoneyDao.getGiftMoneyStock(orderId);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @GlobalTransactional
    @Override
    public void insertSnatchHistoryBySeata(SnatchGiftMoney snatchGiftMoney, String orderId, int stock) {
        System.out.println("insert snatch history service begin,XID:{}" + RootContext.getXID());
        //减库存
        giftMoneyDao.updateGiftMoneyStock(orderId, stock);
        //调用用户微服务，插入用户领取记录
        SnatchHistory snatchHistory = new SnatchHistory(snatchGiftMoney.getUserId(), orderId, snatchGiftMoney.getSnatchAmount(), snatchGiftMoney.getSnatchTime());
        log.info("snatchHistory：" + snatchHistory);
        String result = userService.insertSnatchHistory(snatchHistory);
        /*JSONObject jsonObject = JSONObject.parseObject(result);
        if (Integer.parseInt(jsonObject.get("code").toString()) == 200){
            log.info("用户：" + snatchGiftMoney.getUserId() + "， 领取的红包：" +
            orderId + "，记录写入成功");
            return;
        }
        log.info("用户：" + snatchGiftMoney.getUserId() + "， 领取的红包：" +
                orderId + "，记录写入失败");
        throw new RuntimeException("用户：" + snatchGiftMoney.getUserId() + "， 领取的红包：" + orderId + "，记录写入失败");*/
    }
}
