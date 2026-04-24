package com.digitalpark.modules.system.service;

import cn.hutool.core.util.StrUtil;
import com.digitalpark.modules.system.mapper.SysUserMapper;
import com.digitalpark.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限校验服务
 * 用于 @PreAuthorize("@ss.hasPermi('xxx')") 表达式中的权限判断
 *
 * @author digitalpark
 */
@Slf4j
@Service("ss")
@RequiredArgsConstructor
public class PermissionService {

    private final SysUserMapper sysUserMapper;

    /**
     * 判断当前用户是否拥有指定权限
     *
     * @param permission 权限标识
     * @return 是否拥有权限
     */
    public boolean hasPermi(String permission) {
        if (StrUtil.isBlank(permission)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        // 管理员拥有所有权限
        if (isAdmin(authentication.getName())) {
            return true;
        }
        // 查询用户权限
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, authentication.getName())
        );
        if (user == null) {
            return false;
        }
        List<String> permissions = sysUserMapper.selectPermissionsByUserId(user.getId());
        return permissions.contains(permission);
    }

    /**
     * 判断当前用户是否拥有任意一个指定权限
     *
     * @param permissions 权限标识数组
     * @return 是否拥有任意一个权限
     */
    public boolean hasAnyPermi(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        for (String permission : permissions) {
            if (hasPermi(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前用户是否拥有指定角色
     *
     * @param role 角色编码
     * @return 是否拥有角色
     */
    public boolean hasRole(String role) {
        if (StrUtil.isBlank(role)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        // 管理员拥有所有角色
        if (isAdmin(authentication.getName())) {
            return true;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    /**
     * 判断是否为管理员
     */
    private boolean isAdmin(String username) {
        return "admin".equals(username);
    }
}
