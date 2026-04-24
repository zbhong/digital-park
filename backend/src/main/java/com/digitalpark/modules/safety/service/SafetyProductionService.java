package com.digitalpark.modules.safety.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyProduction;

/**
 * 生产安全Service接口
 *
 * @author digitalpark
 */
public interface SafetyProductionService {

    PageResult<SafetyProduction> selectPage(SafetyProduction query, int pageNum, int pageSize);

    SafetyProduction selectById(Long id);

    int insert(SafetyProduction production);

    int update(SafetyProduction production);

    int deleteById(Long id);
}
