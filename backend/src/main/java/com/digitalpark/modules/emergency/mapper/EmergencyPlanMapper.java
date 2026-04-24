package com.digitalpark.modules.emergency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.emergency.entity.EmergencyPlan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应急预案Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EmergencyPlanMapper extends BaseMapper<EmergencyPlan> {
}
