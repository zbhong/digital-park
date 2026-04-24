package com.digitalpark.modules.safety.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyFireEquipment;

import java.util.List;

/**
 * 消防设备Service接口
 *
 * @author digitalpark
 */
public interface SafetyFireEquipmentService {

    PageResult<SafetyFireEquipment> selectPage(SafetyFireEquipment query, int pageNum, int pageSize);

    List<SafetyFireEquipment> selectList(SafetyFireEquipment query);

    SafetyFireEquipment selectById(Long id);

    int insert(SafetyFireEquipment equipment);

    int update(SafetyFireEquipment equipment);

    int deleteById(Long id);

    List<SafetyFireEquipment> selectExpiring(int days);
}
