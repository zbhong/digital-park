package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.energy.entity.EnergySource;
import com.digitalpark.modules.energy.mapper.EnergySourceMapper;
import com.digitalpark.modules.energy.service.EnergySourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 源侧设备Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergySourceServiceImpl implements EnergySourceService {

    private final EnergySourceMapper energySourceMapper;

    @Override
    public List<EnergySource> selectList(EnergySource query) {
        LambdaQueryWrapper<EnergySource> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(EnergySource::getName, query.getName());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(EnergySource::getType, query.getType());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EnergySource::getAreaId, query.getAreaId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(EnergySource::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(EnergySource::getCreateTime);
        return energySourceMapper.selectList(wrapper);
    }

    @Override
    public EnergySource selectById(Long id) {
        return energySourceMapper.selectById(id);
    }

    @Override
    public int insert(EnergySource source) {
        if (source.getName() == null || source.getName().isEmpty()) {
            throw new BusinessException("设备名称不能为空");
        }
        return energySourceMapper.insert(source);
    }

    @Override
    public int update(EnergySource source) {
        if (source.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return energySourceMapper.updateById(source);
    }

    @Override
    public int deleteById(Long id) {
        return energySourceMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getGenerationStats(Long sourceId, String period) {
        Map<String, Object> result = new HashMap<>();

        result.put("totalOutput", 125680.5);
        result.put("totalIncome", 87976.35);
        result.put("avgEfficiency", 18.5);
        result.put("unit", "kWh");

        List<Map<String, Object>> trend = new ArrayList<>();
        if ("day".equals(period)) {
            String[] hours = {"06:00", "09:00", "12:00", "15:00", "18:00"};
            double[] values = {50, 180, 350, 320, 120};
            for (int i = 0; i < hours.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("time", hours[i]);
                item.put("output", values[i]);
                trend.add(item);
            }
        } else {
            String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
            double[] values = {8500, 9200, 12500, 15800, 18200, 19500, 20100, 19800, 16500, 13200, 9800, 8200};
            for (int i = 0; i < months.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("time", months[i]);
                item.put("output", values[i]);
                trend.add(item);
            }
        }
        result.put("trend", trend);

        return result;
    }

    @Override
    public Map<String, Object> getRealtimeData(Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("currentPower", 285.6);
        result.put("todayOutput", 1850.3);
        result.put("todayIncome", 1295.21);
        result.put("efficiency", 19.2);
        result.put("status", "运行中");
        result.put("temperature", 42.5);
        result.put("irradiance", 680);
        return result;
    }
}
