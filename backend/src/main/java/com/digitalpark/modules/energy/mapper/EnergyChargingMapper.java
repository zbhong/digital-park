package com.digitalpark.modules.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.energy.entity.EnergyCharging;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充电桩Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnergyChargingMapper extends BaseMapper<EnergyCharging> {
}
