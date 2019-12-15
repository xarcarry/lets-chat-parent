package com.chat.auth.service;

import com.alibaba.fastjson.JSON;
import com.chat.user.feign.UserService;
import com.chat.user.model.PermissionDto;
import com.chat.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelfUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.getUserByUsername(username);
        if (userDto == null){
            //如果用户查不到，返回空，由Provider抛出异常
            return null;
        }

        List<PermissionDto> permissionDtos = userService.getPermissionsByUserId(userDto.getId());
        List<String> permissions = new ArrayList<>();
        permissionDtos.stream().forEach(c -> permissions.add(c.getCode()));

        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);

        //userDto转成json，放入User.withUsername()中
        String principal = JSON.toJSONString(userDto);
        UserDetails userDetails = User.withUsername(principal).password(userDto.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
