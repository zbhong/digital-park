package com.digitalpark.modules.property.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyVehicle;

/**
 * 车辆Service接口
 *
 * @author digitalpark
 */
public interface PropertyVehicleService {

    PageResult<PropertyVehicle> selectPage(PropertyVehicle query, int pageNum, int pageSize);

    PropertyVehicle selectById(Long id);

    PropertyVehicle selectByPlateNumber(String plateNumber);

    int insert(PropertyVehicle vehicle);

    int update(PropertyVehicle vehicle);

    int disable(Long id);

    int enable(Long id);

    int deleteById(Long id);
}
