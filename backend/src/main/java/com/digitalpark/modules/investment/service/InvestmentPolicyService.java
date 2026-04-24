package com.digitalpark.modules.investment.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentPolicy;

import java.util.List;

/**
 * 招商政策Service接口
 *
 * @author digitalpark
 */
public interface InvestmentPolicyService {

    PageResult<InvestmentPolicy> selectPage(InvestmentPolicy query, int pageNum, int pageSize);

    InvestmentPolicy selectById(Long id);

    List<InvestmentPolicy> selectEnabled();

    List<InvestmentPolicy> selectByType(String type);

    List<InvestmentPolicy> matchPolicies(String industry, String scale);

    int insert(InvestmentPolicy policy);

    int update(InvestmentPolicy policy);

    int enable(Long id);

    int disable(Long id);

    int deleteById(Long id);
}
