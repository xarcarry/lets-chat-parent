package com.chat.user.feign;

import com.chat.user.model.PermissionDto;
import com.chat.user.model.SnatchHistory;
import com.chat.user.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(value = "lets-chat-user", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService {
    @GetMapping("/user/users/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/user/permissions/{userId}")
    public List<PermissionDto> getPermissionsByUserId(@PathVariable("userId") String userId);

    @PostMapping("/user/snatch/history")
    public String insertSnatchHistory(@RequestBody SnatchHistory snatchHistory);
}
