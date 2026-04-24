package com.digitalpark.modules.energy.service;

import com.digitalpark.modules.energy.entity.EnergyMeter;

import java.util.List;

/**
 * 能源计量表Service接口
 *
 * @author digitalpark
 */
public interface EnergyMeterService {

    /**
     * 查询计量表列表
     */
    List<EnergyMeter> selectList(EnergyMeter query);

    /**
     * 根据ID查询
     */
    EnergyMeter selectById(Long id);

    /**
     * 新增计量表
     */
    int insert(EnergyMeter meter);

    /**
     * 修改计量表
     */
    int update(EnergyMeter meter);

    /**
     * 删除计量表
     */
    int deleteById(Long id);
}
