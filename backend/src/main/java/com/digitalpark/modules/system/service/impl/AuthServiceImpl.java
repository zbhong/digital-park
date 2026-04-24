package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.utils.JwtUtils;
import com.digitalpark.common.utils.RedisUtils;
import com.digitalpark.modules.system.dto.LoginDTO;
import com.digitalpark.modules.system.entity.SysMenu;
import com.digitalpark.modules.system.entity.SysRole;
import com.digitalpark.modules.system.entity.SysUser;
import com.digitalpark.modules.system.mapper.SysMenuMapper;
import com.digitalpark.modules.system.mapper.SysRoleMapper;
import com.digitalpark.modules.system.mapper.SysUserMapper;
import com.digitalpark.modules.system.service.AuthService;
import com.digitalpark.modules.system.service.SysLoginLogService;
import com.digitalpark.modules.system.service.SysUserService;
import com.digitalpark.modules.system.vo.RouteVO;
import com.digitalpark.modules.system.vo.UserInfoVO;
import com.digitalpark.modules.system.vo.UserVO;
import com.digitalpark.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 认证服务 Service 实现
 * 使用 BCrypt 验证密码，生成 JWT token，记录登录日志，返回 token 和用户信息
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final SysLoginLogService sysLoginLogService;

    private static final String TOKEN_PREFIX = "token:";
    private static final long TOKEN_EXPIRE_HOURS = 24;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(LoginDTO loginDTO, String ip) {
        // 查询用户
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginDTO.getUsername())
        );
        if (user == null) {
            // 记录登录失败日志
            sysLoginLogService.recordLoginLog(loginDTO.getUsername(), ip, 1, "用户名或密码错误");
            throw new BusinessException("用户名或密码错误");
        }
        // 使用 BCrypt 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            // 记录登录失败日志
            sysLoginLogService.recordLoginLog(loginDTO.getUsername(), ip, 1, "用户名或密码错误");
            throw new BusinessException("用户名或密码错误");
        }
        // 校验状态
        if (user.getStatus() != null && user.getStatus() == 1) {
            sysLoginLogService.recordLoginLog(loginDTO.getUsername(), ip, 1, "用户已被禁用");
            throw new BusinessException("用户已被禁用，请联系管理员");
        }
        // 生成 JWT Token
        String token = jwtUtils.generateToken(user.getUsername());
        // 存入Redis
        redisUtils.set(TOKEN_PREFIX + token, user.getId().toString(), TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

        // 记录登录成功日志
        sysLoginLogService.recordLoginLog(user.getUsername(), ip, 0, "登录成功");

        log.info("用户 {} 登录成功", user.getUsername());
        return token;
    }

    @Override
    public void logout() {
        // 从SecurityContext获取当前用户名
        String username = SecurityUtils.getCurrentUsername();
        if (StrUtil.isNotBlank(username)) {
            log.info("用户 {} 登出", username);
        }
        // 将当前请求Token加入Redis黑名单
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            String token = SecurityUtils.getTokenFromRequest(request);
            if (StrUtil.isNotBlank(token)) {
                String blacklistKey = "token:blacklist:" + token;
                redisUtils.set(blacklistKey, username != null ? username : "unknown", TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
                // 同时删除Redis中的登录Token记录
                String loginKey = TOKEN_PREFIX + token;
                redisUtils.delete(loginKey);
                log.info("Token已加入黑名单");
            }
        } catch (Exception e) {
            log.warn("处理Token黑名单失败: {}", e.getMessage());
        }
    }

    @Override
    public UserInfoVO getUserInfoByUsername(String username) {
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserId(user.getId());
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setPhone(user.getPhone());

        // 查询角色列表
        List<SysRole> roles = sysRoleMapper.selectRolesByUserId(user.getId());
        userInfoVO.setRoles(roles.stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList()));

        // 查询权限列表
        List<String> permissions = sysUserMapper.selectPermissionsByUserId(user.getId());
        userInfoVO.setPermissions(permissions);

        return userInfoVO;
    }

    @Override
    public UserInfoVO getUserInfo() {
        String username = SecurityUtils.getCurrentUsername();
        if (StrUtil.isBlank(username)) {
            throw new BusinessException("未获取到当前用户信息");
        }
        return getUserInfoByUsername(username);
    }

    @Override
    public List<RouteVO> getRoutes() {
        String username = SecurityUtils.getCurrentUsername();
        if (StrUtil.isBlank(username)) {
            throw new BusinessException("未获取到当前用户信息");
        }
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 查询用户菜单
        List<SysMenu> menus = sysMenuMapper.selectMenusByUserId(user.getId());
        // 构建菜单树
        List<SysMenu> menuTree = buildMenuTree(menus, 0L);
        // 转换为路由
        return buildRouters(menuTree);
    }

    @Override
    public UserVO getCurrentUserDetail() {
        String username = SecurityUtils.getCurrentUsername();
        if (StrUtil.isBlank(username)) {
            throw new BusinessException("未获取到当前用户信息");
        }
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToUserVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentUserProfile(UserVO userVO) {
        String username = SecurityUtils.getCurrentUsername();
        if (StrUtil.isBlank(username)) {
            throw new BusinessException("未获取到当前用户信息");
        }
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 只允许更新个人信息字段
        if (userVO.getNickname() != null) {
            user.setNickname(userVO.getNickname());
        }
        if (userVO.getPhone() != null) {
            user.setPhone(userVO.getPhone());
        }
        if (userVO.getEmail() != null) {
            user.setEmail(userVO.getEmail());
        }
        if (userVO.getAvatar() != null) {
            user.setAvatar(userVO.getAvatar());
        }
        sysUserMapper.updateById(user);
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
