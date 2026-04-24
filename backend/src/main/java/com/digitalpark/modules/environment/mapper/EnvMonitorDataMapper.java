package com.digitalpark.modules.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.environment.entity.EnvMonitorData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 环境监测数据Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EnvMonitorDataMapper extends BaseMapper<EnvMonitorData> {
}
