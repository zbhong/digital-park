package com.digitalpark.modules.investment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentAsset;
import com.digitalpark.modules.investment.mapper.InvestmentAssetMapper;
import com.digitalpark.modules.investment.service.InvestmentAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 招商房源Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class InvestmentAssetServiceImpl implements InvestmentAssetService {

    private final InvestmentAssetMapper assetMapper;

    @Override
    public PageResult<InvestmentAsset> selectPage(InvestmentAsset query, int pageNum, int pageSize) {
        LambdaQueryWrapper<InvestmentAsset> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(InvestmentAsset::getCreateTime);
        Page<InvestmentAsset> page = assetMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public InvestmentAsset selectById(Long id) {
        return assetMapper.selectById(id);
    }

    @Override
    public List<InvestmentAsset> selectByBuildingId(Long buildingId) {
        LambdaQueryWrapper<InvestmentAsset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentAsset::getBuildingId, buildingId);
        return assetMapper.selectList(wrapper);
    }

    @Override
    public List<InvestmentAsset> selectAvailable() {
        LambdaQueryWrapper<InvestmentAsset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentAsset::getStatus, "可租");
        return assetMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectRentControlData() {
        List<InvestmentAsset> list = assetMapper.selectList(null);
        Map<String, Object> data = new HashMap<>();
        int total = list.size();
        int available = 0;
        int rented = 0;
        int reserved = 0;
        int unavailable = 0;
        BigDecimal totalArea = BigDecimal.ZERO;
        BigDecimal rentedArea = BigDecimal.ZERO;
        for (InvestmentAsset asset : list) {
            BigDecimal area = asset.getArea() != null ? asset.getArea() : BigDecimal.ZERO;
            totalArea = totalArea.add(area);
            String status = asset.getStatus();
            if ("可租".equals(status)) {
                available++;
            } else if ("已租".equals(status)) {
                rented++;
                rentedArea = rentedArea.add(area);
            } else if ("预留".equals(status)) {
                reserved++;
            } else if ("不可租".equals(status)) {
                unavailable++;
            }
        }
        data.put("total", total);
        data.put("available", available);
        data.put("rented", rented);
        data.put("reserved", reserved);
        data.put("unavailable", unavailable);
        data.put("totalArea", totalArea);
        data.put("rentedArea", rentedArea);
        data.put("rentRate", totalArea.compareTo(BigDecimal.ZERO) > 0 ?
                rentedArea.divide(totalArea, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100")) : BigDecimal.ZERO);
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(data);
        return result;
    }

    @Override
    public BigDecimal selectTotalAvailableArea() {
        LambdaQueryWrapper<InvestmentAsset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentAsset::getStatus, "可租");
        List<InvestmentAsset> list = assetMapper.selectList(wrapper);
        BigDecimal total = BigDecimal.ZERO;
        for (InvestmentAsset asset : list) {
            total = total.add(asset.getArea() != null ? asset.getArea() : BigDecimal.ZERO);
        }
        return total;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(InvestmentAsset asset) {
        if (!StringUtils.hasText(asset.getStatus())) {
            asset.setStatus("可租");
        }
        return assetMapper.insert(asset);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(InvestmentAsset asset) {
        return assetMapper.updateById(asset);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return assetMapper.deleteById(id);
    }

    private LambdaQueryWrapper<InvestmentAsset> buildQueryWrapper(InvestmentAsset query) {
        LambdaQueryWrapper<InvestmentAsset> wrapper = new LambdaQueryWrapper<>();
        if (query.getBuildingId() != null) {
            wrapper.eq(InvestmentAsset::getBuildingId, query.getBuildingId());
        }
        if (StringUtils.hasText(query.getFloor())) {
            wrapper.eq(InvestmentAsset::getFloor, query.getFloor());
        }
        if (StringUtils.hasText(query.getRoomNumber())) {
            wrapper.like(InvestmentAsset::getRoomNumber, query.getRoomNumber());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(InvestmentAsset::getStatus, query.getStatus());
        }
        if (query.getEnterpriseId() != null) {
            wrapper.eq(InvestmentAsset::getEnterpriseId, query.getEnterpriseId());
        }
        return wrapper;
    }
}
