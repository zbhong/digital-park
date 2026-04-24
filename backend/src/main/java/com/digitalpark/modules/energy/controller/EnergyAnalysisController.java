package com.digitalpark.modules.energy.controller;

import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.energy.dto.EnergyRecordDTO;
import com.digitalpark.modules.energy.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 能源统计分析控制器
 *
 * @author digitalpark
 */
@Tag(name = "能源统计分析")
@RestController
@RequestMapping("/energy/analysis")
@RequiredArgsConstructor
public class EnergyAnalysisController extends BaseController {

    private final EnergyRecordService energyRecordService;
    private final EnergyLoadService energyLoadService;
    private final EnergyStorageService energyStorageService;
    private final EnergyChargingService energyChargingService;

    @Operation(summary = "能耗统计(按时间/区域/类型)")
    @GetMapping("/consumption")
    public R<Map<String, Object>> getConsumption(EnergyRecordDTO dto) {
        return success(energyRecordService.getConsumptionStats(dto));
    }

    @Operation(summary = "费用统计")
    @GetMapping("/cost")
    public R<Map<String, Object>> getCost(EnergyRecordDTO dto) {
        return success(energyRecordService.getCostStats(dto));
    }

    @Operation(summary = "碳排放统计")
    @GetMapping("/carbon")
    public R<Map<String, Object>> getCarbon(EnergyRecordDTO dto) {
        return success(energyRecordService.getCarbonStats(dto));
    }

    @Operation(summary = "对标分析")
    @GetMapping("/compare")
    public R<Map<String, Object>> getCompare(EnergyRecordDTO dto) {
        return success(energyRecordService.getBenchmark(dto));
    }

    @Operation(summary = "负荷分析")
    @GetMapping("/load")
    public R<Map<String, Object>> getLoadAnalysis(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "day") String granularity) {
        return success(energyLoadService.getLoadAnalysis(type, startDate, endDate, granularity));
    }

    @Operation(summary = "储能分析")
    @GetMapping("/storage")
    public R<Map<String, Object>> getStorageAnalysis(
            @RequestParam(required = false) Long storageId,
            @RequestParam(defaultValue = "month") String period) {
        return success(energyStorageService.getIncomeStats(storageId, period));
    }

    @Operation(summary = "充电桩运营分析")
    @GetMapping("/charging")
    public R<Map<String, Object>> getChargingAnalysis(
            @RequestParam(defaultValue = "week") String period) {
        return success(energyChargingService.getOperationStats(period));
    }

    @Operation(summary = "能源流向数据")
    @GetMapping("/flow")
    public R<Map<String, Object>> getEnergyFlow() {
        Map<String, Object> result = new java.util.HashMap<>();

        // 供给侧
        java.util.List<Map<String, Object>> supply = new java.util.ArrayList<>();
        Map<String, Object> grid = new java.util.HashMap<>();
        grid.put("name", "市电");
        grid.put("value", 6800.0);
        grid.put("unit", "kWh");
        grid.put("ratio", 72.5);
        supply.add(grid);

        Map<String, Object> solar = new java.util.HashMap<>();
        solar.put("name", "光伏");
        solar.put("value", 1850.0);
        solar.put("unit", "kWh");
        solar.put("ratio", 19.7);
        supply.add(solar);

        Map<String, Object> wind = new java.util.HashMap<>();
        wind.put("name", "风电");
        wind.put("value", 520.0);
        wind.put("unit", "kWh");
        wind.put("ratio", 5.5);
        supply.add(wind);

        Map<String, Object> storage = new java.util.HashMap<>();
        storage.put("name", "储能放电");
        storage.put("value", 230.0);
        storage.put("unit", "kWh");
        storage.put("ratio", 2.3);
        supply.add(storage);

        result.put("supply", supply);
        result.put("totalSupply", 9400.0);

        // 需求侧
        java.util.List<Map<String, Object>> demand = new java.util.ArrayList<>();
        Map<String, Object> hvac = new java.util.HashMap<>();
        hvac.put("name", "空调暖通");
        hvac.put("value", 3200.0);
        hvac.put("unit", "kWh");
        hvac.put("ratio", 34.0);
        demand.add(hvac);

        Map<String, Object> production = new java.util.HashMap<>();
        production.put("name", "生产设备");
        production.put("value", 2800.0);
        production.put("unit", "kWh");
        production.put("ratio", 29.8);
        demand.add(production);

        Map<String, Object> lighting = new java.util.HashMap<>();
        lighting.put("name", "照明");
        lighting.put("value", 1200.0);
        lighting.put("unit", "kWh");
        lighting.put("ratio", 12.8);
        demand.add(lighting);

        Map<String, Object> charging = new java.util.HashMap<>();
        charging.put("name", "充电桩");
        charging.put("value", 980.0);
        charging.put("unit", "kWh");
        charging.put("ratio", 10.4);
        demand.add(charging);

        Map<String, Object> other = new java.util.HashMap<>();
        other.put("name", "其他");
        other.put("value", 1220.0);
        other.put("unit", "kWh");
        other.put("ratio", 13.0);
        demand.add(other);

        result.put("demand", demand);
        result.put("totalDemand", 9400.0);

        // 自给率
        result.put("selfSupplyRate", 27.5);
        result.put("renewableRatio", 25.2);

        return success(result);
    }
}
