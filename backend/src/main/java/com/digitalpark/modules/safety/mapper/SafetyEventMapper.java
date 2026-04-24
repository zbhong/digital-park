package com.digitalpark.modules.safety.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.safety.entity.SafetyEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 安全事件Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface SafetyEventMapper extends BaseMapper<SafetyEvent> {
}
