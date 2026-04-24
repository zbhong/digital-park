package com.digitalpark.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.entity.SysLog;
import com.digitalpark.modules.system.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志控制器
 * /system/log 路径
 *
 * @author digitalpark
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/system/log")
@RequiredArgsConstructor
public class SysLogController extends BaseController {

    private final SysLogService sysLogService;

    /**
     * 获取操作日志列表（分页）
     */
    @Operation(summary = "操作日志列表")
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public R<PageResult<?>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String username,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) LocalDateTime startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) LocalDateTime endDate) {
        Page<SysLog> page = new Page<>(current, size);
        PageResult<?> result = sysLogService.getLogList(page, module, username, startDate, endDate);
        return success(result);
    }

    /**
     * 删除操作日志
     */
    @Operation(summary = "删除操作日志")
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(module = "操作日志", type = Log.OperationType.DELETE, description = "删除操作日志")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable List<Long> ids) {
        sysLogService.deleteLog(ids);
        return success();
    }

    /**
     * 获取单条操作日志详情
     */
    @Operation(summary = "操作日志详情")
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping("/{id}")
    public R<SysLog> getInfo(@PathVariable Long id) {
        SysLog log = sysLogService.getLogById(id);
        return success(log);
    }

    /**
     * 清空操作日志
     */
    @Operation(summary = "清空操作日志")
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(module = "操作日志", type = Log.OperationType.DELETE, description = "清空操作日志")
    @DeleteMapping("/clear")
    public R<Void> clear() {
        sysLogService.clearLog();
        return success();
    }
}
