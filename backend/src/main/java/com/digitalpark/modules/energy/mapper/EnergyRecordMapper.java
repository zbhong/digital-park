package com.digitalpark.modules.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.energy.entity.EnergyRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 能源计量记录Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnergyRecordMapper extends BaseMapper<EnergyRecord> {
}
