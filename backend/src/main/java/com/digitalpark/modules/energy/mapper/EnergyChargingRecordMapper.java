package com.digitalpark.modules.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.energy.entity.EnergyChargingRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充电记录Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnergyChargingRecordMapper extends BaseMapper<EnergyChargingRecord> {
}
