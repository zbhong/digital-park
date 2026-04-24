package com.digitalpark.modules.property.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyFee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 物业费Service接口
 *
 * @author digitalpark
 */
public interface PropertyFeeService {

    PageResult<PropertyFee> selectPage(PropertyFee query, int pageNum, int pageSize);

    PropertyFee selectById(Long id);

    List<PropertyFee> selectByEnterpriseId(Long enterpriseId);

    List<Map<String, Object>> selectFeeStatistics();

    List<Map<String, Object>> selectFeeTypeStatistics();

    BigDecimal selectTotalUnpaid(Long enterpriseId);

    int insert(PropertyFee fee);

    int pay(Long id);

    int batchPay(List<Long> ids);

    int deleteById(Long id);
}
