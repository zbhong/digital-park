package com.digitalpark.modules.twin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.twin.dto.TwinDevicePoiDTO;
import com.digitalpark.modules.twin.dto.TwinIndicatorDTO;
import com.digitalpark.modules.twin.dto.TwinSceneConfigDTO;
import com.digitalpark.modules.twin.entity.*;
import com.digitalpark.modules.twin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数字孪生可视化配置Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/twin/config")
@RequiredArgsConstructor
public class TwinConfigController {

    private final TwinSceneConfigService sceneConfigService;
    private final TwinLayerConfigService layerConfigService;
    private final TwinBuildingPoiService buildingPoiService;
    private final TwinDevicePoiService devicePoiService;
    private final TwinIndicatorService indicatorService;
    private final TwinBaseMapService baseMapService;
    private final TwinPointConfigService pointConfigService;
    private final TwinPointStyleService pointStyleService;
    private final TwinApiConfigService apiConfigService;
    private final TwinAlertRuleService alertRuleService;
    private final TwinConfigVersionService configVersionService;
    private final TwinConfigLogService configLogService;

    // ========== 场景配置 CRUD ==========

    @GetMapping("/scenes")
    public R<PageResult<TwinSceneConfig>> scenePage(TwinSceneConfig query,
                                                     @RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(sceneConfigService.selectPage(query, pageNum, pageSize));
    }

    @GetMapping("/scenes/all")
    public R<List<TwinSceneConfig>> sceneAll() {
        return R.ok(sceneConfigService.selectAll());
    }

    @GetMapping("/scenes/{id}")
    public R<TwinSceneConfig> sceneInfo(@PathVariable Long id) {
        return R.ok(sceneConfigService.selectById(id));
    }

    @GetMapping("/scenes/type/{type}")
    public R<List<TwinSceneConfig>> sceneByType(@PathVariable String type) {
        return R.ok(sceneConfigService.selectByType(type));
    }

    @PostMapping("/scenes")
    public R<TwinSceneConfig> addScene(@RequestBody TwinSceneConfigDTO dto) {
        TwinSceneConfig config = new TwinSceneConfig();
        config.setName(dto.getName());
        config.setType(dto.getType());
        config.setBackground(dto.getBackground());
        config.setZoom(dto.getZoom());
        config.setCenterX(dto.getCenterX());
        config.setCenterY(dto.getCenterY());
        config.setWidth(dto.getWidth());
        config.setHeight(dto.getHeight());
        config.setLayersJson(dto.getLayersJson());
        config.setRemark(dto.getRemark());
        sceneConfigService.insert(config);
        return R.ok(config);
    }

    @PutMapping("/scenes")
    public R<TwinSceneConfig> editScene(@RequestBody TwinSceneConfigDTO dto) {
        TwinSceneConfig config = new TwinSceneConfig();
        config.setId(dto.getId());
        config.setName(dto.getName());
        config.setType(dto.getType());
        config.setBackground(dto.getBackground());
        config.setZoom(dto.getZoom());
        config.setCenterX(dto.getCenterX());
        config.setCenterY(dto.getCenterY());
        config.setWidth(dto.getWidth());
        config.setHeight(dto.getHeight());
        config.setLayersJson(dto.getLayersJson());
        config.setRemark(dto.getRemark());
        sceneConfigService.update(config);
        return R.ok(config);
    }

    @DeleteMapping("/scenes/{id}")
    public R<Void> removeScene(@PathVariable Long id) {
        sceneConfigService.deleteById(id);
        return R.ok();
    }

    // ========== 图层配置 CRUD ==========

    @GetMapping("/layers")
    public R<PageResult<TwinLayerConfig>> layerPage(TwinLayerConfig query,
                                                     @RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(layerConfigService.selectPage(query, pageNum, pageSize));
    }

    @GetMapping("/layers/all")
    public R<List<TwinLayerConfig>> layerAll() {
        return R.ok(layerConfigService.selectAll());
    }

    @GetMapping("/layers/{id}")
    public R<TwinLayerConfig> layerInfo(@PathVariable Long id) {
        return R.ok(layerConfigService.selectById(id));
    }

    @GetMapping("/layers/type/{type}")
    public R<List<TwinLayerConfig>> layerByType(@PathVariable String type) {
        return R.ok(layerConfigService.selectByType(type));
    }

    @PostMapping("/layers")
    public R<TwinLayerConfig> addLayer(@RequestBody TwinLayerConfig config) {
        layerConfigService.insert(config);
        return R.ok(config);
    }

    @PutMapping("/layers")
    public R<TwinLayerConfig> editLayer(@RequestBody TwinLayerConfig config) {
        layerConfigService.update(config);
        return R.ok(config);
    }

    @DeleteMapping("/layers/{id}")
    public R<Void> removeLayer(@PathVariable Long id) {
        layerConfigService.deleteById(id);
        return R.ok();
    }

    // ========== 楼栋POI CRUD ==========

    @GetMapping("/building-pois")
    public R<PageResult<TwinBuildingPoi>> buildingPoiPage(TwinBuildingPoi query,
                                                           @RequestParam(defaultValue = "1") int pageNum,
                                                           @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(buildingPoiService.selectPage(query, pageNum, pageSize));
    }

    @GetMapping("/building-pois/{id}")
    public R<TwinBuildingPoi> buildingPoiInfo(@PathVariable Long id) {
        return R.ok(buildingPoiService.selectById(id));
    }

    @GetMapping("/building-pois/building/{buildingId}")
    public R<List<TwinBuildingPoi>> buildingPoisByBuilding(@PathVariable Long buildingId) {
        return R.ok(buildingPoiService.selectByBuildingId(buildingId));
    }

    @PostMapping("/building-pois")
    public R<TwinBuildingPoi> addBuildingPoi(@RequestBody TwinBuildingPoi poi) {
        buildingPoiService.insert(poi);
        return R.ok(poi);
    }

    @PutMapping("/building-pois")
    public R<TwinBuildingPoi> editBuildingPoi(@RequestBody TwinBuildingPoi poi) {
        buildingPoiService.update(poi);
        return R.ok(poi);
    }

    @DeleteMapping("/building-pois/{id}")
    public R<Void> removeBuildingPoi(@PathVariable Long id) {
        buildingPoiService.deleteById(id);
        return R.ok();
    }

    // ========== 设备POI CRUD + 批量保存 ==========

    @GetMapping("/device-pois")
    public R<PageResult<TwinDevicePoi>> devicePoiPage(TwinDevicePoi query,
                                                       @RequestParam(defaultValue = "1") int pageNum,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(devicePoiService.selectPage(query, pageNum, pageSize));
    }

    @GetMapping("/device-pois/{id}")
    public R<TwinDevicePoi> devicePoiInfo(@PathVariable Long id) {
        return R.ok(devicePoiService.selectById(id));
    }

    @GetMapping("/device-pois/type/{deviceType}")
    public R<List<TwinDevicePoi>> devicePoisByType(@PathVariable String deviceType) {
        return R.ok(devicePoiService.selectByDeviceType(deviceType));
    }

    @PostMapping("/device-pois")
    public R<TwinDevicePoi> addDevicePoi(@RequestBody TwinDevicePoi poi) {
        devicePoiService.insert(poi);
        return R.ok(poi);
    }

    @PutMapping("/device-pois")
    public R<TwinDevicePoi> editDevicePoi(@RequestBody TwinDevicePoi poi) {
        devicePoiService.update(poi);
        return R.ok(poi);
    }

    @PostMapping("/device-pois/batch")
    public R<Integer> batchSaveDevicePois(@RequestBody TwinDevicePoiDTO dto) {
        List<TwinDevicePoi> list = dto.getItems().stream().map(item -> {
            TwinDevicePoi poi = new TwinDevicePoi();
            poi.setId(item.getId());
            poi.setDeviceId(item.getDeviceId());
            poi.setDeviceType(item.getDeviceType());
            poi.setName(item.getName());
            poi.setX(item.getX());
            poi.setY(item.getY());
            poi.setIcon(item.getIcon());
            poi.setStatus(item.getStatus());
            poi.setPopupConfigJson(item.getPopupConfigJson());
            return poi;
        }).collect(Collectors.toList());
        int count = devicePoiService.batchSave(list);
        return R.ok(count);
    }

    @DeleteMapping("/device-pois/{id}")
    public R<Void> removeDevicePoi(@PathVariable Long id) {
        devicePoiService.deleteById(id);
        return R.ok();
    }

    // ========== 指标配置 CRUD ==========

    @GetMapping("/indicators")
    public R<PageResult<TwinIndicator>> indicatorPage(TwinIndicator query,
                                                      @RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(indicatorService.selectPage(query, pageNum, pageSize));
    }

    @GetMapping("/indicators/{id}")
    public R<TwinIndicator> indicatorInfo(@PathVariable Long id) {
        return R.ok(indicatorService.selectById(id));
    }

    @GetMapping("/indicators/category/{category}")
    public R<List<TwinIndicator>> indicatorByCategory(@PathVariable String category) {
        return R.ok(indicatorService.selectByCategory(category));
    }

    @PostMapping("/indicators")
    public R<TwinIndicator> addIndicator(@RequestBody TwinIndicatorDTO dto) {
        TwinIndicator indicator = new TwinIndicator();
        indicator.setName(dto.getName());
        indicator.setCode(dto.getCode());
        indicator.setCategory(dto.getCategory());
        indicator.setUnit(dto.getUnit());
        indicator.setTargetValue(dto.getTargetValue());
        indicator.setWarningValue(dto.getWarningValue());
        indicator.setDangerValue(dto.getDangerValue());
        indicator.setCurrentValue(dto.getCurrentValue());
        indicator.setChartType(dto.getChartType());
        indicator.setMap2dBind(dto.getMap2dBind());
        indicator.setRefreshInterval(dto.getRefreshInterval());
        indicator.setRemark(dto.getRemark());
        indicatorService.insert(indicator);
        return R.ok(indicator);
    }

    @PutMapping("/indicators")
    public R<TwinIndicator> editIndicator(@RequestBody TwinIndicatorDTO dto) {
        TwinIndicator indicator = new TwinIndicator();
        indicator.setId(dto.getId());
        indicator.setName(dto.getName());
        indicator.setCode(dto.getCode());
        indicator.setCategory(dto.getCategory());
        indicator.setUnit(dto.getUnit());
        indicator.setTargetValue(dto.getTargetValue());
        indicator.setWarningValue(dto.getWarningValue());
        indicator.setDangerValue(dto.getDangerValue());
        indicator.setCurrentValue(dto.getCurrentValue());
        indicator.setChartType(dto.getChartType());
        indicator.setMap2dBind(dto.getMap2dBind());
        indicator.setRefreshInterval(dto.getRefreshInterval());
        indicator.setRemark(dto.getRemark());
        indicatorService.update(indicator);
        return R.ok(indicator);
    }

    @DeleteMapping("/indicators/{id}")
    public R<Void> removeIndicator(@PathVariable Long id) {
        indicatorService.deleteById(id);
        return R.ok();
    }

    // ========== 底图管理（使用 TwinVisualConfigController 的端点） ==========

    @PutMapping("/basemap/grid")
    public R<Void> toggleGrid(@RequestParam Long id, @RequestParam Boolean showGrid) {
        TwinBaseMap map = new TwinBaseMap();
        map.setId(id);
        map.setShowGrid(showGrid ? 1 : 0);
        baseMapService.update(map);
        return R.ok();
    }

    // ========== 点位配置 CRUD ==========

    @GetMapping("/points/list")
    public R<List<TwinPointConfig>> listPoints(@RequestParam(required = false) Long mapId,
                                                @RequestParam(required = false) Long groupId) {
        return R.ok(pointConfigService.list(mapId, groupId));
    }

    @PostMapping("/points/batch")
    public R<Void> batchSavePoints(@RequestBody List<TwinPointConfig> points) {
        pointConfigService.batchSave(points);
        return R.ok();
    }

    @DeleteMapping("/points/{id}")
    public R<Void> deletePoint(@PathVariable Long id) {
        pointConfigService.delete(id);
        return R.ok();
    }

    // ========== 点位样式 CRUD ==========

    @GetMapping("/styles/list")
    public R<List<TwinPointStyle>> listStyles(@RequestParam(required = false) String iconType) {
        return R.ok(pointStyleService.list(iconType));
    }

    @PostMapping("/styles")
    public R<TwinPointStyle> addStyle(@RequestBody TwinPointStyle style) {
        return R.ok(pointStyleService.save(style));
    }

    @PutMapping("/styles")
    public R<TwinPointStyle> updateStyle(@RequestBody TwinPointStyle style) {
        return R.ok(pointStyleService.update(style));
    }

    @DeleteMapping("/styles/{id}")
    public R<Void> deleteStyle(@PathVariable Long id) {
        pointStyleService.delete(id);
        return R.ok();
    }

    // ========== 接口配置 CRUD ==========

    @GetMapping("/apis/list")
    public R<List<TwinApiConfig>> listApis(@RequestParam(required = false) String apiType) {
        return R.ok(apiConfigService.list(apiType));
    }

    @PostMapping("/apis")
    public R<TwinApiConfig> addApi(@RequestBody TwinApiConfig config) {
        return R.ok(apiConfigService.save(config));
    }

    @PutMapping("/apis")
    public R<TwinApiConfig> updateApi(@RequestBody TwinApiConfig config) {
        return R.ok(apiConfigService.update(config));
    }

    @DeleteMapping("/apis/{id}")
    public R<Void> deleteApi(@PathVariable Long id) {
        apiConfigService.delete(id);
        return R.ok();
    }

    // ========== 发布管理 ==========

    @GetMapping("/versions/list")
    public R<List<TwinConfigVersion>> listVersions() {
        return R.ok(configVersionService.list());
    }

    @PostMapping("/versions")
    public R<TwinConfigVersion> addVersion(@RequestBody TwinConfigVersion version) {
        return R.ok(configVersionService.saveDraft(null, version.getChangeLog() != null ? version.getChangeLog() : ""));
    }

    @PutMapping("/versions/publish/{id}")
    public R<Void> publishVersion(@PathVariable Long id) {
        configVersionService.publish(id);
        return R.ok();
    }

    @PutMapping("/versions/rollback/{id}")
    public R<Void> rollbackVersion(@PathVariable Long id) {
        configVersionService.rollback(id);
        return R.ok();
    }

    // ========== 操作日志 ==========

    @GetMapping("/logs/list")
    public R<Object> listLogs(@RequestParam(required = false) String module,
                               @RequestParam(required = false) String operationType,
                               @RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "20") int pageSize) {
        return R.ok(configLogService.list(module, pageNum, pageSize));
    }
}
