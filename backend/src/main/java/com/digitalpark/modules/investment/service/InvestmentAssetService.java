package com.digitalpark.modules.investment.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentAsset;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 招商房源Service接口
 *
 * @author digitalpark
 */
public interface InvestmentAssetService {

    PageResult<InvestmentAsset> selectPage(InvestmentAsset query, int pageNum, int pageSize);

    InvestmentAsset selectById(Long id);

    List<InvestmentAsset> selectByBuildingId(Long buildingId);

    List<InvestmentAsset> selectAvailable();

    List<Map<String, Object>> selectRentControlData();

    BigDecimal selectTotalAvailableArea();

    int insert(InvestmentAsset asset);

    int update(InvestmentAsset asset);

    int deleteById(Long id);
}
