package com.hlp.agrisys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hlp.agrisys.entity.Menu;

import java.util.List;

/**
 * <p>
 * menu table 服务类
 * </p>
 *
 * @author hlp
 * @since 2022-07-22
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
