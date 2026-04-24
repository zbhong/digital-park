package com.digitalpark.modules.enterprise.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.enterprise.entity.EnterpriseInfo;

import java.util.List;
import java.util.Map;

/**
 * 企业信息Service接口
 *
 * @author digitalpark
 */
public interface EnterpriseInfoService {

    PageResult<EnterpriseInfo> selectPage(EnterpriseInfo query, int pageNum, int pageSize);

    EnterpriseInfo selectById(Long id);

    List<EnterpriseInfo> selectByAreaId(Long areaId);

    List<EnterpriseInfo> selectByBuildingId(Long buildingId);

    List<Map<String, Object>> selectIndustryStatistics();

    List<Map<String, Object>> selectStatusStatistics();

    int insert(EnterpriseInfo info);

    int update(EnterpriseInfo info);

    int deleteById(Long id);
}
