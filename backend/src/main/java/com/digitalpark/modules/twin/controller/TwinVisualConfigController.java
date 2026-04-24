package com.digitalpark.modules.twin.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.twin.entity.*;
import com.digitalpark.modules.twin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 可视化配置管理Controller
 */
@RestController
@RequestMapping("/twin/config")
@RequiredArgsConstructor
public class TwinVisualConfigController {

    private final TwinBaseMapService baseMapService;
    private final TwinIconLibraryService iconLibraryService;
    private final TwinPointConfigService pointConfigService;
    private final TwinPointStyleService pointStyleService;
    private final TwinApiConfigService apiConfigService;
    private final TwinAlertRuleService alertRuleService;
    private final TwinConfigVersionService configVersionService;
    private final TwinConfigLogService configLogService;

    // ==================== 底图管理 ====================

    @GetMapping("/basemap/list")
    public R<List<TwinBaseMap>> basemapList(@RequestParam(required = false) String mapType,
                                             @RequestParam(required = false) Long bindId) {
        return R.ok(baseMapService.list(mapType, bindId));
    }

    @PostMapping("/basemap/upload")
    public R<TwinBaseMap> basemapUpload(@RequestBody TwinBaseMap map) {
        return R.ok(baseMapService.upload(map));
    }

    @PutMapping("/basemap/update")
    public R<TwinBaseMap> basemapUpdate(@RequestBody TwinBaseMap map) {
        return R.ok(baseMapService.update(map));
    }

    @DeleteMapping("/basemap/delete/{id}")
    public R<Void> basemapDelete(@PathVariable Long id) {
        baseMapService.delete(id);
        return R.ok();
    }

    @GetMapping("/basemap/preview/{id}")
    public R<TwinBaseMap> basemapPreview(@PathVariable Long id) {
        return R.ok(baseMapService.preview(id));
    }

    // ==================== 图标库 ====================

    @GetMapping("/icon/list")
    public R<List<TwinIconLibrary>> iconList(@RequestParam(required = false) String iconType) {
        return R.ok(iconLibraryService.list(iconType));
    }

    @PostMapping("/icon/upload")
    public R<TwinIconLibrary> iconUpload(@RequestBody TwinIconLibrary icon) {
        return R.ok(iconLibraryService.upload(icon));
    }

    @PutMapping("/icon/update")
    public R<TwinIconLibrary> iconUpdate(@RequestBody TwinIconLibrary icon) {
        return R.ok(iconLibraryService.update(icon));
    }

    @DeleteMapping("/icon/delete/{id}")
    public R<Void> iconDelete(@PathVariable Long id) {
        iconLibraryService.delete(id);
        return R.ok();
    }

    // ==================== 点位配置 ====================

    @GetMapping("/point/list")
    public R<List<TwinPointConfig>> pointList(@RequestParam(required = false) Long mapId,
                                               @RequestParam(required = false) Long groupId) {
        return R.ok(pointConfigService.list(mapId, groupId));
    }

    @PostMapping("/point/save")
    public R<TwinPointConfig> pointSave(@RequestBody TwinPointConfig point) {
        return R.ok(pointConfigService.save(point));
    }

    @PostMapping("/point/batch-save")
    public R<Integer> pointBatchSave(@RequestBody List<TwinPointConfig> points) {
        return R.ok(pointConfigService.batchSave(points));
    }

    @PutMapping("/point/update")
    public R<TwinPointConfig> pointUpdate(@RequestBody TwinPointConfig point) {
        return R.ok(pointConfigService.update(point));
    }

    @DeleteMapping("/point/delete/{id}")
    public R<Void> pointDelete(@PathVariable Long id) {
        pointConfigService.delete(id);
        return R.ok();
    }

    @PostMapping("/point/batch-delete")
    public R<Void> pointBatchDelete(@RequestBody List<Long> ids) {
        pointConfigService.batchDelete(ids);
        return R.ok();
    }

    @PutMapping("/point/position")
    public R<Void> pointPosition(@RequestParam Long id,
                                  @RequestParam Double x,
                                  @RequestParam Double y,
                                  @RequestParam(required = false) Double rotation,
                                  @RequestParam(required = false) Integer zIndex) {
        pointConfigService.updatePosition(id, x, y, rotation, zIndex);
        return R.ok();
    }

    @PostMapping("/point/bind-device")
    public R<Void> pointBindDevice(@RequestParam Long id,
                                    @RequestParam Long deviceId,
                                    @RequestParam(required = false) String deviceCode,
                                    @RequestParam(required = false) String deviceName,
                                    @RequestParam(required = false) String bindType) {
        pointConfigService.bindDevice(id, deviceId, deviceCode, deviceName, bindType);
        return R.ok();
    }

    @PostMapping("/point/unbind-device/{id}")
    public R<Void> pointUnbindDevice(@PathVariable Long id) {
        pointConfigService.unbindDevice(id);
        return R.ok();
    }

    // ==================== 点位样式 ====================

    @GetMapping("/style/list")
    public R<List<TwinPointStyle>> styleList(@RequestParam(required = false) String iconType) {
        return R.ok(pointStyleService.list(iconType));
    }

    @PostMapping("/style/save")
    public R<TwinPointStyle> styleSave(@RequestBody TwinPointStyle style) {
        return R.ok(pointStyleService.save(style));
    }

    @PutMapping("/style/update")
    public R<TwinPointStyle> styleUpdate(@RequestBody TwinPointStyle style) {
        return R.ok(pointStyleService.update(style));
    }

    @DeleteMapping("/style/delete/{id}")
    public R<Void> styleDelete(@PathVariable Long id) {
        pointStyleService.delete(id);
        return R.ok();
    }

    @PostMapping("/style/apply-group")
    public R<Integer> styleApplyGroup(@RequestParam Long styleId,
                                       @RequestParam String iconType) {
        return R.ok(pointStyleService.applyToGroup(styleId, iconType));
    }

    // ==================== 接口配置 ====================

    @GetMapping("/api/list")
    public R<List<TwinApiConfig>> apiList(@RequestParam(required = false) String apiType) {
        return R.ok(apiConfigService.list(apiType));
    }

    @PostMapping("/api/save")
    public R<TwinApiConfig> apiSave(@RequestBody TwinApiConfig config) {
        return R.ok(apiConfigService.save(config));
    }

    @PutMapping("/api/update")
    public R<TwinApiConfig> apiUpdate(@RequestBody TwinApiConfig config) {
        return R.ok(apiConfigService.update(config));
    }

    @DeleteMapping("/api/delete/{id}")
    public R<Void> apiDelete(@PathVariable Long id) {
        apiConfigService.delete(id);
        return R.ok();
    }

    @PostMapping("/api/test/{id}")
    public R<Map<String, Object>> apiTest(@PathVariable Long id) {
        return R.ok(apiConfigService.testConnection(id));
    }

    // ==================== 告警规则 ====================

    @GetMapping("/alert-rule/list")
    public R<List<TwinAlertRule>> alertRuleList() {
        return R.ok(alertRuleService.list());
    }

    @PostMapping("/alert-rule/save")
    public R<TwinAlertRule> alertRuleSave(@RequestBody TwinAlertRule rule) {
        return R.ok(alertRuleService.save(rule));
    }

    @PutMapping("/alert-rule/update")
    public R<TwinAlertRule> alertRuleUpdate(@RequestBody TwinAlertRule rule) {
        return R.ok(alertRuleService.update(rule));
    }

    @DeleteMapping("/alert-rule/delete/{id}")
    public R<Void> alertRuleDelete(@PathVariable Long id) {
        alertRuleService.delete(id);
        return R.ok();
    }

    // ==================== 配置版本 ====================

    @GetMapping("/version/list")
    public R<List<TwinConfigVersion>> versionList() {
        return R.ok(configVersionService.list());
    }

    @PostMapping("/version/save-draft")
    public R<TwinConfigVersion> versionSaveDraft(@RequestBody Map<String, String> body) {
        return R.ok(configVersionService.saveDraft(body.get("configSnapshot"), body.get("changeLog")));
    }

    @PostMapping("/version/publish/{id}")
    public R<Void> versionPublish(@PathVariable Long id) {
        configVersionService.publish(id);
        return R.ok();
    }

    @PostMapping("/version/rollback/{id}")
    public R<Void> versionRollback(@PathVariable Long id) {
        configVersionService.rollback(id);
        return R.ok();
    }

    @GetMapping("/version/export/{id}")
    public R<String> versionExport(@PathVariable Long id) {
        return R.ok(configVersionService.exportConfig(id));
    }

    @PostMapping("/version/import")
    public R<Void> versionImport(@RequestBody Map<String, String> body) {
        configVersionService.importConfig(body.get("json"));
        return R.ok();
    }

    // ==================== 操作日志 ====================

    @GetMapping("/log/list")
    public R<PageResult<TwinConfigLog>> logList(@RequestParam(required = false) String module,
                                                  @RequestParam(defaultValue = "1") int pageNum,
                                                  @RequestParam(defaultValue = "20") int pageSize) {
        return R.ok(configLogService.list(module, pageNum, pageSize));
    }

    // ==================== 大屏获取已发布配置 ====================

    @GetMapping("/published")
    public R<TwinConfigVersion> publishedConfig() {
        List<TwinConfigVersion> versions = configVersionService.list();
        return R.ok(versions.stream()
                .filter(v -> "published".equals(v.getStatus()))
                .findFirst()
                .orElse(null));
    }
}
