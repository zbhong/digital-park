package com.digitalpark.modules.investment.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentContract;

import java.util.List;
import java.util.Map;

/**
 * 合同Service接口
 *
 * @author digitalpark
 */
public interface InvestmentContractService {

    PageResult<InvestmentContract> selectPage(InvestmentContract query, int pageNum, int pageSize);

    InvestmentContract selectById(Long id);

    InvestmentContract selectByContractNo(String contractNo);

    List<Map<String, Object>> selectStatusStatistics();

    int insert(InvestmentContract contract);

    int update(InvestmentContract contract);

    int submitForApproval(Long id);

    int approve(Long id);

    int reject(Long id);

    int terminate(Long id);

    int deleteById(Long id);
}
