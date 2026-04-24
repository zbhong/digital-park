package com.digitalpark.modules.safety.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.safety.entity.SafetyMonitorPoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 安防监测点位Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface SafetyMonitorPointMapper extends BaseMapper<SafetyMonitorPoint> {
}
