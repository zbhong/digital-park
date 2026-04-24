package com.digitalpark.modules.twin.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.twin.entity.*;
import com.digitalpark.modules.twin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数字孪生数据驾驶舱Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/twin")
@RequiredArgsConstructor
public class TwinController {

    private final ParkBuildingService buildingService;
    private final ParkAreaService areaService;
    private final TwinIndicatorService indicatorService;
    private final TwinAlertService alertService;

    /**
     * 园区全景概览
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> overview = new HashMap<>();
        // 建筑统计
        List<ParkBuilding> buildings = buildingService.selectAll();
        overview.put("buildingCount", buildings.size());
        // 区域统计
        List<ParkArea> areas = areaService.selectAll();
        overview.put("areaCount", areas.size());
        // 指标概览
        List<TwinIndicator> indicators = indicatorService.selectAll();
        overview.put("indicatorCount", indicators.size());
        overview.put("indicators", indicators);
        // 告警概览
        Map<String, Object> alertStats = alertService.selectStatistics();
        overview.put("alertStats", alertStats);
        // 建筑列表
        overview.put("buildings", buildings);
        // 区域列表
        overview.put("areas", areas);
        return R.ok(overview);
    }

    /**
     * 核心指标列表
     */
    @GetMapping("/indicators")
    public R<List<TwinIndicator>> indicators(
            @RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return R.ok(indicatorService.selectByCategory(category));
        }
        return R.ok(indicatorService.selectAll());
    }

    /**
     * 实时指标数据
     */
    @GetMapping("/indicators/realtime")
    public R<List<TwinIndicator>> realtimeIndicators() {
        return R.ok(indicatorService.selectRealtimeData());
    }

    /**
     * 告警列表(分页)
     */
    @GetMapping("/alerts")
    public R<PageResult<TwinAlert>> alerts(TwinAlert query,
                                            @RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(alertService.selectPage(query, pageNum, pageSize));
    }

    /**
     * 处理告警
     */
    @PutMapping("/alerts/{id}/handle")
    public R<Void> handleAlert(@PathVariable Long id,
                               @RequestParam Long handlerId,
                               @RequestParam String handleResult) {
        alertService.handleAlert(id, handlerId, handleResult);
        return R.ok();
    }

    /**
     * 告警统计
     */
    @GetMapping("/alerts/statistics")
    public R<Map<String, Object>> alertStatistics() {
        return R.ok(alertService.selectStatistics());
    }

    /**
     * 能流数据
     */
    @GetMapping("/energy-flow")
    public R<Map<String, Object>> energyFlow() {
        Map<String, Object> energyFlow = new HashMap<>();
        energyFlow.put("totalConsumption", 12580.5);
        energyFlow.put("solarGeneration", 3200.0);
        energyFlow.put("gridSupply", 9380.5);
        energyFlow.put("storageDischarge", 500.0);
        energyFlow.put("evCharging", 1200.0);
        energyFlow.put("lighting", 2800.0);
        energyFlow.put("hvac", 5600.0);
        energyFlow.put("equipment", 2980.5);
        return R.ok(energyFlow);
    }

    /**
     * 碳流数据
     */
    @GetMapping("/carbon-flow")
    public R<Map<String, Object>> carbonFlow() {
        Map<String, Object> carbonFlow = new HashMap<>();
        carbonFlow.put("totalEmission", 8560.0);
        carbonFlow.put("carbonIntensity", 0.68);
        carbonFlow.put("solarReduction", 2240.0);
        carbonFlow.put("treeEquivalent", 123000);
        carbonFlow.put("monthTrend", List.of(
                Map.of("month", "1月", "emission", 720),
                Map.of("month", "2月", "emission", 680),
                Map.of("month", "3月", "emission", 710),
                Map.of("month", "4月", "emission", 650),
                Map.of("month", "5月", "emission", 700),
                Map.of("month", "6月", "emission", 730)
        ));
        return R.ok(carbonFlow);
    }

    /**
     * 安全态势数据
     */
    @GetMapping("/safety-status")
    public R<Map<String, Object>> safetyStatus() {
        Map<String, Object> safetyStatus = new HashMap<>();
        safetyStatus.put("safetyScore", 92.5);
        safetyStatus.put("monitorPoints", 156);
        safetyStatus.put("onlineRate", 98.7);
        safetyStatus.put("patrolCompletion", 95.2);
        safetyStatus.put("fireEquipment", 320);
        safetyStatus.put("eventToday", 3);
        safetyStatus.put("unhandledAlerts", 2);
        return R.ok(safetyStatus);
    }

    /**
     * 环境监测数据
     */
    @GetMapping("/environment-status")
    public R<Map<String, Object>> environmentStatus() {
        Map<String, Object> envStatus = new HashMap<>();
        envStatus.put("pm25", 35);
        envStatus.put("pm10", 58);
        envStatus.put("temperature", 24.5);
        envStatus.put("humidity", 62);
        envStatus.put("noise", 52);
        envStatus.put("co2", 420);
        envStatus.put("aqiLevel", "优");
        envStatus.put("monitorPoints", 24);
        return R.ok(envStatus);
    }

    // ========== 建筑CRUD ==========

    @GetMapping("/buildings")
    public R<PageResult<ParkBuilding>> buildingPage(ParkBuilding query,
                                                     @RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(buildingService.selectPage(query, pageNum, pageSize));
    }

    @GetMapping("/buildings/{id}")
    public R<ParkBuilding> buildingInfo(@PathVariable Long id) {
        return R.ok(buildingService.selectById(id));
    }

    @GetMapping("/buildings/area/{areaId}")
    public R<List<ParkBuilding>> buildingsByArea(@PathVariable Long areaId) {
        return R.ok(buildingService.selectByAreaId(areaId));
    }

    @PostMapping("/buildings")
    public R<ParkBuilding> addBuilding(@RequestBody ParkBuilding building) {
        buildingService.insert(building);
        return R.ok(building);
    }

    @PutMapping("/buildings")
    public R<ParkBuilding> editBuilding(@RequestBody ParkBuilding building) {
        buildingService.update(building);
        return R.ok(building);
    }

    @DeleteMapping("/buildings/{id}")
    public R<Void> removeBuilding(@PathVariable Long id) {
        buildingService.deleteById(id);
        return R.ok();
    }

    // ========== 区域CRUD ==========

    @GetMapping("/areas")
    public R<List<ParkArea>> areaTree() {
        return R.ok(areaService.selectTree());
    }

    @GetMapping("/areas/page")
    public R<PageResult<ParkArea>> areaPage(ParkArea query,
                                             @RequestParam(defaultValue = "1") int pageNum,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(areaService.selectPage(query, pageNum, pageSize));
    }

    @GetMapping("/areas/{id}")
    public R<ParkArea> areaInfo(@PathVariable Long id) {
        return R.ok(areaService.selectById(id));
    }

    @GetMapping("/areas/children/{parentId}")
    public R<List<ParkArea>> areaChildren(@PathVariable Long parentId) {
        return R.ok(areaService.selectByParentId(parentId));
    }

    @PostMapping("/areas")
    public R<ParkArea> addArea(@RequestBody ParkArea area) {
        areaService.insert(area);
        return R.ok(area);
    }

    @PutMapping("/areas")
    public R<ParkArea> editArea(@RequestBody ParkArea area) {
        areaService.update(area);
        return R.ok(area);
    }

    @DeleteMapping("/areas/{id}")
    public R<Void> removeArea(@PathVariable Long id) {
        areaService.deleteById(id);
        return R.ok();
    }

    // ========== 楼层管理 ==========

    /**
     * 获取所有楼层列表
     */
    @GetMapping("/floors")
    public R<List<Map<String, Object>>> floors() {
        List<ParkBuilding> buildings = buildingService.selectAll();
        List<Map<String, Object>> floors = new ArrayList<>();
        for (ParkBuilding building : buildings) {
            int floorCount = building.getFloors() != null ? building.getFloors() : 0;
            for (int i = 1; i <= floorCount; i++) {
                Map<String, Object> floor = new HashMap<>();
                floor.put("buildingId", building.getId());
                floor.put("buildingName", building.getName());
                floor.put("floorNumber", i);
                floor.put("floorName", building.getName() + "-" + i + "F");
                floors.add(floor);
            }
        }
        return R.ok(floors);
    }

    /**
     * 获取指定建筑的楼层列表
     */
    @GetMapping("/floors/{buildingId}")
    public R<List<Map<String, Object>>> buildingFloors(@PathVariable Long buildingId) {
        ParkBuilding building = buildingService.selectById(buildingId);
        List<Map<String, Object>> floors = new ArrayList<>();
        if (building != null) {
            int floorCount = building.getFloors() != null ? building.getFloors() : 0;
            for (int i = 1; i <= floorCount; i++) {
                Map<String, Object> floor = new HashMap<>();
                floor.put("buildingId", building.getId());
                floor.put("buildingName", building.getName());
                floor.put("floorNumber", i);
                floor.put("floorName", building.getName() + "-" + i + "F");
                floors.add(floor);
            }
        }
        return R.ok(floors);
    }

    /**
     * 获取楼层驾驶舱数据
     */
    @GetMapping("/floor-dashboard/{floorId}")
    public R<Map<String, Object>> floorDashboard(@PathVariable Long floorId) {
        Map<String, Object> dashboard = new HashMap<>();
        // floorId格式: buildingId-floorNumber
        Long buildingId;
        int floorNumber;
        String floorIdStr = String.valueOf(floorId);
        if (floorIdStr.contains("-")) {
            String[] parts = floorIdStr.split("-");
            buildingId = Long.parseLong(parts[0]);
            floorNumber = Integer.parseInt(parts[1]);
        } else {
            buildingId = Long.parseLong(floorIdStr);
            floorNumber = 1;
        }
        ParkBuilding building = buildingService.selectById(buildingId);
        if (building != null) {
            dashboard.put("buildingName", building.getName());
            dashboard.put("floorNumber", floorNumber);
            dashboard.put("floorName", building.getName() + "-" + floorNumber + "F");
            dashboard.put("totalArea", building.getTotalArea());
            dashboard.put("usedArea", building.getUsedArea());
        }
        // 楼层指标
        List<TwinIndicator> indicators = indicatorService.selectByCategory("楼层");
        dashboard.put("indicators", indicators);
        // 楼层告警
        Map<String, Object> alertStats = alertService.selectStatistics();
        dashboard.put("alertStats", alertStats);
        return R.ok(dashboard);
    }
}
