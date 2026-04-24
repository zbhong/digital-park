package com.digitalpark.modules.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetMaintenance;
import com.digitalpark.modules.asset.mapper.AssetMaintenanceMapper;
import com.digitalpark.modules.asset.service.AssetMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 资产维保Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class AssetMaintenanceServiceImpl implements AssetMaintenanceService {

    private final AssetMaintenanceMapper assetMaintenanceMapper;

    @Override
    public PageResult<AssetMaintenance> selectPage(AssetMaintenance query, int pageNum, int pageSize) {
        LambdaQueryWrapper<AssetMaintenance> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(AssetMaintenance::getCreateTime);
        Page<AssetMaintenance> page = assetMaintenanceMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<AssetMaintenance> selectListByAssetId(Long assetId) {
        return assetMaintenanceMapper.selectList(
                new LambdaQueryWrapper<AssetMaintenance>()
                        .eq(AssetMaintenance::getAssetId, assetId)
                        .orderByDesc(AssetMaintenance::getCreateTime));
    }

    @Override
    public AssetMaintenance selectById(Long id) {
        return assetMaintenanceMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(AssetMaintenance maintenance) {
        return assetMaintenanceMapper.insert(maintenance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AssetMaintenance maintenance) {
        return assetMaintenanceMapper.updateById(maintenance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return assetMaintenanceMapper.deleteById(id);
    }

    @Override
    public List<AssetMaintenance> selectExpiringSoon(int days) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);
        return assetMaintenanceMapper.selectList(
                new LambdaQueryWrapper<AssetMaintenance>()
                        .ge(AssetMaintenance::getPlanDate, today)
                        .le(AssetMaintenance::getPlanDate, endDate)
                        .isNull(AssetMaintenance::getActualDate)
                        .orderByAsc(AssetMaintenance::getPlanDate));
    }

    private LambdaQueryWrapper<AssetMaintenance> buildQueryWrapper(AssetMaintenance query) {
        LambdaQueryWrapper<AssetMaintenance> wrapper = new LambdaQueryWrapper<>();
        if (query.getAssetId() != null) {
            wrapper.eq(AssetMaintenance::getAssetId, query.getAssetId());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(AssetMaintenance::getType, query.getType());
        }
        if (StringUtils.hasText(query.getExecutor())) {
            wrapper.like(AssetMaintenance::getExecutor, query.getExecutor());
        }
        return wrapper;
    }
}
