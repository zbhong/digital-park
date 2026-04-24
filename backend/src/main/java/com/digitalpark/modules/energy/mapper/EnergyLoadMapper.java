package com.digitalpark.modules.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.energy.entity.EnergyLoad;
import org.apache.ibatis.annotations.Mapper;

/**
 * 负荷管理Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnergyLoadMapper extends BaseMapper<EnergyLoad> {
}
