package com.chat.user.service.impl;

import com.chat.common.model.ResponseMsg;
import com.chat.user.dao.UserDao;
import com.chat.user.model.PermissionDto;
import com.chat.user.model.SnatchHistory;
import com.chat.user.model.UserDto;
import com.chat.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户相关业务的实现类
 * @author xar
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDto getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public List<PermissionDto> getPermissionsByUserId(String userId) {
        return userDao.getPermissionsByUserId(userId);
    }

    @Override
    public ResponseMsg insertSnatchHistory(SnatchHistory snatchHistory) {
        System.out.println("snatchHistory：" + snatchHistory.getUserId());
        if ("789456".equals(snatchHistory.getUserId())){
            throw new RuntimeException("id异常");
        }
        int flag = userDao.insertSnatchHistory(snatchHistory.getUserId(),
                snatchHistory.getOrderId(), snatchHistory.getSnatchAmount(),
                snatchHistory.getSnatchTime());
        return new ResponseMsg<String>(200, "插入成功");
    }
}
