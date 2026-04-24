package com.digitalpark.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.system.entity.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志 Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {
}
