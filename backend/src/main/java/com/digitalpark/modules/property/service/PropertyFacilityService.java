package com.digitalpark.modules.property.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyFacility;

import java.util.List;

/**
 * 设施运维Service接口
 *
 * @author digitalpark
 */
public interface PropertyFacilityService {

    PageResult<PropertyFacility> selectPage(PropertyFacility query, int pageNum, int pageSize);

    PropertyFacility selectById(Long id);

    List<PropertyFacility> selectByType(String type);

    List<PropertyFacility> selectByAreaId(Long areaId);

    List<PropertyFacility> selectCheckReminders();

    int insert(PropertyFacility facility);

    int update(PropertyFacility facility);

    int check(Long id);

    int deleteById(Long id);
}
