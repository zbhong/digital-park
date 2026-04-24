package com.digitalpark.modules.system.service;

import com.digitalpark.modules.system.dto.MenuDTO;
import com.digitalpark.modules.system.entity.SysMenu;
import com.digitalpark.modules.system.vo.RouteVO;

import java.util.List;

/**
 * 系统菜单 Service 接口
 *
 * @author digitalpark
 */
public interface SysMenuService {

    /**
     * 获取菜单树
     *
     * @return 菜单树形结构
     */
    List<SysMenu> getMenuTree();

    /**
     * 获取菜单列表（平铺）
     *
     * @return 菜单列表
     */
    List<SysMenu> getMenuList();

    /**
     * 根据ID获取菜单详情
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    SysMenu getMenuById(Long id);

    /**
     * 创建菜单
     *
     * @param menuDTO 菜单信息
     */
    void createMenu(MenuDTO menuDTO);

    /**
     * 更新菜单
     *
     * @param menuDTO 菜单信息
     */
    void updateMenu(MenuDTO menuDTO);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    void deleteMenu(Long id);

    /**
     * 构建前端路由树
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouteVO> buildRouteTree(List<SysMenu> menus);
}
