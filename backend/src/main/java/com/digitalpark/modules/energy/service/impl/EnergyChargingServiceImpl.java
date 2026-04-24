package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.energy.dto.EnergyChargingDTO;
import com.digitalpark.modules.energy.entity.EnergyCharging;
import com.digitalpark.modules.energy.mapper.EnergyChargingMapper;
import com.digitalpark.modules.energy.service.EnergyChargingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 充电桩Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergyChargingServiceImpl implements EnergyChargingService {

    private final EnergyChargingMapper energyChargingMapper;

    @Override
    public List<EnergyCharging> selectList(EnergyCharging query) {
        LambdaQueryWrapper<EnergyCharging> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(EnergyCharging::getName, query.getName());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(EnergyCharging::getType, query.getType());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(EnergyCharging::getStatus, query.getStatus());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EnergyCharging::getAreaId, query.getAreaId());
        }
        wrapper.orderByDesc(EnergyCharging::getCreateTime);
        return energyChargingMapper.selectList(wrapper);
    }

    @Override
    public EnergyCharging selectById(Long id) {
        return energyChargingMapper.selectById(id);
    }

    @Override
    public int insert(EnergyCharging charging) {
        if (charging.getName() == null || charging.getName().isEmpty()) {
            throw new BusinessException("充电桩名称不能为空");
        }
        return energyChargingMapper.insert(charging);
    }

    @Override
    public int update(EnergyCharging charging) {
        if (charging.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return energyChargingMapper.updateById(charging);
    }

    @Override
    public int deleteById(Long id) {
        return energyChargingMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getStatusMonitor() {
        Map<String, Object> result = new HashMap<>();

        result.put("total", 48);
        result.put("idle", 18);
        result.put("charging", 22);
        result.put("fault", 3);
        result.put("offline", 5);

        result.put("idleRate", 37.5);
        result.put("utilizationRate", 45.8);
        result.put("faultRate", 6.3);

        result.put("totalPower", 2880.0);
        result.put("currentPower", 1580.5);

        List<Map<String, Object>> byType = new ArrayList<>();
        Map<String, Object> dc = new HashMap<>();
        dc.put("type", "直流快充");
        dc.put("total", 20);
        dc.put("charging", 14);
        dc.put("idle", 4);
        dc.put("fault", 1);
        dc.put("offline", 1);
        byType.add(dc);

        Map<String, Object> ac = new HashMap<>();
        ac.put("type", "交流慢充");
        ac.put("total", 24);
        ac.put("charging", 6);
        ac.put("idle", 12);
        ac.put("fault", 2);
        ac.put("offline", 4);
        byType.add(ac);

        Map<String, Object> ordered = new HashMap<>();
        ordered.put("type", "有序充电");
        ordered.put("total", 4);
        ordered.put("charging", 2);
        ordered.put("idle", 2);
        ordered.put("fault", 0);
        ordered.put("offline", 0);
        byType.add(ordered);

        result.put("byType", byType);

        return result;
    }

    @Override
    public Map<String, Object> getOperationStats(String period) {
        Map<String, Object> result = new HashMap<>();

        result.put("totalEnergy", 285600.5);
        result.put("totalIncome", 285600.5);
        result.put("totalOrders", 8520);
        result.put("avgDuration", 68);
        result.put("avgEnergy", 33.5);
        result.put("peakEnergy", 152000.0);
        result.put("flatEnergy", 98000.0);
        result.put("valleyEnergy", 35600.5);

        List<Map<String, Object>> trend = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        double[] energies = {8500, 9200, 8800, 9500, 10200, 12500, 11800};
        double[] incomes = {8500, 9200, 8800, 9500, 10200, 12500, 11800};
        for (int i = 0; i < days.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("time", days[i]);
            item.put("energy", energies[i]);
            item.put("income", incomes[i]);
            trend.add(item);
        }
        result.put("trend", trend);

        return result;
    }

    @Override
    public int updateSchedule(EnergyChargingDTO dto) {
        if (dto.getChargingIds() == null || dto.getChargingIds().isEmpty()) {
            throw new BusinessException("充电桩ID列表不能为空");
        }
        // TODO: 实现调度配置持久化
        return 1;
    }
}
