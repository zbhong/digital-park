package com.digitalpark.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.entity.SysConfig;
import com.digitalpark.modules.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置控制器
 * /system/config 路径
 *
 * @author digitalpark
 */
@Tag(name = "配置管理")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SysConfigController extends BaseController {

    private final SysConfigService sysConfigService;

    /**
     * 获取配置列表（分页）
     */
    @Operation(summary = "配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public R<PageResult<SysConfig>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String configName) {
        Page<SysConfig> page = new Page<>(current, size);
        PageResult<SysConfig> result = sysConfigService.getConfigList(page, configName);
        return success(result);
    }

    /**
     * 根据key获取配置
     */
    @Operation(summary = "根据key获取配置")
    @GetMapping("/{key}")
    public R<String> getConfigByKey(@PathVariable String key) {
        String value = sysConfigService.getConfigByKey(key);
        return success(value);
    }

    /**
     * 新增配置
     */
    @Operation(summary = "新增配置")
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(module = "配置管理", type = Log.OperationType.INSERT, description = "新增配置")
    @PostMapping
    public R<Void> add(@Valid @RequestBody SysConfig config) {
        sysConfigService.createConfig(config);
        return success();
    }

    /**
     * 修改配置
     */
    @Operation(summary = "修改配置")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(module = "配置管理", type = Log.OperationType.UPDATE, description = "修改配置")
    @PutMapping
    public R<Void> edit(@Valid @RequestBody SysConfig config) {
        sysConfigService.updateConfig(config);
        return success();
    }

    /**
     * 删除配置
     */
    @Operation(summary = "删除配置")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(module = "配置管理", type = Log.OperationType.DELETE, description = "删除配置")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysConfigService.deleteConfig(id);
        return success();
    }

    /**
     * 获取单个配置详情
     */
    @Operation(summary = "配置详情")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/detail/{id}")
    public R<SysConfig> getInfo(@PathVariable Long id) {
        SysConfig config = sysConfigService.getConfigById(id);
        return success(config);
    }

    /**
     * 刷新配置缓存
     */
    @Operation(summary = "刷新配置缓存")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(module = "配置管理", type = Log.OperationType.UPDATE, description = "刷新配置缓存")
    @PostMapping("/refreshCache")
    public R<Void> refreshCache() {
        sysConfigService.refreshCache();
        return success();
    }
}
