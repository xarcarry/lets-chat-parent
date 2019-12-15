package com.chat.common.config.redisson;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/*public class RedissonConfig {
    private static Config config = new Config();
    //声明redisso对象
    private static Redisson redisson = null;
    //实例化redisson
    static{
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        //得到redisson对象
        redisson = (Redisson) Redisson.create(config);

    }

    //获取redisson对象的方法
    public static Redisson getRedisson(){
        return redisson;
    }
}*/
public class RedissonConfig {
    private static Config config = new Config();
    private static SingleServerConfig singleServerConfig = config.useSingleServer();
    //声明redisso对象
    private static Redisson redisson = null;
    //实例化redisson
    static{
        singleServerConfig.setAddress("redis://127.0.0.1:6379");
        singleServerConfig.setPingConnectionInterval(60);
        singleServerConfig.setTimeout(10000);
        singleServerConfig.setConnectionPoolSize(64);
        singleServerConfig.setConnectTimeout(10000);
        singleServerConfig.setConnectionMinimumIdleSize(32);
        singleServerConfig.setSubscriptionConnectionPoolSize(50);
        singleServerConfig.setSubscriptionConnectionMinimumIdleSize(1);
        singleServerConfig.setConnectTimeout(10000);
                //得到redisson对象
                redisson = (Redisson) Redisson.create(config);

    }

    //获取redisson对象的方法
    public static Redisson getRedisson(){
        return redisson;
    }
}
