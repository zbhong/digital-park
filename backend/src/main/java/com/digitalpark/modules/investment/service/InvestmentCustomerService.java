package com.digitalpark.modules.investment.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentCustomer;
import com.digitalpark.modules.investment.entity.InvestmentFollow;

import java.util.List;
import java.util.Map;

/**
 * 客户Service接口
 *
 * @author digitalpark
 */
public interface InvestmentCustomerService {

    PageResult<InvestmentCustomer> selectPage(InvestmentCustomer query, int pageNum, int pageSize);

    InvestmentCustomer selectById(Long id);

    List<InvestmentCustomer> selectPublicPool();

    List<InvestmentCustomer> selectByFollowUserId(Long followUserId);

    List<Map<String, Object>> selectStatusStatistics();

    List<Map<String, Object>> selectSourceStatistics();

    int insert(InvestmentCustomer customer);

    int update(InvestmentCustomer customer);

    int assign(Long id, Long followUserId);

    int releaseToPublicPool(Long id);

    List<InvestmentFollow> selectFollowRecords(Long customerId);

    int addFollowRecord(InvestmentFollow follow);

    /**
     * 更新跟进状态
     *
     * @param id     客户ID
     * @param status 跟进状态
     */
    int updateFollowStatus(Long id, String status);

    int deleteById(Long id);
}
