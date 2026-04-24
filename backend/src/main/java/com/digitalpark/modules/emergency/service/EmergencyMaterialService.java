package com.digitalpark.modules.emergency.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyMaterial;

import java.util.List;

/**
 * 应急物资Service接口
 *
 * @author digitalpark
 */
public interface EmergencyMaterialService {

    PageResult<EmergencyMaterial> selectPage(EmergencyMaterial query, int pageNum, int pageSize);

    List<EmergencyMaterial> selectList(EmergencyMaterial query);

    EmergencyMaterial selectById(Long id);

    int insert(EmergencyMaterial material);

    int update(EmergencyMaterial material);

    int deleteById(Long id);

    List<EmergencyMaterial> selectLowStock(int threshold);
}
