package com.chat.gift.money.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 红包算法
 * @author xar
 */
public class RedPacketUtil {

    /**
     * 每个红包最小金额，单位为分
     */
    private static final int MIN_MONEY = 1;

    /**
     * 红包金额的离散程度，值越大红包金额越分散
     */
    private static final double DISPERSE = 10;

    public static void checkAmount(BigDecimal amount, List<BigDecimal> redBags){
        System.out.println("期望金额：" + amount);
        BigDecimal result = new BigDecimal(0);
        for (BigDecimal redBag : redBags) {
            result = result.add(redBag);
        }
        System.out.println("实际金额：" + result);
    }
    /**
     * 根据红包个数和金额生成每个红包的金额
     * @param amount 红包金额
     * @param count 红包个数
     * @return
     */
    public static List<BigDecimal> getRedBags(BigDecimal amount, int count){
        List<BigDecimal> snatchAmount = new ArrayList<>();
        int lastCount = count;
        for (int i=0;i<count;i++){
            BigDecimal oneRedAmount = RedPacketUtil.getOneRedBag(amount, lastCount);
            snatchAmount.add(oneRedAmount);
            amount = amount.subtract(oneRedAmount);
            lastCount --;
        }
        return snatchAmount;
    }
    /**
     * 根据剩余的红包金额和红包个数，获取一个红包的金额
     * @param amount 剩余金额，单位为元
     * @param count  剩余红包数
     * @return 红包金额， 单位为元
     */
    public static BigDecimal getOneRedBag(BigDecimal amount, int count) {
        //将 元*100 转为分
        int money = amount.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).intValue();
        if (money / count < MIN_MONEY) {
            throw new RuntimeException("最小值设置过大");
        }

        //最大值 = 均值*离散程度
        int max = (int) (money * DISPERSE / count);

        //最大值不能大于总金额
        max = max > money ? money : max;
        return new BigDecimal(randomBetweenMinAndMax(money, count, MIN_MONEY, max)).divide(new BigDecimal(100), 2,
                RoundingMode.HALF_UP);

    }

    /**
     * 在最小值和最大值之间随机产生一个红包
     * @param money
     * @param count
     * @param min : 最小金额
     * @param max ： 最大金额
     * @return
     */
    private static int randomBetweenMinAndMax(int money, int count, int min, int max) {
        //最后一个红包直接返回
        if (count == 1) {
            return money;
        }
        //最小和最大金额一样，返最小和最大值都行
        if (min == max) {
            return min;
        }
        //最小值 == 均值， 直接返回最小值
        if (min == money / count) {
            return min;
        }
        //min<=随机数bag<=max
        int bag = ((int) Math.rint(Math.random() * (max - min) + min));

        //剩余的均值
        int avg = (money - bag) / (count - 1);
        //比较验证剩余的红包还够不够分(均值>=最小值 是必须条件),不够分的话就是最大值过大
        if (avg < MIN_MONEY) {
            /*
             * 重新随机一个红包，最大值改成本次生成的红包金额
             * 由于 min<=本次红包金额bag<=max, 所以递归时bag是不断减小的。
             * bag在减小到min之间一定有一个值是合适的，递归结束。
             * bag减小到和min相等时，递归也会结束，所以这里不会死递归。
             */
            return randomBetweenMinAndMax(money, count, min, bag);
        } else {
            return bag;
        }
    }

}
