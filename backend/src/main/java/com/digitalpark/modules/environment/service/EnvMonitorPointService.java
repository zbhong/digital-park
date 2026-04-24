package com.digitalpark.modules.environment.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.environment.entity.EnvMonitorPoint;

import java.util.List;

/**
 * 环境监测点位Service接口
 *
 * @author digitalpark
 */
public interface EnvMonitorPointService {

    PageResult<EnvMonitorPoint> selectPage(EnvMonitorPoint query, int pageNum, int pageSize);

    List<EnvMonitorPoint> selectList(EnvMonitorPoint query);

    EnvMonitorPoint selectById(Long id);

    int insert(EnvMonitorPoint point);

    int update(EnvMonitorPoint point);

    int deleteById(Long id);
}
