package com.chat.push.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushController {

    @GetMapping("push/{content}")
    public String push(@PathVariable("content") String content){
        System.out.println(content);
        return content;
    }
}
