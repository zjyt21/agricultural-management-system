package com.hlp.agrisys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.entity.Role;

import java.util.List;

/**
 * <p>
 * role table 服务类
 * </p>
 *
 * @author hlp
 * @since 2022-07-22
 */
public interface IRoleService extends IService<Role> {

    Result setRoleMenu(Long roleId, List<Long> menuIds);

    Result getRoleMenu(Long roleId);
}
