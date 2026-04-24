package com.digitalpark.modules.safety.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyPatrolRoute;

import java.util.List;

/**
 * 巡检路线Service接口
 *
 * @author digitalpark
 */
public interface SafetyPatrolRouteService {

    PageResult<SafetyPatrolRoute> selectPage(SafetyPatrolRoute query, int pageNum, int pageSize);

    List<SafetyPatrolRoute> selectList(SafetyPatrolRoute query);

    SafetyPatrolRoute selectById(Long id);

    int insert(SafetyPatrolRoute route);

    int update(SafetyPatrolRoute route);

    int deleteById(Long id);
}
