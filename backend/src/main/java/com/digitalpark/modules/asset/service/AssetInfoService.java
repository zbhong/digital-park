package com.digitalpark.modules.asset.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetInfo;

import java.util.Map;

/**
 * 资产信息Service接口
 *
 * @author digitalpark
 */
public interface AssetInfoService {

    PageResult<AssetInfo> selectPage(AssetInfo query, int pageNum, int pageSize);

    AssetInfo selectById(Long id);

    int insert(AssetInfo info);

    int update(AssetInfo info);

    int deleteById(Long id);

    /**
     * 按类型统计资产数量
     */
    Map<String, Object> selectStatisticsByType();

    /**
     * 按状态统计资产数量
     */
    Map<String, Object> selectStatisticsByStatus();
}
