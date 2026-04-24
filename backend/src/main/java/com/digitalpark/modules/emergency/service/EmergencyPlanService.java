package com.digitalpark.modules.emergency.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyPlan;

/**
 * 应急预案Service接口
 *
 * @author digitalpark
 */
public interface EmergencyPlanService {

    PageResult<EmergencyPlan> selectPage(EmergencyPlan query, int pageNum, int pageSize);

    EmergencyPlan selectById(Long id);

    int insert(EmergencyPlan plan);

    int update(EmergencyPlan plan);

    int deleteById(Long id);

    int publish(Long id);

    int revoke(Long id);
}
