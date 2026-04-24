package com.digitalpark.modules.safety.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.safety.entity.SafetyNetwork;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网络安全Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface SafetyNetworkMapper extends BaseMapper<SafetyNetwork> {
}
