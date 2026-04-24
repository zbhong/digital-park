package com.digitalpark.modules.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.environment.entity.EnvMonitorPoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 环境监测点位Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnvMonitorPointMapper extends BaseMapper<EnvMonitorPoint> {
}
