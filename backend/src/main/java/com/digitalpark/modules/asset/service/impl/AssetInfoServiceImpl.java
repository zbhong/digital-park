package com.digitalpark.modules.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetInfo;
import com.digitalpark.modules.asset.mapper.AssetInfoMapper;
import com.digitalpark.modules.asset.service.AssetInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产信息Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class AssetInfoServiceImpl implements AssetInfoService {

    private final AssetInfoMapper assetInfoMapper;

    @Override
    public PageResult<AssetInfo> selectPage(AssetInfo query, int pageNum, int pageSize) {
        LambdaQueryWrapper<AssetInfo> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(AssetInfo::getCreateTime);
        Page<AssetInfo> page = assetInfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public AssetInfo selectById(Long id) {
        return assetInfoMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(AssetInfo info) {
        if (info.getStatus() == null) {
            info.setStatus("在用");
        }
        return assetInfoMapper.insert(info);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AssetInfo info) {
        return assetInfoMapper.updateById(info);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return assetInfoMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> selectStatisticsByType() {
        List<AssetInfo> allAssets = assetInfoMapper.selectList(null);
        Map<String, Long> typeStats = allAssets.stream()
                .filter(a -> a.getType() != null)
                .collect(Collectors.groupingBy(AssetInfo::getType, Collectors.counting()));

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", allAssets.size());
        result.put("typeStatistics", typeStats);
        return result;
    }

    @Override
    public Map<String, Object> selectStatisticsByStatus() {
        List<AssetInfo> allAssets = assetInfoMapper.selectList(null);
        Map<String, Long> statusStats = allAssets.stream()
                .filter(a -> a.getStatus() != null)
                .collect(Collectors.groupingBy(AssetInfo::getStatus, Collectors.counting()));

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", allAssets.size());
        result.put("statusStatistics", statusStats);
        return result;
    }

    private LambdaQueryWrapper<AssetInfo> buildQueryWrapper(AssetInfo query) {
        LambdaQueryWrapper<AssetInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(AssetInfo::getName, query.getName());
        }
        if (StringUtils.hasText(query.getAssetCode())) {
            wrapper.eq(AssetInfo::getAssetCode, query.getAssetCode());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(AssetInfo::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(AssetInfo::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getBrand())) {
            wrapper.like(AssetInfo::getBrand, query.getBrand());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(AssetInfo::getCategoryId, query.getCategoryId());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(AssetInfo::getAreaId, query.getAreaId());
        }
        if (query.getBuildingId() != null) {
            wrapper.eq(AssetInfo::getBuildingId, query.getBuildingId());
        }
        return wrapper;
    }
}
