package com.hlp.agrisys.mapper;

import com.hlp.agrisys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hlp
 * @since 2022-07-23
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    @Select("select menu_id from role_menu where role_id = #{roleId}")
    List<Long> selectByRoleId(@Param("roleId")Long roleId);
}
