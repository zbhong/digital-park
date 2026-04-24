package com.digitalpark.modules.asset.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetMaintenance;

import java.util.List;

/**
 * 资产维保Service接口
 *
 * @author digitalpark
 */
public interface AssetMaintenanceService {

    PageResult<AssetMaintenance> selectPage(AssetMaintenance query, int pageNum, int pageSize);

    List<AssetMaintenance> selectListByAssetId(Long assetId);

    AssetMaintenance selectById(Long id);

    int insert(AssetMaintenance maintenance);

    int update(AssetMaintenance maintenance);

    int deleteById(Long id);

    /**
     * 获取即将到期的维保计划
     */
    List<AssetMaintenance> selectExpiringSoon(int days);
}
