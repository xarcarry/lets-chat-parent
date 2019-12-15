package com.chat.gift.money;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.chat.gift.money", "com.chat.common.util", "com.chat.common.config"})
@EnableEurekaClient
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.chat.user.feign")
@MapperScan(basePackages = "com.chat.gift.money.dao")
public class GiftMoneyApp {
    public static void main(String[] args) {
        SpringApplication.run(GiftMoneyApp.class, args);
    }

    /*@Bean
    public GlobalTransactionScanner setGlobalTransactionScanner(){
        return new GlobalTransactionScanner("gift-money", "lets-chat-gift-money-fescar-service-group");
    }*/
}
