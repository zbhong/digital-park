package com.digitalpark.modules.property.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyVisitor;

/**
 * 访客Service接口
 *
 * @author digitalpark
 */
public interface PropertyVisitorService {

    PageResult<PropertyVisitor> selectPage(PropertyVisitor query, int pageNum, int pageSize);

    PropertyVisitor selectById(Long id);

    int insert(PropertyVisitor visitor);

    int approve(Long id);

    int reject(Long id);

    int leave(Long id);

    int deleteById(Long id);
}
