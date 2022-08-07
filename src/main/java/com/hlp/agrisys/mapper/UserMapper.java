package com.hlp.agrisys.mapper;

import com.hlp.agrisys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hlp.agrisys.vo.UserPasswordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * user table Mapper 接口
 * </p>
 *
 * @author hlp
 * @since 2022-07-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set password = #{newPassword} where username = #{username}")
    int updatePassword(UserPasswordVo userPasswordVo);

}
