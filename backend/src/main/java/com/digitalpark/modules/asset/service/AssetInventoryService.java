package com.digitalpark.modules.asset.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetInventory;

/**
 * 资产盘点Service接口
 *
 * @author digitalpark
 */
public interface AssetInventoryService {

    PageResult<AssetInventory> selectPage(AssetInventory query, int pageNum, int pageSize);

    AssetInventory selectById(Long id);

    /**
     * 创建盘点（同时生成盘点明细）
     */
    int createInventory(AssetInventory inventory, java.util.List<Long> assetIds);

    int update(AssetInventory inventory);

    int deleteById(Long id);

    /**
     * 完成盘点
     */
    int completeInventory(Long id);
}
