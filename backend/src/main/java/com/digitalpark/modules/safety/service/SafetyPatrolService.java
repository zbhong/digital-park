package com.digitalpark.modules.safety.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyPatrol;

/**
 * 安全巡检Service接口
 *
 * @author digitalpark
 */
public interface SafetyPatrolService {

    PageResult<SafetyPatrol> selectPage(SafetyPatrol query, int pageNum, int pageSize);

    SafetyPatrol selectById(Long id);

    int insert(SafetyPatrol patrol);

    int update(SafetyPatrol patrol);

    int deleteById(Long id);
}
