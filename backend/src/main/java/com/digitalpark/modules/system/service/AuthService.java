package com.digitalpark.modules.system.service;

import com.digitalpark.modules.system.dto.LoginDTO;
import com.digitalpark.modules.system.vo.RouteVO;
import com.digitalpark.modules.system.vo.UserInfoVO;
import com.digitalpark.modules.system.vo.UserVO;

import java.util.List;

/**
 * 认证服务 Service 接口
 *
 * @author digitalpark
 */
public interface AuthService {

    /**
     * 用户登录
     * 使用 BCrypt 验证密码，生成 JWT token，记录登录日志，返回 token 和用户信息
     *
     * @param loginDTO 登录请求
     * @param ip       登录IP
     * @return token
     */
    String login(LoginDTO loginDTO, String ip);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取当前用户信息
     * 返回当前用户信息、角色列表、权限列表
     *
     * @return 用户信息VO
     */
    UserInfoVO getUserInfo();

    /**
     * 根据用户名获取用户信息（登录时使用）
     *
     * @param username 用户名
     * @return 用户信息VO
     */
    UserInfoVO getUserInfoByUsername(String username);

    /**
     * 获取当前用户路由
     * 根据用户角色构建前端路由树（Vue Router格式）
     *
     * @return 路由列表
     */
    List<RouteVO> getRoutes();

    /**
     * 获取当前用户详情（用于个人信息页面）
     *
     * @return 用户VO
     */
    UserVO getCurrentUserDetail();

    /**
     * 更新当前用户个人信息
     *
     * @param userVO 用户信息
     */
    void updateCurrentUserProfile(UserVO userVO);
}
