package com.digitalpark.modules.emergency.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.emergency.entity.EmergencyEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应急事件Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface EmergencyEventMapper extends BaseMapper<EmergencyEvent> {
}
