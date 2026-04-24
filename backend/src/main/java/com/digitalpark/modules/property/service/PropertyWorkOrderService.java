package com.digitalpark.modules.property.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyWorkOrder;

import java.util.List;
import java.util.Map;

/**
 * 物业工单Service接口
 *
 * @author digitalpark
 */
public interface PropertyWorkOrderService {

    PageResult<PropertyWorkOrder> selectPage(PropertyWorkOrder query, int pageNum, int pageSize);

    PropertyWorkOrder selectById(Long id);

    List<PropertyWorkOrder> selectByEnterpriseId(Long enterpriseId);

    List<Map<String, Object>> selectTypeStatistics();

    List<Map<String, Object>> selectStatusStatistics();

    List<Map<String, Object>> selectPriorityStatistics();

    int insert(PropertyWorkOrder order);

    int dispatch(Long id, Long handlerId);

    int handle(Long id, String handleResult);

    int complete(Long id);

    int evaluate(Long id, Integer satisfaction);

    int deleteById(Long id);
}
