package com.digitalpark.modules.investment.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentOpportunity;

import java.util.List;
import java.util.Map;

/**
 * 商机Service接口
 *
 * @author digitalpark
 */
public interface InvestmentOpportunityService {

    PageResult<InvestmentOpportunity> selectPage(InvestmentOpportunity query, int pageNum, int pageSize);

    InvestmentOpportunity selectById(Long id);

    List<InvestmentOpportunity> selectByCustomerId(Long customerId);

    List<Map<String, Object>> selectStageStatistics();

    int insert(InvestmentOpportunity opportunity);

    int update(InvestmentOpportunity opportunity);

    int advanceStage(Long id, String stage);

    int deleteById(Long id);
}
