package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.entity.SysLog;
import com.digitalpark.modules.system.mapper.SysLogMapper;
import com.digitalpark.modules.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLogServiceImpl implements SysLogService {

    private final SysLogMapper sysLogMapper;

    @Override
    public PageResult<?> getLogList(Page<?> page, String module, String username, LocalDateTime startDate, LocalDateTime endDate) {
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(module)) {
            wrapper.like(SysLog::getModule, module);
        }
        if (StrUtil.isNotBlank(username)) {
            wrapper.like(SysLog::getCreateBy, username);
        }
        if (startDate != null) {
            wrapper.ge(SysLog::getCreateTime, startDate);
        }
        if (endDate != null) {
            wrapper.le(SysLog::getCreateTime, endDate);
        }
        wrapper.orderByDesc(SysLog::getCreateTime);

        Page<SysLog> logPage = sysLogMapper.selectPage((Page<SysLog>) page, wrapper);
        return PageResult.of(logPage.getRecords(), logPage.getTotal(), logPage.getCurrent(), logPage.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLog(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            sysLogMapper.deleteBatchIds(ids);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearLog() {
        sysLogMapper.delete(new LambdaQueryWrapper<>());
    }

    @Override
    public SysLog getLogById(Long id) {
        return sysLogMapper.selectById(id);
    }
}
