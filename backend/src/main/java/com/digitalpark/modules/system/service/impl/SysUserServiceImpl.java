package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.dto.ChangePasswordDTO;
import com.digitalpark.modules.system.dto.UserDTO;
import com.digitalpark.modules.system.entity.SysMenu;
import com.digitalpark.modules.system.entity.SysRole;
import com.digitalpark.modules.system.entity.SysUser;
import com.digitalpark.modules.system.mapper.SysMenuMapper;
import com.digitalpark.modules.system.mapper.SysRoleMapper;
import com.digitalpark.modules.system.mapper.SysUserMapper;
import com.digitalpark.modules.system.service.SysUserService;
import com.digitalpark.modules.system.vo.RouteVO;
import com.digitalpark.modules.system.vo.UserVO;
import com.digitalpark.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统用户 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<UserVO> getUserList(Page<?> page, String keyword) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(SysUser::getUsername, keyword)
                    .or().like(SysUser::getNickname, keyword)
                    .or().like(SysUser::getPhone, keyword));
        }
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = sysUserMapper.selectPage((Page<SysUser>) page, wrapper);
        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::convertToUserVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, userPage.getTotal(), userPage.getCurrent(), userPage.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDTO userDTO) {
        // 校验用户名唯一
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, userDTO.getUsername())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(
                StrUtil.isBlank(userDTO.getPassword()) ? "123456" : userDTO.getPassword()));
        user.setNickname(userDTO.getNickname());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setAvatar(userDTO.getAvatar());
        user.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : 0);
        user.setOrgId(userDTO.getOrgId());
        user.setDeptId(userDTO.getDeptId());
        user.setRemark(userDTO.getRemark());
        sysUserMapper.insert(user);

        // 分配角色
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            assignRoleDirect(user.getId(), userDTO.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        SysUser user = sysUserMapper.selectById(userDTO.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 校验用户名唯一（排除自身）
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, userDTO.getUsername())
                        .ne(SysUser::getId, userDTO.getId())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        user.setUsername(userDTO.getUsername());
        user.setNickname(userDTO.getNickname());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setAvatar(userDTO.getAvatar());
        user.setStatus(userDTO.getStatus());
        user.setOrgId(userDTO.getOrgId());
        user.setDeptId(userDTO.getDeptId());
        user.setRemark(userDTO.getRemark());
        sysUserMapper.updateById(user);

        // 更新角色
        if (userDTO.getRoleIds() != null) {
            assignRoleDirect(user.getId(), userDTO.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 删除用户角色关联
        sysUserMapper.deleteUserRoleByUserId(id);
        sysUserMapper.deleteById(id);
    }

    @Override
    public UserVO getUserById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToUserVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(Long userId, List<Long> roleIds) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        assignRoleDirect(userId, roleIds);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserMapper.updateById(user);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        String username = SecurityUtils.getCurrentUsername();
        if (StrUtil.isBlank(username)) {
            throw new BusinessException("未获取到当前用户信息");
        }
        // 校验两次密码一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 校验旧密码
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        sysUserMapper.updateById(user);
    }

    @Override
    public void changeStatus(Long userId, Integer status) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        sysUserMapper.updateById(user);
    }

    @Override
    public List<SysRole> getUserRoles(Long userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return sysUserMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<RouteVO> getUserRouters(Long userId) {
        // 查询用户菜单
        List<SysMenu> menus = sysMenuMapper.selectMenusByUserId(userId);
        // 构建菜单树
        List<SysMenu> menuTree = buildMenuTree(menus, 0L);
        // 转换为路由
        return buildRouters(menuTree);
    }

    // ==================== 私有方法 ====================

    /**
     * 直接分配角色（内部方法）
     */
    private void assignRoleDirect(Long userId, List<Long> roleIds) {
        // 先删除旧的角色关联
        sysUserMapper.deleteUserRoleByUserId(userId);
        // 再插入新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            sysUserMapper.insertUserRole(userId, roleIds);
        }
    }

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
     * 构建路由树
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

    /**
     * 转换为UserVO
     */
    private UserVO convertToUserVO(SysUser user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus());
        vo.setOrgId(user.getOrgId());
        vo.setDeptId(user.getDeptId());
        vo.setCreateTime(user.getCreateTime());
        vo.setRemark(user.getRemark());

        // 查询角色信息
        List<SysRole> roles = sysRoleMapper.selectRolesByUserId(user.getId());
        vo.setRoles(roles.stream().map(role -> {
            UserVO.RoleVO roleVO = new UserVO.RoleVO();
            roleVO.setId(role.getId());
            roleVO.setRoleName(role.getRoleName());
            roleVO.setRoleCode(role.getRoleCode());
            return roleVO;
        }).collect(Collectors.toList()));
        vo.setRoleIds(roles.stream().map(SysRole::getId).collect(Collectors.toList()));

        return vo;
    }
}
