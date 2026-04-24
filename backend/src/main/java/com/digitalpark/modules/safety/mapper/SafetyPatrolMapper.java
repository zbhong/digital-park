package com.digitalpark.modules.safety.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.safety.entity.SafetyPatrol;
import org.apache.ibatis.annotations.Mapper;

/**
 * 安全巡检Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface SafetyPatrolMapper extends BaseMapper<SafetyPatrol> {
}
