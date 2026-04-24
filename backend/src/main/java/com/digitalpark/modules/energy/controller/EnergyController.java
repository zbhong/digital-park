package com.digitalpark.modules.energy.controller;

import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.energy.dto.EnergyChargingDTO;
import com.digitalpark.modules.energy.dto.EnergyStorageDTO;
import com.digitalpark.modules.energy.entity.*;
import com.digitalpark.modules.energy.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 能源管理基础控制器
 *
 * @author digitalpark
 */
@Tag(name = "能源管理")
@RestController
@RequestMapping("/energy")
@RequiredArgsConstructor
public class EnergyController extends BaseController {

    private final EnergyMeterService energyMeterService;
    private final EnergyRecordService energyRecordService;
    private final EnergySourceService energySourceService;
    private final EnergyGridService energyGridService;
    private final EnergyLoadService energyLoadService;
    private final EnergyStorageService energyStorageService;
    private final EnergyChargingService energyChargingService;
    private final EnergyChargingRecordService energyChargingRecordService;

    // ==================== 能源计量表 ====================

    @Operation(summary = "计量表列表")
    @GetMapping("/meters")
    public R<List<EnergyMeter>> getMeters(EnergyMeter query) {
        return success(energyMeterService.selectList(query));
    }

    @Operation(summary = "计量表详情")
    @GetMapping("/meters/{id}")
    public R<EnergyMeter> getMeter(@PathVariable Long id) {
        return success(energyMeterService.selectById(id));
    }

    @Operation(summary = "新增计量表")
    @PostMapping("/meters")
    public R<EnergyMeter> addMeter(@RequestBody EnergyMeter meter) {
        energyMeterService.insert(meter);
        return success(meter);
    }

    @Operation(summary = "修改计量表")
    @PutMapping("/meters")
    public R<EnergyMeter> updateMeter(@RequestBody EnergyMeter meter) {
        energyMeterService.update(meter);
        return success(meter);
    }

    @Operation(summary = "删除计量表")
    @DeleteMapping("/meters/{id}")
    public R<Void> deleteMeter(@PathVariable Long id) {
        energyMeterService.deleteById(id);
        return success();
    }

    // ==================== 计量记录 ====================

    @Operation(summary = "计量记录分页列表")
    @GetMapping("/records")
    public R<PageResult<EnergyRecord>> getRecords(
            @RequestParam(required = false) Long meterId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return success(energyRecordService.selectPage(meterId, startTime, endTime, pageNum, pageSize));
    }

    @Operation(summary = "批量导入计量数据")
    @PostMapping("/records/import")
    public R<Integer> importRecords(@RequestBody List<EnergyRecord> records) {
        return success(energyRecordService.batchImport(records));
    }

    @Operation(summary = "峰谷平统计")
    @GetMapping("/records/peak-valley")
    public R<Map<String, Object>> getPeakValley(
            @RequestParam(required = false) Long meterId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return success(energyRecordService.getPeakValley(meterId, startTime, endTime));
    }

    @Operation(summary = "趋势分析")
    @GetMapping("/records/trend")
    public R<Map<String, Object>> getRecordTrend(
            @RequestParam(required = false) Long meterId,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return success(energyRecordService.getTrend(meterId, period, startTime, endTime));
    }

    // ==================== 源侧设备 ====================

    @Operation(summary = "源侧设备列表")
    @GetMapping("/sources")
    public R<List<EnergySource>> getSources(EnergySource query) {
        return success(energySourceService.selectList(query));
    }

    @Operation(summary = "源侧设备详情")
    @GetMapping("/sources/{id}")
    public R<EnergySource> getSource(@PathVariable Long id) {
        return success(energySourceService.selectById(id));
    }

    @Operation(summary = "新增源侧设备")
    @PostMapping("/sources")
    public R<EnergySource> addSource(@RequestBody EnergySource source) {
        energySourceService.insert(source);
        return success(source);
    }

    @Operation(summary = "修改源侧设备")
    @PutMapping("/sources")
    public R<EnergySource> updateSource(@RequestBody EnergySource source) {
        energySourceService.update(source);
        return success(source);
    }

    @Operation(summary = "删除源侧设备")
    @DeleteMapping("/sources/{id}")
    public R<Void> deleteSource(@PathVariable Long id) {
        energySourceService.deleteById(id);
        return success();
    }

    @Operation(summary = "源侧设备实时数据")
    @GetMapping("/sources/{id}/realtime")
    public R<Map<String, Object>> getSourceRealtime(@PathVariable Long id) {
        return success(energySourceService.getRealtimeData(id));
    }

    @Operation(summary = "发电统计")
    @GetMapping("/sources/{id}/generation")
    public R<Map<String, Object>> getGenerationStats(
            @PathVariable Long id,
            @RequestParam(defaultValue = "month") String period) {
        return success(energySourceService.getGenerationStats(id, period));
    }

    // ==================== 配电网 ====================

    @Operation(summary = "配电网列表")
    @GetMapping("/grids")
    public R<List<EnergyGrid>> getGrids(EnergyGrid query) {
        return success(energyGridService.selectList(query));
    }

    @Operation(summary = "配电网详情")
    @GetMapping("/grids/{id}")
    public R<EnergyGrid> getGrid(@PathVariable Long id) {
        return success(energyGridService.selectById(id));
    }

    @Operation(summary = "新增配电网")
    @PostMapping("/grids")
    public R<EnergyGrid> addGrid(@RequestBody EnergyGrid grid) {
        energyGridService.insert(grid);
        return success(grid);
    }

    @Operation(summary = "修改配电网")
    @PutMapping("/grids")
    public R<EnergyGrid> updateGrid(@RequestBody EnergyGrid grid) {
        energyGridService.update(grid);
        return success(grid);
    }

    @Operation(summary = "删除配电网")
    @DeleteMapping("/grids/{id}")
    public R<Void> deleteGrid(@PathVariable Long id) {
        energyGridService.deleteById(id);
        return success();
    }

    // ==================== 负荷管理 ====================

    @Operation(summary = "负荷列表")
    @GetMapping("/loads")
    public R<List<EnergyLoad>> getLoads(EnergyLoad query) {
        return success(energyLoadService.selectList(query));
    }

    @Operation(summary = "负荷详情")
    @GetMapping("/loads/{id}")
    public R<EnergyLoad> getLoad(@PathVariable Long id) {
        return success(energyLoadService.selectById(id));
    }

    @Operation(summary = "新增负荷")
    @PostMapping("/loads")
    public R<EnergyLoad> addLoad(@RequestBody EnergyLoad load) {
        energyLoadService.insert(load);
        return success(load);
    }

    @Operation(summary = "修改负荷")
    @PutMapping("/loads")
    public R<EnergyLoad> updateLoad(@RequestBody EnergyLoad load) {
        energyLoadService.update(load);
        return success(load);
    }

    @Operation(summary = "删除负荷")
    @DeleteMapping("/loads/{id}")
    public R<Void> deleteLoad(@PathVariable Long id) {
        energyLoadService.deleteById(id);
        return success();
    }

    @Operation(summary = "负荷统计")
    @GetMapping("/loads/statistics")
    public R<Map<String, Object>> getLoadStatistics() {
        return success(energyLoadService.getStatistics());
    }

    @Operation(summary = "柔性调度")
    @PostMapping("/loads/{id}/dispatch")
    public R<Map<String, Object>> flexibleDispatch(
            @PathVariable Long id,
            @RequestParam String action,
            @RequestParam(required = false) Double targetPower) {
        return success(energyLoadService.flexibleDispatch(id, action, targetPower));
    }

    // ==================== 储能系统 ====================

    @Operation(summary = "储能系统列表")
    @GetMapping("/storages")
    public R<List<EnergyStorage>> getStorages(EnergyStorage query) {
        return success(energyStorageService.selectList(query));
    }

    @Operation(summary = "储能系统详情")
    @GetMapping("/storages/{id}")
    public R<EnergyStorage> getStorage(@PathVariable Long id) {
        return success(energyStorageService.selectById(id));
    }

    @Operation(summary = "新增储能系统")
    @PostMapping("/storages")
    public R<EnergyStorage> addStorage(@RequestBody EnergyStorage storage) {
        energyStorageService.insert(storage);
        return success(storage);
    }

    @Operation(summary = "修改储能系统")
    @PutMapping("/storages")
    public R<EnergyStorage> updateStorage(@RequestBody EnergyStorage storage) {
        energyStorageService.update(storage);
        return success(storage);
    }

    @Operation(summary = "删除储能系统")
    @DeleteMapping("/storages/{id}")
    public R<Void> deleteStorage(@PathVariable Long id) {
        energyStorageService.deleteById(id);
        return success();
    }

    @Operation(summary = "储能SOC监控")
    @GetMapping("/storages/{id}/soc")
    public R<Map<String, Object>> getStorageSoc(@PathVariable Long id) {
        return success(energyStorageService.getSocMonitor(id));
    }

    @Operation(summary = "储能实时状态")
    @GetMapping("/storages/{id}/status")
    public R<Map<String, Object>> getStorageStatus(@PathVariable Long id) {
        return success(energyStorageService.getRealtimeStatus(id));
    }

    @Operation(summary = "更新储能策略")
    @PutMapping("/storages/strategy")
    public R<Void> updateStorageStrategy(@RequestBody EnergyStorageDTO dto) {
        energyStorageService.updateStrategy(dto);
        return success();
    }

    @Operation(summary = "储能收益统计")
    @GetMapping("/storages/{id}/income")
    public R<Map<String, Object>> getStorageIncome(
            @PathVariable Long id,
            @RequestParam(defaultValue = "month") String period) {
        return success(energyStorageService.getIncomeStats(id, period));
    }

    // ==================== 充电桩 ====================

    @Operation(summary = "充电桩列表")
    @GetMapping("/chargings")
    public R<List<EnergyCharging>> getChargings(EnergyCharging query) {
        return success(energyChargingService.selectList(query));
    }

    @Operation(summary = "充电桩详情")
    @GetMapping("/chargings/{id}")
    public R<EnergyCharging> getCharging(@PathVariable Long id) {
        return success(energyChargingService.selectById(id));
    }

    @Operation(summary = "新增充电桩")
    @PostMapping("/chargings")
    public R<EnergyCharging> addCharging(@RequestBody EnergyCharging charging) {
        energyChargingService.insert(charging);
        return success(charging);
    }

    @Operation(summary = "修改充电桩")
    @PutMapping("/chargings")
    public R<EnergyCharging> updateCharging(@RequestBody EnergyCharging charging) {
        energyChargingService.update(charging);
        return success(charging);
    }

    @Operation(summary = "删除充电桩")
    @DeleteMapping("/chargings/{id}")
    public R<Void> deleteCharging(@PathVariable Long id) {
        energyChargingService.deleteById(id);
        return success();
    }

    @Operation(summary = "充电桩状态监控")
    @GetMapping("/chargings/monitor")
    public R<Map<String, Object>> getChargingMonitor() {
        return success(energyChargingService.getStatusMonitor());
    }

    @Operation(summary = "充电桩运营统计")
    @GetMapping("/chargings/operation")
    public R<Map<String, Object>> getChargingOperation(
            @RequestParam(defaultValue = "week") String period) {
        return success(energyChargingService.getOperationStats(period));
    }

    @Operation(summary = "充电桩调度配置")
    @PutMapping("/chargings/schedule")
    public R<Void> updateChargingSchedule(@RequestBody EnergyChargingDTO dto) {
        energyChargingService.updateSchedule(dto);
        return success();
    }

    // ==================== 充电记录 ====================

    @Operation(summary = "充电记录分页列表")
    @GetMapping("/charging-records")
    public R<PageResult<EnergyChargingRecord>> getChargingRecords(
            @RequestParam(required = false) Long chargingId,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return success(energyChargingRecordService.selectPage(chargingId, plateNumber, startTime, endTime, pageNum, pageSize));
    }

    @Operation(summary = "充电记录收入统计")
    @GetMapping("/charging-records/income")
    public R<Map<String, Object>> getChargingRecordIncome(
            @RequestParam(defaultValue = "month") String period) {
        return success(energyChargingRecordService.getIncomeStats(period));
    }
}
