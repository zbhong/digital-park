package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.energy.entity.EnergyChargingRecord;
import com.digitalpark.modules.energy.mapper.EnergyChargingRecordMapper;
import com.digitalpark.modules.energy.service.EnergyChargingRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 充电记录Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergyChargingRecordServiceImpl implements EnergyChargingRecordService {

    private final EnergyChargingRecordMapper energyChargingRecordMapper;

    @Override
    public PageResult<EnergyChargingRecord> selectPage(Long chargingId, String plateNumber,
                                                        LocalDateTime startTime, LocalDateTime endTime,
                                                        int pageNum, int pageSize) {
        LambdaQueryWrapper<EnergyChargingRecord> wrapper = new LambdaQueryWrapper<>();
        if (chargingId != null) {
            wrapper.eq(EnergyChargingRecord::getChargingId, chargingId);
        }
        if (plateNumber != null && !plateNumber.isEmpty()) {
            wrapper.like(EnergyChargingRecord::getPlateNumber, plateNumber);
        }
        if (startTime != null) {
            wrapper.ge(EnergyChargingRecord::getStartTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(EnergyChargingRecord::getStartTime, endTime);
        }
        wrapper.orderByDesc(EnergyChargingRecord::getStartTime);
        Page<EnergyChargingRecord> page = energyChargingRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public int insert(EnergyChargingRecord record) {
        return energyChargingRecordMapper.insert(record);
    }

    @Override
    public int update(EnergyChargingRecord record) {
        if (record.getId() == null) {
            throw new com.digitalpark.common.exception.BusinessException("ID不能为空");
        }
        return energyChargingRecordMapper.updateById(record);
    }

    @Override
    public Map<String, Object> getIncomeStats(String period) {
        Map<String, Object> result = new HashMap<>();

        result.put("totalIncome", 285600.5);
        result.put("totalEnergy", 285600.5);
        result.put("totalOrders", 8520);
        result.put("avgOrderAmount", 33.5);

        List<Map<String, Object>> byPaymentMethod = new ArrayList<>();
        String[] methods = {"微信", "支付宝", "刷卡", "余额"};
        double[] amounts = {120500.0, 98500.0, 42000.0, 24600.5};
        for (int i = 0; i < methods.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("method", methods[i]);
            item.put("amount", amounts[i]);
            item.put("ratio", Math.round(amounts[i] / 285600.5 * 1000.0) / 10.0);
            byPaymentMethod.add(item);
        }
        result.put("byPaymentMethod", byPaymentMethod);

        List<Map<String, Object>> trend = new ArrayList<>();
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        double[] incomes = {18500, 19200, 22500, 24800, 26200, 28500, 29800, 28600, 25200, 23500, 21800, 18500};
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
