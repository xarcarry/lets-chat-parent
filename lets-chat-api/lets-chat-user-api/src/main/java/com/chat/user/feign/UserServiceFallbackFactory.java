package com.chat.user.feign;

import com.chat.user.model.PermissionDto;
import com.chat.user.model.SnatchHistory;
import com.chat.user.model.UserDto;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            public UserDto getUserByUsername(String username) {
                throw new RuntimeException("该服务暂时关闭");
            }

            @Override
            public List<PermissionDto> getPermissionsByUserId(String userId) {
                throw new RuntimeException("该服务暂时关闭");
            }

            @Override
            public String insertSnatchHistory(SnatchHistory snatchHistory) {
                throw new RuntimeException("该服务暂时关闭");
            }
        };
    }
}
