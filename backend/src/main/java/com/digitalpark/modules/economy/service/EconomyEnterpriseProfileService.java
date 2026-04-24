package com.digitalpark.modules.economy.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.economy.entity.EconomyEnterpriseProfile;

import java.util.List;
import java.util.Map;

/**
 * 企业画像Service接口
 *
 * @author digitalpark
 */
public interface EconomyEnterpriseProfileService {

    PageResult<EconomyEnterpriseProfile> selectPage(EconomyEnterpriseProfile query, int pageNum, int pageSize);

    EconomyEnterpriseProfile selectById(Long id);

    EconomyEnterpriseProfile selectByEnterpriseId(Long enterpriseId);

    List<EconomyEnterpriseProfile> selectByIndustry(String industry);

    List<Map<String, Object>> selectIndustryDistribution();

    List<Map<String, Object>> selectScaleDistribution();

    int insert(EconomyEnterpriseProfile profile);

    int update(EconomyEnterpriseProfile profile);

    int deleteById(Long id);
}
