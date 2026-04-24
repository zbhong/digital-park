package com.digitalpark.modules.safety.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyEvent;

import java.util.Map;

/**
 * 安全事件Service接口
 *
 * @author digitalpark
 */
public interface SafetyEventService {

    PageResult<SafetyEvent> selectPage(SafetyEvent query, int pageNum, int pageSize);

    SafetyEvent selectById(Long id);

    int insert(SafetyEvent event);

    int update(SafetyEvent event);

    int deleteById(Long id);

    int handle(Long id, String handleResult);

    Map<String, Object> selectStatistics();
}
