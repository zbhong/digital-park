package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.energy.dto.EnergyRecordDTO;
import com.digitalpark.modules.energy.entity.EnergyRecord;
import com.digitalpark.modules.energy.mapper.EnergyRecordMapper;
import com.digitalpark.modules.energy.service.EnergyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 能源计量记录Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergyRecordServiceImpl implements EnergyRecordService {

    private final EnergyRecordMapper energyRecordMapper;

    @Override
    public PageResult<EnergyRecord> selectPage(Long meterId, LocalDateTime startTime, LocalDateTime endTime, int pageNum, int pageSize) {
        LambdaQueryWrapper<EnergyRecord> wrapper = new LambdaQueryWrapper<>();
        if (meterId != null) {
            wrapper.eq(EnergyRecord::getMeterId, meterId);
        }
        if (startTime != null) {
            wrapper.ge(EnergyRecord::getRecordTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(EnergyRecord::getRecordTime, endTime);
        }
        wrapper.orderByDesc(EnergyRecord::getRecordTime);
        Page<EnergyRecord> page = energyRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<EnergyRecord> selectList(EnergyRecord query) {
        LambdaQueryWrapper<EnergyRecord> wrapper = new LambdaQueryWrapper<>();
        if (query.getMeterId() != null) {
            wrapper.eq(EnergyRecord::getMeterId, query.getMeterId());
        }
        if (query.getMeterNo() != null && !query.getMeterNo().isEmpty()) {
            wrapper.eq(EnergyRecord::getMeterNo, query.getMeterNo());
        }
        wrapper.orderByDesc(EnergyRecord::getRecordTime);
        return energyRecordMapper.selectList(wrapper);
    }

    @Override
    public int insert(EnergyRecord record) {
        return energyRecordMapper.insert(record);
    }

    @Override
    public int batchImport(List<EnergyRecord> records) {
        int count = 0;
        for (EnergyRecord record : records) {
            count += energyRecordMapper.insert(record);
        }
        return count;
    }

    @Override
    public Map<String, Object> getConsumptionStats(EnergyRecordDTO dto) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> groups = new ArrayList<>();

        String groupBy = dto.getGroupBy() != null ? dto.getGroupBy() : "type";

        if ("area".equals(groupBy)) {
            String[] areas = {"A区", "B区", "C区", "D区"};
            double[] values = {2850.5, 1920.3, 1560.8, 980.2};
            for (int i = 0; i < areas.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", areas[i]);
                item.put("consumption", values[i]);
                item.put("cost", values[i] * 0.85);
                groups.add(item);
            }
        } else if ("time".equals(groupBy)) {
            String granularity = dto.getGranularity() != null ? dto.getGranularity() : "day";
            if ("hour".equals(granularity)) {
                for (int i = 0; i < 24; i++) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", String.format("%02d:00", i));
                    item.put("consumption", 100 + Math.random() * 200);
                    groups.add(item);
                }
            } else if ("month".equals(granularity)) {
                String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
                double[] values = {52000, 48000, 51000, 55000, 62000, 68000, 72000, 70000, 61000, 56000, 50000, 47000};
                for (int i = 0; i < months.length; i++) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", months[i]);
                    item.put("consumption", values[i]);
                    groups.add(item);
                }
            } else {
                String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                double[] values = {1850, 1920, 1780, 2050, 1890, 1200, 980};
                for (int i = 0; i < days.length; i++) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", days[i]);
                    item.put("consumption", values[i]);
                    groups.add(item);
                }
            }
        } else {
            String[] types = {"电力", "水", "天然气", "蒸汽", "压缩空气"};
            double[] values = {5800.0, 320.5, 560.2, 380.0, 251.1};
            String[] units = {"kWh", "t", "m3", "GJ", "m3"};
            for (int i = 0; i < types.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", types[i]);
                item.put("consumption", values[i]);
                item.put("unit", units[i]);
                groups.add(item);
            }
        }

        result.put("groups", groups);
        double totalConsumption = groups.stream()
                .mapToDouble(g -> ((Number) g.get("consumption")).doubleValue())
                .sum();
        result.put("totalConsumption", totalConsumption);
        result.put("yearOnYear", 5.2);
        result.put("monthOnMonth", -1.8);
        return result;
    }

    @Override
    public Map<String, Object> getCostStats(EnergyRecordDTO dto) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> costByType = new ArrayList<>();
        String[] types = {"电力", "水", "天然气", "蒸汽", "压缩空气"};
        double[] costs = {4930.0, 1666.6, 1960.7, 32300.0, 502.2};
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", types[i]);
            item.put("cost", costs[i]);
            item.put("ratio", Math.round(costs[i] / 41359.5 * 1000.0) / 10.0);
            costByType.add(item);
        }

        result.put("costByType", costByType);
        result.put("totalCost", 41359.5);
        result.put("yearOnYear", 3.8);
        result.put("monthOnMonth", -0.5);

        Map<String, Object> peakValley = new HashMap<>();
        peakValley.put("peakCost", 15230.0);
        peakValley.put("flatCost", 18520.0);
        peakValley.put("valleyCost", 7609.5);
        result.put("peakValley", peakValley);

        return result;
    }

    @Override
    public Map<String, Object> getCarbonStats(EnergyRecordDTO dto) {
        Map<String, Object> result = new HashMap<>();

        result.put("totalEmission", 3850.6);
        result.put("unit", "tCO2");
        result.put("yearOnYear", -8.5);
        result.put("monthOnMonth", -2.1);

        List<Map<String, Object>> byType = new ArrayList<>();
        String[] types = {"电力", "天然气", "蒸汽"};
        double[] emissions = {2850.3, 650.2, 350.1};
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", types[i]);
            item.put("emission", emissions[i]);
            item.put("ratio", Math.round(emissions[i] / 3850.6 * 1000.0) / 10.0);
            byType.add(item);
        }
        result.put("byType", byType);

        List<Map<String, Object>> trend = new ArrayList<>();
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        double[] values = {380, 350, 365, 340, 320, 310, 305, 300, 290, 285, 280, 265};
        for (int i = 0; i < months.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", months[i]);
            item.put("emission", values[i]);
            trend.add(item);
        }
        result.put("trend", trend);

        result.put("carbonIntensity", 0.52);
        result.put("carbonIntensityUnit", "tCO2/万元产值");

        return result;
    }

    @Override
    public Map<String, Object> getBenchmark(EnergyRecordDTO dto) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> current = new HashMap<>();
        current.put("energyIntensity", 45.8);
        current.put("unit", "kWh/m2");
        current.put("carbonIntensity", 0.52);
        current.put("carbonUnit", "tCO2/万元产值");
        result.put("current", current);

        Map<String, Object> benchmark = new HashMap<>();
        benchmark.put("energyIntensity", 38.5);
        benchmark.put("carbonIntensity", 0.42);
        result.put("benchmark", benchmark);

        Map<String, Object> advanced = new HashMap<>();
        advanced.put("energyIntensity", 32.0);
        advanced.put("carbonIntensity", 0.35);
        result.put("advanced", advanced);

        result.put("energySavingRate", 16.0);
        result.put("carbonReductionRate", 19.2);

        List<Map<String, Object>> suggestions = new ArrayList<>();
        Map<String, Object> s1 = new HashMap<>();
        s1.put("type", "HIGH");
        s1.put("content", "单位面积能耗高于行业基准值19%，建议优化空调运行策略");
        s1.put("saving", 12500.0);
        suggestions.add(s1);

        Map<String, Object> s2 = new HashMap<>();
        s2.put("type", "MEDIUM");
        s2.put("content", "照明系统能耗偏高，建议更换LED灯具并加装智能控制");
        s2.put("saving", 5800.0);
        suggestions.add(s2);

        Map<String, Object> s3 = new HashMap<>();
        s3.put("type", "LOW");
        s3.put("content", "可考虑增加光伏发电比例以降低碳排放");
        s3.put("saving", 3200.0);
        suggestions.add(s3);

        result.put("suggestions", suggestions);

        return result;
    }

    @Override
    public Map<String, Object> getPeakValley(Long meterId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> peak = new HashMap<>();
        peak.put("name", "峰时段");
        peak.put("consumption", 2850.5);
        peak.put("cost", 3612.1);
        peak.put("price", 1.267);
        peak.put("ratio", 42.5);
        result.put("peak", peak);

        Map<String, Object> flat = new HashMap<>();
        flat.put("name", "平时段");
        flat.put("consumption", 2450.3);
        flat.put("cost", 2082.8);
        flat.put("price", 0.850);
        flat.put("ratio", 36.5);
        result.put("flat", flat);

        Map<String, Object> valley = new HashMap<>();
        valley.put("name", "谷时段");
        valley.put("consumption", 1411.2);
        valley.put("cost", 423.4);
        valley.put("price", 0.300);
        valley.put("ratio", 21.0);
        result.put("valley", valley);

        result.put("totalConsumption", 6712.0);
        result.put("totalCost", 6118.3);
        result.put("avgPrice", 0.912);

        double savingPotential = 2850.5 * (1.267 - 0.850);
        result.put("savingPotential", Math.round(savingPotential * 100.0) / 100.0);

        return result;
    }

    @Override
    public Map<String, Object> getTrend(Long meterId, String period, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels;
        List<Double> values;

        if ("day".equals(period)) {
            labels = Arrays.asList("00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00");
            values = Arrays.asList(120.5, 85.3, 180.2, 250.8, 220.5, 195.3, 130.2);
        } else if ("month".equals(period)) {
            labels = Arrays.asList("1日", "5日", "10日", "15日", "20日", "25日", "30日");
            values = Arrays.asList(1850.0, 1920.5, 1780.3, 2050.8, 1890.2, 1960.5, 1820.0);
        } else {
            labels = Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月",
                    "7月", "8月", "9月", "10月", "11月", "12月");
            values = Arrays.asList(52000.0, 48000.0, 51000.0, 55000.0, 62000.0, 68000.0,
                    72000.0, 70000.0, 61000.0, 56000.0, 50000.0, 47000.0);
        }

        result.put("period", period);
        result.put("labels", labels);
        result.put("values", values);
        return result;
    }
}
