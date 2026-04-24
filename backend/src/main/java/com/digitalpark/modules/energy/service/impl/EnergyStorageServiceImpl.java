package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.energy.dto.EnergyStorageDTO;
import com.digitalpark.modules.energy.entity.EnergyStorage;
import com.digitalpark.modules.energy.mapper.EnergyStorageMapper;
import com.digitalpark.modules.energy.service.EnergyStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 储能系统Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergyStorageServiceImpl implements EnergyStorageService {

    private final EnergyStorageMapper energyStorageMapper;

    @Override
    public List<EnergyStorage> selectList(EnergyStorage query) {
        LambdaQueryWrapper<EnergyStorage> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(EnergyStorage::getName, query.getName());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(EnergyStorage::getType, query.getType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(EnergyStorage::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(EnergyStorage::getCreateTime);
        return energyStorageMapper.selectList(wrapper);
    }

    @Override
    public EnergyStorage selectById(Long id) {
        return energyStorageMapper.selectById(id);
    }

    @Override
    public int insert(EnergyStorage storage) {
        if (storage.getName() == null || storage.getName().isEmpty()) {
            throw new BusinessException("储能名称不能为空");
        }
        return energyStorageMapper.insert(storage);
    }

    @Override
    public int update(EnergyStorage storage) {
        if (storage.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return energyStorageMapper.updateById(storage);
    }

    @Override
    public int deleteById(Long id) {
        return energyStorageMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getSocMonitor(Long storageId) {
        Map<String, Object> result = new HashMap<>();

        result.put("soc", 65.8);
        result.put("soh", 92.3);
        result.put("voltage", 768.5);
        result.put("current", 125.3);
        result.put("temperature", 28.5);
        result.put("chargePower", 0);
        result.put("dischargePower", 250.0);
        result.put("status", "放电中");

        List<Map<String, Object>> socTrend = new ArrayList<>();
        String[] times = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00"};
        double[] socs = {35.2, 82.5, 80.1, 72.3, 68.5, 65.8};
        for (int i = 0; i < times.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("time", times[i]);
            item.put("soc", socs[i]);
            socTrend.add(item);
        }
        result.put("socTrend", socTrend);

        result.put("estimatedChargeTime", 0);
        result.put("estimatedDischargeTime", 2.5);

        return result;
    }

    @Override
    public Map<String, Object> getRealtimeStatus(Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("soc", 65.8);
        result.put("soh", 92.3);
        result.put("chargePower", 0);
        result.put("dischargePower", 250.0);
        result.put("voltage", 768.5);
        result.put("current", 125.3);
        result.put("temperature", 28.5);
        result.put("cycleCount", 1250);
        result.put("status", "放电中");
        result.put("dailyIncome", 1580.5);
        return result;
    }

    @Override
    public int updateStrategy(EnergyStorageDTO dto) {
        if (dto.getStorageId() == null) {
            throw new BusinessException("储能ID不能为空");
        }
        if (dto.getStrategyType() == null || dto.getStrategyType().isEmpty()) {
            throw new BusinessException("策略类型不能为空");
        }
        // TODO: 实现策略配置持久化
        return 1;
    }

    @Override
    public Map<String, Object> getIncomeStats(Long storageId, String period) {
        Map<String, Object> result = new HashMap<>();

        result.put("totalIncome", 125680.5);
        result.put("totalCharge", 98500.0);
        result.put("totalDischarge", 89200.0);
        result.put("peakCharge", 35600.0);
        result.put("valleyDischarge", 52800.0);
        result.put("arbitrageIncome", 45230.0);

        List<Map<String, Object>> trend = new ArrayList<>();
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        double[] incomes = {8500, 9200, 10500, 11800, 12200, 12500, 12800, 12600, 11200, 10500, 9800, 8500};
        for (int i = 0; i < months.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("time", months[i]);
            item.put("income", incomes[i]);
            trend.add(item);
        }
        result.put("trend", trend);

        return result;
    }
}
