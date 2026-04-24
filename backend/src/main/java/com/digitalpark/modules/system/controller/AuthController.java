package com.digitalpark.modules.system.controller;

import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.dto.ChangePasswordDTO;
import com.digitalpark.modules.system.dto.LoginDTO;
import com.digitalpark.modules.system.service.AuthService;
import com.digitalpark.modules.system.service.SysUserService;
import com.digitalpark.modules.system.vo.RouteVO;
import com.digitalpark.modules.system.vo.UserInfoVO;
import com.digitalpark.modules.system.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 认证控制器
 * /auth 路径，登录、登出、获取用户信息、获取路由
 *
 * @author digitalpark
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;
    private final SysUserService sysUserService;

    /**
     * 用户登录
     * 使用 BCrypt 验证密码，生成 JWT token，记录登录日志，返回 token 和用户信息
     */
    @Operation(summary = "用户登录")
    @Log(module = "认证管理", type = Log.OperationType.LOGIN, description = "用户登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        String ip = getClientIp();
        String token = authService.login(loginDTO, ip);
        // 登录成功后根据用户名获取用户信息（此时SecurityContext尚未设置token）
        UserInfoVO userInfo = authService.getUserInfoByUsername(loginDTO.getUsername());
        return success(Map.of("token", token, "userInfo", userInfo));
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出")
    @Log(module = "认证管理", type = Log.OperationType.LOGOUT, description = "用户登出")
    @PostMapping("/logout")
    public R<Void> logout() {
        authService.logout();
        return success();
    }

    /**
     * 获取当前用户信息
     * 返回当前用户信息、角色列表、权限列表
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/userInfo")
    public R<UserInfoVO> getUserInfo() {
        UserInfoVO userInfo = authService.getUserInfo();
        return success(userInfo);
    }

    /**
     * 获取用户路由
     * 根据用户角色构建前端路由树（Vue Router格式）
     */
    @Operation(summary = "获取用户路由")
    @GetMapping("/routes")
    public R<List<RouteVO>> getRoutes() {
        List<RouteVO> routes = authService.getRoutes();
        return success(routes);
    }

    /**
     * 获取当前用户详情
     */
    @Operation(summary = "获取当前用户详情")
    @GetMapping("/userDetail")
    public R<UserVO> getUserDetail() {
        UserVO userVO = authService.getCurrentUserDetail();
        return success(userVO);
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @Log(module = "认证管理", type = Log.OperationType.UPDATE, description = "修改密码")
    @PutMapping("/password")
    public R<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        sysUserService.changePassword(changePasswordDTO);
        return success();
    }

    /**
     * 获取当前用户个人信息（可编辑的profile）
     */
    @Operation(summary = "获取当前用户个人信息")
    @GetMapping("/profile")
    public R<UserVO> getProfile() {
        UserVO userVO = authService.getCurrentUserDetail();
        return success(userVO);
    }

    /**
     * 更新当前用户个人信息
     */
    @Operation(summary = "更新当前用户个人信息")
    @Log(module = "认证管理", type = Log.OperationType.UPDATE, description = "更新个人信息")
    @PutMapping("/profile")
    public R<Void> updateProfile(@RequestBody UserVO userVO) {
        authService.updateCurrentUserProfile(userVO);
        return success();
    }
}
