package com.digitalpark.modules.asset.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetInventoryItem;

import java.util.List;

/**
 * 盘点明细Service接口
 *
 * @author digitalpark
 */
public interface AssetInventoryItemService {

    PageResult<AssetInventoryItem> selectPage(AssetInventoryItem query, int pageNum, int pageSize);

    List<AssetInventoryItem> selectListByInventoryId(Long inventoryId);

    AssetInventoryItem selectById(Long id);

    int insert(AssetInventoryItem item);

    int update(AssetInventoryItem item);

    int deleteById(Long id);

    /**
     * 批量保存盘点明细
     */
    int batchInsert(List<AssetInventoryItem> items);
}
