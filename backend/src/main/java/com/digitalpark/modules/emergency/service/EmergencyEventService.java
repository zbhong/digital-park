package com.digitalpark.modules.emergency.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyEvent;

import java.util.Map;

/**
 * 应急事件Service接口
 *
 * @author digitalpark
 */
public interface EmergencyEventService {

    PageResult<EmergencyEvent> selectPage(EmergencyEvent query, int pageNum, int pageSize);

    EmergencyEvent selectById(Long id);

    int insert(EmergencyEvent event);

    int update(EmergencyEvent event);

    int deleteById(Long id);

    int handle(Long id, String handleResult);

    Map<String, Object> selectStatistics();
}
