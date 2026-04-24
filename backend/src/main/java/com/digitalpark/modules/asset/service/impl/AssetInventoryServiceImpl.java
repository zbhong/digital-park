package com.digitalpark.modules.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetInventory;
import com.digitalpark.modules.asset.entity.AssetInventoryItem;
import com.digitalpark.modules.asset.mapper.AssetInventoryMapper;
import com.digitalpark.modules.asset.service.AssetInventoryItemService;
import com.digitalpark.modules.asset.service.AssetInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产盘点Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class AssetInventoryServiceImpl implements AssetInventoryService {

    private final AssetInventoryMapper assetInventoryMapper;
    private final AssetInventoryItemService assetInventoryItemService;

    @Override
    public PageResult<AssetInventory> selectPage(AssetInventory query, int pageNum, int pageSize) {
        LambdaQueryWrapper<AssetInventory> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(AssetInventory::getCreateTime);
        Page<AssetInventory> page = assetInventoryMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public AssetInventory selectById(Long id) {
        return assetInventoryMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createInventory(AssetInventory inventory, List<Long> assetIds) {
        inventory.setStatus("计划中");
        int rows = assetInventoryMapper.insert(inventory);

        // 生成盘点明细
        if (assetIds != null && !assetIds.isEmpty()) {
            List<AssetInventoryItem> items = assetIds.stream().map(assetId -> {
                AssetInventoryItem item = new AssetInventoryItem();
                item.setInventoryId(inventory.getId());
                item.setAssetId(assetId);
                item.setActualQuantity(1);
                item.setStatus("正常");
                return item;
            }).collect(Collectors.toList());
            assetInventoryItemService.batchInsert(items);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AssetInventory inventory) {
        return assetInventoryMapper.updateById(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        AssetInventory inventory = assetInventoryMapper.selectById(id);
        if (inventory != null && "进行中".equals(inventory.getStatus())) {
            throw new BusinessException("盘点进行中，无法删除");
        }
        // 删除盘点明细
        assetInventoryItemService.selectListByInventoryId(id).forEach(item ->
                assetInventoryItemService.deleteById(item.getId()));
        return assetInventoryMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int completeInventory(Long id) {
        AssetInventory inventory = assetInventoryMapper.selectById(id);
        if (inventory == null) {
            throw new BusinessException("盘点记录不存在");
        }
        if ("已完成".equals(inventory.getStatus())) {
            throw new BusinessException("该盘点已完成");
        }

        // 统计盘点结果
        List<AssetInventoryItem> items = assetInventoryItemService.selectListByInventoryId(id);
        Map<String, Long> statusCount = items.stream()
                .filter(i -> i.getStatus() != null)
                .collect(Collectors.groupingBy(AssetInventoryItem::getStatus, Collectors.counting()));

        StringBuilder result = new StringBuilder();
        result.append("共盘点").append(items.size()).append("项，");
        result.append("正常").append(statusCount.getOrDefault("正常", 0L)).append("项，");
        result.append("盘盈").append(statusCount.getOrDefault("盘盈", 0L)).append("项，");
        result.append("盘亏").append(statusCount.getOrDefault("盘亏", 0L)).append("项，");
        result.append("异常").append(statusCount.getOrDefault("异常", 0L)).append("项");

        AssetInventory update = new AssetInventory();
        update.setId(id);
        update.setStatus("已完成");
        update.setResult(result.toString());
        update.setEndDate(LocalDate.now());
        return assetInventoryMapper.updateById(update);
    }

    private LambdaQueryWrapper<AssetInventory> buildQueryWrapper(AssetInventory query) {
        LambdaQueryWrapper<AssetInventory> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(AssetInventory::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(AssetInventory::getStatus, query.getStatus());
        }
        if (query.getCreatorId() != null) {
            wrapper.eq(AssetInventory::getCreatorId, query.getCreatorId());
        }
        return wrapper;
    }
}
