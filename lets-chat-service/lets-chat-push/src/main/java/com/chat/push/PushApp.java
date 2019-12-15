package com.chat.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PushApp {
    public static void main(String[] args) {
        SpringApplication.run(PushApp.class, args);
    }
}
