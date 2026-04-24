package com.digitalpark.modules.emergency.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyDrill;

/**
 * 应急演练Service接口
 *
 * @author digitalpark
 */
public interface EmergencyDrillService {

    PageResult<EmergencyDrill> selectPage(EmergencyDrill query, int pageNum, int pageSize);

    EmergencyDrill selectById(Long id);

    int insert(EmergencyDrill drill);

    int update(EmergencyDrill drill);

    int deleteById(Long id);
}
