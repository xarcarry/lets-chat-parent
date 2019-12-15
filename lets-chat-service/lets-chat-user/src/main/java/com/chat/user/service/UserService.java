package com.chat.user.service;


import com.chat.common.model.ResponseMsg;
import com.chat.user.model.PermissionDto;
import com.chat.user.model.SnatchHistory;
import com.chat.user.model.UserDto;

import java.util.List;

/**
 * 用户相关业务处理
 * @author xar
 */
public interface UserService {
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    public UserDto getUserByUsername(String username);

    /**
     * 根据用户id获取用户权限列表
     * @param userId 用户id
     * @return 用户权限列表
     */
    public List<PermissionDto> getPermissionsByUserId(String userId);

    /**
     * 插入用户领取红包记录
     * @param snatchHistory 领取记录的对象
     * @return 插入结果
     */
    public ResponseMsg insertSnatchHistory(SnatchHistory snatchHistory);
}
