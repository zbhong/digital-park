package com.digitalpark.modules.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.energy.entity.EnergySource;
import org.apache.ibatis.annotations.Mapper;

/**
 * 源侧设备Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnergySourceMapper extends BaseMapper<EnergySource> {
}
