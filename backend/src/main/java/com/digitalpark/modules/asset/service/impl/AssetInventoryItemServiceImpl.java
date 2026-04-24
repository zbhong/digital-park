package com.digitalpark.modules.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetInventoryItem;
import com.digitalpark.modules.asset.mapper.AssetInventoryItemMapper;
import com.digitalpark.modules.asset.service.AssetInventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 盘点明细Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class AssetInventoryItemServiceImpl implements AssetInventoryItemService {

    private final AssetInventoryItemMapper assetInventoryItemMapper;

    @Override
    public PageResult<AssetInventoryItem> selectPage(AssetInventoryItem query, int pageNum, int pageSize) {
        LambdaQueryWrapper<AssetInventoryItem> wrapper = buildQueryWrapper(query);
        wrapper.orderByAsc(AssetInventoryItem::getId);
        Page<AssetInventoryItem> page = assetInventoryItemMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<AssetInventoryItem> selectListByInventoryId(Long inventoryId) {
        return assetInventoryItemMapper.selectList(
                new LambdaQueryWrapper<AssetInventoryItem>()
                        .eq(AssetInventoryItem::getInventoryId, inventoryId)
                        .orderByAsc(AssetInventoryItem::getId));
    }

    @Override
    public AssetInventoryItem selectById(Long id) {
        return assetInventoryItemMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(AssetInventoryItem item) {
        return assetInventoryItemMapper.insert(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AssetInventoryItem item) {
        return assetInventoryItemMapper.updateById(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return assetInventoryItemMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<AssetInventoryItem> items) {
        int count = 0;
        for (AssetInventoryItem item : items) {
            count += assetInventoryItemMapper.insert(item);
        }
        return count;
    }

    private LambdaQueryWrapper<AssetInventoryItem> buildQueryWrapper(AssetInventoryItem query) {
        LambdaQueryWrapper<AssetInventoryItem> wrapper = new LambdaQueryWrapper<>();
        if (query.getInventoryId() != null) {
            wrapper.eq(AssetInventoryItem::getInventoryId, query.getInventoryId());
        }
        if (query.getAssetId() != null) {
            wrapper.eq(AssetInventoryItem::getAssetId, query.getAssetId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(AssetInventoryItem::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
