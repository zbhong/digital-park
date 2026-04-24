package com.digitalpark.modules.energy.controller;

import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.energy.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 能源报告控制器
 *
 * @author digitalpark
 */
@Tag(name = "能源报告")
@RestController
@RequestMapping("/energy/report")
@RequiredArgsConstructor
public class EnergyReportController extends BaseController {

    private final EnergyRecordService energyRecordService;
    private final EnergySourceService energySourceService;
    private final EnergyStorageService energyStorageService;
    private final EnergyChargingService energyChargingService;
    private final EnergyChargingRecordService energyChargingRecordService;

    @Operation(summary = "能耗日报")
    @GetMapping("/daily")
    public R<Map<String, Object>> getDailyReport(@RequestParam(required = false) String date) {
        Map<String, Object> result = new HashMap<>();

        result.put("date", date != null ? date : "2026-04-20");
        result.put("totalConsumption", 8520.5);
        result.put("totalCost", 7242.43);
        result.put("totalCarbon", 5.82);
        result.put("yearOnYearConsumption", -3.2);
        result.put("monthOnMonthConsumption", 1.5);

        List<Map<String, Object>> byType = new ArrayList<>();
        String[] types = {"电力", "水", "天然气", "蒸汽"};
        double[] consumptions = {6800.0, 120.5, 280.0, 1320.0};
        double[] costs = {5780.0, 626.6, 980.0, 855.83};
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", types[i]);
            item.put("consumption", consumptions[i]);
            item.put("cost", costs[i]);
            byType.add(item);
        }
        result.put("byType", byType);

        List<Map<String, Object>> byArea = new ArrayList<>();
        String[] areas = {"A区", "B区", "C区", "D区"};
        double[] areaConsumptions = {2850.5, 2320.3, 1860.8, 1489.0};
        for (int i = 0; i < areas.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("area", areas[i]);
            item.put("consumption", areaConsumptions[i]);
            byArea.add(item);
        }
        result.put("byArea", byArea);

        return success(result);
    }

    @Operation(summary = "能耗月报")
    @GetMapping("/monthly")
    public R<Map<String, Object>> getMonthlyReport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        Map<String, Object> result = new HashMap<>();

        result.put("year", year != null ? year : 2026);
        result.put("month", month != null ? month : 4);
        result.put("totalConsumption", 215600.0);
        result.put("totalCost", 183260.0);
        result.put("totalCarbon", 147.5);
        result.put("yearOnYearConsumption", -5.8);
        result.put("monthOnMonthConsumption", 2.3);

        List<Map<String, Object>> dailyTrend = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("day", i);
            item.put("consumption", 6500 + Math.random() * 3000);
            item.put("cost", 5500 + Math.random() * 2500);
            dailyTrend.add(item);
        }
        result.put("dailyTrend", dailyTrend);

        return success(result);
    }

    @Operation(summary = "能耗年报")
    @GetMapping("/yearly")
    public R<Map<String, Object>> getYearlyReport(@RequestParam(required = false) Integer year) {
        Map<String, Object> result = new HashMap<>();

        result.put("year", year != null ? year : 2026);
        result.put("totalConsumption", 2587200.0);
        result.put("totalCost", 2199120.0);
        result.put("totalCarbon", 1770.0);
        result.put("yearOnYearConsumption", -6.5);

        List<Map<String, Object>> monthlyTrend = new ArrayList<>();
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        double[] consumptions = {198000, 185000, 210000, 215600, 235000, 258000, 272000, 265000, 238000, 218000, 195000, 188600};
        for (int i = 0; i < months.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", months[i]);
            item.put("consumption", consumptions[i]);
            monthlyTrend.add(item);
        }
        result.put("monthlyTrend", monthlyTrend);

        return success(result);
    }

    @Operation(summary = "综合能源概览")
    @GetMapping("/overview")
    public R<Map<String, Object>> getOverview() {
        Map<String, Object> result = new HashMap<>();

        // 今日概览
        Map<String, Object> today = new HashMap<>();
        today.put("consumption", 8520.5);
        today.put("cost", 7242.43);
        today.put("carbon", 5.82);
        today.put("selfSupplyRate", 27.5);
        result.put("today", today);

        // 本月概览
        Map<String, Object> month = new HashMap<>();
        month.put("consumption", 215600.0);
        month.put("cost", 183260.0);
        month.put("carbon", 147.5);
        month.put("yearOnYear", -5.8);
        result.put("month", month);

        // 实时功率
        Map<String, Object> realtime = new HashMap<>();
        realtime.put("totalPower", 5230.5);
        realtime.put("gridPower", 3800.0);
        realtime.put("solarPower", 1200.0);
        realtime.put("storagePower", -230.0);
        realtime.put("loadRate", 61.5);
        result.put("realtime", realtime);

        // 告警
        List<Map<String, Object>> alarms = new ArrayList<>();
        Map<String, Object> alarm1 = new HashMap<>();
        alarm1.put("level", "HIGH");
        alarm1.put("content", "A区变压器负载率超过90%");
        alarm1.put("time", "2026-04-20 14:30:00");
        alarms.add(alarm1);

        Map<String, Object> alarm2 = new HashMap<>();
        alarm2.put("level", "MEDIUM");
        alarm2.put("content", "3号充电桩通信异常");
        alarm2.put("time", "2026-04-20 13:15:00");
        alarms.add(alarm2);

        result.put("alarms", alarms);
        result.put("alarmCount", 2);

        return success(result);
    }

    @Operation(summary = "节能建议报告")
    @GetMapping("/suggestions")
    public R<Map<String, Object>> getSuggestions() {
        Map<String, Object> result = new HashMap<>();

        result.put("totalSavingPotential", 21500.0);
        result.put("totalCarbonReduction", 18.5);

        List<Map<String, Object>> suggestions = new ArrayList<>();

        Map<String, Object> s1 = new HashMap<>();
        s1.put("priority", "HIGH");
        s1.put("category", "空调系统");
        s1.put("content", "优化空调运行策略，将冷冻水温度设定值提高2°C，预计年节能12万kWh");
        s1.put("saving", 102000.0);
        s1.put("carbonReduction", 8.5);
        s1.put("paybackPeriod", "8个月");
        suggestions.add(s1);

        Map<String, Object> s2 = new HashMap<>();
        s2.put("priority", "HIGH");
        s2.put("category", "照明系统");
        s2.put("content", "在车库和公共区域安装LED灯具和智能感应控制，预计年节能5万kWh");
        s2.put("saving", 42500.0);
        s2.put("carbonReduction", 3.6);
        s2.put("paybackPeriod", "12个月");
        suggestions.add(s2);

        Map<String, Object> s3 = new HashMap<>();
        s3.put("priority", "MEDIUM");
        s3.put("category", "储能系统");
        s3.put("content", "利用峰谷电价差优化储能充放电策略，预计年节约电费8万元");
        s3.put("saving", 80000.0);
        s3.put("carbonReduction", 0);
        s3.put("paybackPeriod", "18个月");
        suggestions.add(s3);

        Map<String, Object> s4 = new HashMap<>();
        s4.put("priority", "MEDIUM");
        s4.put("category", "光伏系统");
        s4.put("content", "增加屋顶光伏装机容量500kWp，预计年发电量55万kWh");
        s4.put("saving", 467500.0);
        s4.put("carbonReduction", 38.5);
        s4.put("paybackPeriod", "5年");
        suggestions.add(s4);

        Map<String, Object> s5 = new HashMap<>();
        s5.put("priority", "LOW");
        s5.put("category", "电梯系统");
        s5.put("content", "安装电梯能量回馈装置，预计年节能1.5万kWh");
        s5.put("saving", 12750.0);
        s5.put("carbonReduction", 1.1);
        s5.put("paybackPeriod", "18个月");
        suggestions.add(s5);

        result.put("suggestions", suggestions);

        return success(result);
    }
}
