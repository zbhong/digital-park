package com.digitalpark.modules.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.energy.entity.EnergyMeter;
import org.apache.ibatis.annotations.Mapper;

/**
 * 能源计量表Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnergyMeterMapper extends BaseMapper<EnergyMeter> {
}
