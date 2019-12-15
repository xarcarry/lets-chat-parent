package com.chat.user.controller;

import com.alibaba.fastjson.JSON;
import com.chat.user.model.PermissionDto;
import com.chat.user.model.SnatchHistory;
import com.chat.user.model.UserDto;
import com.chat.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/permissions/{userId}")
    public List<PermissionDto> getPermissionsByUserId(@PathVariable("userId") String userId){
        return userService.getPermissionsByUserId(userId);
    }

    @PostMapping("/snatch/history")
    public String insertSnatchHistory(@RequestBody SnatchHistory snatchHistory){
        return JSON.toJSONString(userService.insertSnatchHistory(snatchHistory));
    }
}
