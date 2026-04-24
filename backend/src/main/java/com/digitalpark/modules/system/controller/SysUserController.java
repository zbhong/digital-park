package com.digitalpark.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.dto.UserDTO;
import com.digitalpark.modules.system.entity.SysUser;
import com.digitalpark.modules.system.service.SysUserService;
import com.digitalpark.modules.system.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户控制器
 * /system/user 路径，用户CRUD
 *
 * @author digitalpark
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserService sysUserService;

    /**
     * 获取用户列表（分页）
     */
    @Operation(summary = "用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public R<PageResult<UserVO>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<SysUser> page = new Page<>(current, size);
        PageResult<UserVO> result = sysUserService.getUserList(page, keyword);
        return success(result);
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "用户详情")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/{id}")
    public R<UserVO> getInfo(@PathVariable Long id) {
        UserVO userVO = sysUserService.getUserById(id);
        return success(userVO);
    }

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(module = "用户管理", type = Log.OperationType.INSERT, description = "新增用户")
    @PostMapping
    public R<Void> add(@Valid @RequestBody UserDTO userDTO) {
        sysUserService.createUser(userDTO);
        return success();
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(module = "用户管理", type = Log.OperationType.UPDATE, description = "修改用户")
    @PutMapping
    public R<Void> edit(@Valid @RequestBody UserDTO userDTO) {
        sysUserService.updateUser(userDTO);
        return success();
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(module = "用户管理", type = Log.OperationType.DELETE, description = "删除用户")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return success();
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码")
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(module = "用户管理", type = Log.OperationType.UPDATE, description = "重置密码")
    @PutMapping("/resetPassword")
    public R<Void> resetPassword(@RequestParam Long userId, @RequestParam String newPassword) {
        sysUserService.resetPassword(userId, newPassword);
        return success();
    }

    /**
     * 分配角色
     */
    @Operation(summary = "分配角色")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(module = "用户管理", type = Log.OperationType.UPDATE, description = "分配角色")
    @PutMapping("/assignRole")
    public R<Void> assignRole(@RequestParam Long userId, @RequestBody List<Long> roleIds) {
        sysUserService.assignRole(userId, roleIds);
        return success();
    }

    /**
     * 修改用户状态
     */
    @Operation(summary = "修改状态")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(module = "用户管理", type = Log.OperationType.UPDATE, description = "修改用户状态")
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestParam Long userId, @RequestParam Integer status) {
        sysUserService.changeStatus(userId, status);
        return success();
    }
}
