package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.energy.entity.EnergyLoad;
import com.digitalpark.modules.energy.mapper.EnergyLoadMapper;
import com.digitalpark.modules.energy.service.EnergyLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 负荷管理Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergyLoadServiceImpl implements EnergyLoadService {

    private final EnergyLoadMapper energyLoadMapper;

    @Override
    public List<EnergyLoad> selectList(EnergyLoad query) {
        LambdaQueryWrapper<EnergyLoad> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(EnergyLoad::getName, query.getName());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(EnergyLoad::getType, query.getType());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EnergyLoad::getAreaId, query.getAreaId());
        }
        if (query.getBuildingId() != null) {
            wrapper.eq(EnergyLoad::getBuildingId, query.getBuildingId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(EnergyLoad::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(EnergyLoad::getCreateTime);
        return energyLoadMapper.selectList(wrapper);
    }

    @Override
    public EnergyLoad selectById(Long id) {
        return energyLoadMapper.selectById(id);
    }

    @Override
    public int insert(EnergyLoad load) {
        if (load.getName() == null || load.getName().isEmpty()) {
            throw new BusinessException("负荷名称不能为空");
        }
        return energyLoadMapper.insert(load);
    }

    @Override
    public int update(EnergyLoad load) {
        if (load.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return energyLoadMapper.updateById(load);
    }

    @Override
    public int deleteById(Long id) {
        return energyLoadMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();

        result.put("totalRatedPower", 8500.0);
        result.put("totalCurrentPower", 5230.5);
        result.put("loadRate", 61.5);
        result.put("peakLoad", 7800.0);
        result.put("valleyLoad", 2100.0);
        result.put("avgLoad", 5200.0);

        List<Map<String, Object>> byType = new ArrayList<>();
        String[] types = {"照明", "空调", "水泵", "风机", "生产设备", "其他"};
        double[] powers = {850, 2800, 520, 380, 1200, 480};
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", types[i]);
            item.put("power", powers[i]);
            item.put("ratio", Math.round(powers[i] / 6230.0 * 1000.0) / 10.0);
            byType.add(item);
        }
        result.put("byType", byType);

        return result;
    }

    @Override
    public Map<String, Object> flexibleDispatch(Long loadId, String action, Double targetPower) {
        Map<String, Object> result = new HashMap<>();
        result.put("loadId", loadId);
        result.put("action", action);
        result.put("targetPower", targetPower);
        result.put("status", "success");
        result.put("message", "调度指令已下发");
        return result;
    }

    @Override
    public Map<String, Object> getLoadAnalysis(String type, String startDate, String endDate, String granularity) {
        Map<String, Object> result = new HashMap<>();

        List<String> labels;
        List<Double> values;
        List<Double> maxValues;
        List<Double> minValues;

        if ("hour".equals(granularity)) {
            labels = Arrays.asList("00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00");
            values = Arrays.asList(2100.0, 1800.0, 4500.0, 6200.0, 5800.0, 5200.0, 2800.0);
            maxValues = Arrays.asList(2500.0, 2100.0, 5200.0, 7800.0, 6500.0, 6000.0, 3200.0);
            minValues = Arrays.asList(1800.0, 1500.0, 3800.0, 5000.0, 4800.0, 4200.0, 2200.0);
        } else {
            labels = Arrays.asList("周一", "周二", "周三", "周四", "周五", "周六", "周日");
            values = Arrays.asList(5200.0, 5400.0, 5100.0, 5500.0, 5300.0, 3200.0, 2800.0);
            maxValues = Arrays.asList(6800.0, 7200.0, 6500.0, 7500.0, 7000.0, 4500.0, 3800.0);
            minValues = Arrays.asList(2100.0, 2200.0, 2000.0, 2300.0, 2100.0, 1500.0, 1200.0);
        }

        result.put("labels", labels);
        result.put("values", values);
        result.put("maxValues", maxValues);
        result.put("minValues", minValues);
        result.put("avgLoad", 4500.0);
        result.put("maxLoad", 7800.0);
        result.put("minLoad", 1200.0);
        result.put("loadFactor", 57.7);

        return result;
    }
}
