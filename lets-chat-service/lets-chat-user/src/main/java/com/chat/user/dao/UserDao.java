package com.chat.user.dao;

import com.chat.user.model.PermissionDto;
import com.chat.user.model.SnatchHistory;
import com.chat.user.model.UserDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 查询到的用户对象
     */
    @Select("select id,username,password,fullname,mobile from t_user where username = #{username}")
    public UserDto getUserByUsername(String username);

    /**
     * 根据用户id查询用户权限
     * @param userId 用户id
     * @return 用户拥有的权限列表
     */
    @Select("select * from t_permission where id in (\n" +
            "    select permission_id from t_role_permission where role_id in (\n" +
            "    select role_id from t_user_role where user_id = '1'));")
    public List<PermissionDto> getPermissionsByUserId(String userId);

    /**
     * 插入用户领取记录
     * @param userId 用户id
     * @param orderId 红包id
     * @param snatchAmount 领取金额
     * @param snatchTime 领取时间
     * @return
     */
    @Insert("insert into gift_money_snatch_history(user_id, order_id, snatch_amount, " +
            "snatch_time) values(#{user_id}, #{order_id}, " +
            "#{snatch_amount}, #{snatch_time})")
    int insertSnatchHistory(@Param("user_id") String userId, @Param("order_id") String orderId,
                            @Param("snatch_amount") String snatchAmount,
                            @Param("snatch_time") String snatchTime);
}
