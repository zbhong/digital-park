package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.system.dto.MenuDTO;
import com.digitalpark.modules.system.entity.SysMenu;
import com.digitalpark.modules.system.mapper.SysMenuMapper;
import com.digitalpark.modules.system.service.SysMenuService;
import com.digitalpark.modules.system.vo.RouteVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统菜单 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> allMenus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder)
        );
        return buildMenuTree(allMenus, 0L);
    }

    @Override
    public List<SysMenu> getMenuList() {
        return sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder)
        );
    }

    @Override
    public SysMenu getMenuById(Long id) {
        return sysMenuMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenu(MenuDTO menuDTO) {
        SysMenu menu = new SysMenu();
        menu.setParentId(menuDTO.getParentId() != null ? menuDTO.getParentId() : 0L);
        menu.setMenuName(menuDTO.getMenuName());
        menu.setPath(menuDTO.getPath());
        menu.setComponent(menuDTO.getComponent());
        menu.setPerms(menuDTO.getPerms());
        menu.setIcon(menuDTO.getIcon());
        menu.setMenuType(menuDTO.getMenuType());
        menu.setSortOrder(menuDTO.getSortOrder() != null ? menuDTO.getSortOrder() : 0);
        menu.setVisible(menuDTO.getVisible() != null ? menuDTO.getVisible() : 0);
        menu.setStatus(menuDTO.getStatus() != null ? menuDTO.getStatus() : 0);
        sysMenuMapper.insert(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuDTO menuDTO) {
        if (menuDTO.getId() == null) {
            throw new BusinessException("菜单ID不能为空");
        }
        SysMenu menu = sysMenuMapper.selectById(menuDTO.getId());
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        // 不能将自身设置为父菜单
        if (menuDTO.getParentId() != null && menuDTO.getParentId().equals(menuDTO.getId())) {
            throw new BusinessException("不能将自身设置为父菜单");
        }
        menu.setParentId(menuDTO.getParentId());
        menu.setMenuName(menuDTO.getMenuName());
        menu.setPath(menuDTO.getPath());
        menu.setComponent(menuDTO.getComponent());
        menu.setPerms(menuDTO.getPerms());
        menu.setIcon(menuDTO.getIcon());
        menu.setMenuType(menuDTO.getMenuType());
        menu.setSortOrder(menuDTO.getSortOrder());
        menu.setVisible(menuDTO.getVisible());
        menu.setStatus(menuDTO.getStatus());
        sysMenuMapper.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        SysMenu menu = sysMenuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        // 检查是否有子菜单
        Long childCount = sysMenuMapper.selectCount(
                new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id)
        );
        if (childCount > 0) {
            throw new BusinessException("存在子菜单，不允许删除");
        }
        sysMenuMapper.deleteById(id);
    }

    @Override
    public List<RouteVO> buildRouteTree(List<SysMenu> menus) {
        // 构建菜单树
        List<SysMenu> menuTree = buildMenuTree(menus, 0L);
        // 转换为路由
        return buildRouters(menuTree);
    }

    // ==================== 私有方法 ====================

    /**
     * 构建菜单树（过滤掉按钮类型 menu_type=3）
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .filter(menu -> menu.getMenuType() == null || !menu.getMenuType().equals("3"))
                .peek(menu -> menu.setChildren(buildMenuTree(menus, menu.getId())))
                .sorted(Comparator.comparingInt(menu -> menu.getSortOrder() != null ? menu.getSortOrder() : 0))
                .collect(Collectors.toList());
    }

    /**
     * 构建路由树（将菜单数据转为前端路由格式，包含component路径、meta信息）
     */
    private List<RouteVO> buildRouters(List<SysMenu> menus) {
        List<RouteVO> routers = new ArrayList<>();
        for (SysMenu menu : menus) {
            RouteVO router = new RouteVO();
            router.setHidden(menu.getVisible() != null && menu.getVisible() == 1);
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new RouteVO.MetaVO(menu.getMenuName(), menu.getIcon()));
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                router.setChildren(buildRouters(menu.getChildren()));
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     */
    private String getRouteName(SysMenu menu) {
        String path = menu.getPath();
        if (StrUtil.isBlank(path)) {
            return "";
        }
        // 将路径转换为驼峰命名
        String[] parts = path.split("/");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (StrUtil.isNotBlank(part)) {
                sb.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1));
            }
        }
        return sb.toString();
    }

    /**
     * 获取路由路径
     */
    private String getRouterPath(SysMenu menu) {
        String path = menu.getPath();
        if (menu.getParentId() == null || menu.getParentId() == 0) {
            return "/" + path;
        }
        return path;
    }

    /**
     * 获取组件路径
     */
    private String getComponent(SysMenu menu) {
        String component = menu.getComponent();
        if (StrUtil.isNotBlank(component)) {
            return component;
        }
        // 默认组件
        if (menu.getParentId() == null || menu.getParentId() == 0) {
            return "Layout";
        }
        if ("C".equals(menu.getMenuType())) {
            return "ParentView";
        }
        return menu.getPath();
    }
}
