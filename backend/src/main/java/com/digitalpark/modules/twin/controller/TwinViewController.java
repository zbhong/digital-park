package com.digitalpark.modules.twin.controller;

import com.digitalpark.common.result.R;
import com.digitalpark.modules.twin.entity.*;
import com.digitalpark.modules.twin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数字孪生前端视图数据Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/twin/view")
@RequiredArgsConstructor
public class TwinViewController {

    private final TwinSceneConfigService sceneConfigService;
    private final TwinLayerConfigService layerConfigService;
    private final TwinBuildingPoiService buildingPoiService;
    private final TwinDevicePoiService devicePoiService;
    private final TwinIndicatorService indicatorService;
    private final ParkBuildingService buildingService;

    /**
     * 获取场景完整数据(含图层、POI、指标)
     */
    @GetMapping("/scene/{id}")
    public R<Map<String, Object>> getSceneData(@PathVariable Long id) {
        TwinSceneConfig scene = sceneConfigService.selectById(id);
        Map<String, Object> sceneData = new HashMap<>();
        sceneData.put("scene", scene);
        sceneData.put("layers", layerConfigService.selectAll());
        sceneData.put("buildingPois", buildingPoiService.selectAll());
        sceneData.put("devicePois", devicePoiService.selectAll());
        sceneData.put("indicators", indicatorService.selectAll());
        return R.ok(sceneData);
    }

    /**
     * 获取所有建筑及POI
     */
    @GetMapping("/buildings")
    public R<Map<String, Object>> getBuildings() {
        Map<String, Object> data = new HashMap<>();
        List<ParkBuilding> buildings = buildingService.selectAll();
        List<TwinBuildingPoi> pois = buildingPoiService.selectAll();
        data.put("buildings", buildings);
        data.put("pois", pois);
        return R.ok(data);
    }

    /**
     * 获取所有设备POI
     */
    @GetMapping("/devices")
    public R<List<TwinDevicePoi>> getDevices() {
        return R.ok(devicePoiService.selectAll());
    }

    /**
     * 获取图层列表
     */
    @GetMapping("/layers")
    public R<List<TwinLayerConfig>> getLayers() {
        return R.ok(layerConfigService.selectAll());
    }
}
