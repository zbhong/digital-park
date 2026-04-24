package com.digitalpark.modules.safety.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyMonitorPoint;

import java.util.List;

/**
 * 安防监测点位Service接口
 *
 * @author digitalpark
 */
public interface SafetyMonitorPointService {

    PageResult<SafetyMonitorPoint> selectPage(SafetyMonitorPoint query, int pageNum, int pageSize);

    List<SafetyMonitorPoint> selectList(SafetyMonitorPoint query);

    SafetyMonitorPoint selectById(Long id);

    int insert(SafetyMonitorPoint point);

    int update(SafetyMonitorPoint point);

    int deleteById(Long id);
}
