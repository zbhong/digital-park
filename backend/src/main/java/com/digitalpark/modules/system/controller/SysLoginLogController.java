package com.digitalpark.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.entity.SysLoginLog;
import com.digitalpark.modules.system.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志控制器
 * /system/loginLog 路径
 *
 * @author digitalpark
 */
@Tag(name = "登录日志")
@RestController
@RequestMapping("/system/loginLog")
@RequiredArgsConstructor
public class SysLoginLogController extends BaseController {

    private final SysLoginLogService sysLoginLogService;

    /**
     * 获取登录日志列表（分页）
     */
    @Operation(summary = "登录日志列表")
    @PreAuthorize("@ss.hasPermi('system:loginLog:list')")
    @GetMapping("/list")
    public R<PageResult<?>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        Page<SysLoginLog> page = new Page<>(current, size);
        PageResult<?> result = sysLoginLogService.getLoginLogList(page, username, status);
        return success(result);
    }

    /**
     * 删除登录日志
     */
    @Operation(summary = "删除登录日志")
    @PreAuthorize("@ss.hasPermi('system:loginLog:remove')")
    @Log(module = "登录日志", type = Log.OperationType.DELETE, description = "删除登录日志")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable List<Long> ids) {
        sysLoginLogService.deleteLoginLog(ids);
        return success();
    }

    /**
     * 清空登录日志
     */
    @Operation(summary = "清空登录日志")
    @PreAuthorize("@ss.hasPermi('system:loginLog:remove')")
    @Log(module = "登录日志", type = Log.OperationType.DELETE, description = "清空登录日志")
    @DeleteMapping("/clear")
    public R<Void> clear() {
        sysLoginLogService.clearLoginLog();
        return success();
    }

    /**
     * 解锁用户
     */
    @Operation(summary = "解锁用户")
    @PreAuthorize("@ss.hasPermi('system:loginLog:unlock')")
    @Log(module = "登录日志", type = Log.OperationType.UPDATE, description = "解锁用户")
    @GetMapping("/unlock/{username}")
    public R<Void> unlockUser(@PathVariable String username) {
        sysLoginLogService.unlockUser(username);
        return success();
    }
}
