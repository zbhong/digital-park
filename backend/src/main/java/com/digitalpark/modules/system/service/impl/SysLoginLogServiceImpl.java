package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.entity.SysLoginLog;
import com.digitalpark.modules.system.mapper.SysLoginLogMapper;
import com.digitalpark.modules.system.service.SysLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl implements SysLoginLogService {

    private final SysLoginLogMapper sysLoginLogMapper;

    @Override
    public PageResult<?> getLoginLogList(Page<?> page, String username, Integer status) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(username)) {
            wrapper.like(SysLoginLog::getUsername, username);
        }
        if (status != null) {
            wrapper.eq(SysLoginLog::getStatus, status);
        }
        wrapper.orderByDesc(SysLoginLog::getCreateTime);

        Page<SysLoginLog> logPage = sysLoginLogMapper.selectPage((Page<SysLoginLog>) page, wrapper);
        return PageResult.of(logPage.getRecords(), logPage.getTotal(), logPage.getCurrent(), logPage.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLoginLog(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            sysLoginLogMapper.deleteBatchIds(ids);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordLoginLog(String username, String ip, Integer status, String message) {
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUsername(username);
        loginLog.setIp(ip);
        loginLog.setStatus(status);
        loginLog.setMessage(message);
        loginLog.setCreateTime(LocalDateTime.now());
        sysLoginLogMapper.insert(loginLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearLoginLog() {
        sysLoginLogMapper.delete(new LambdaQueryWrapper<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockUser(String username) {
        // 删除该用户的登录失败日志记录
        sysLoginLogMapper.delete(
                new LambdaQueryWrapper<SysLoginLog>()
                        .eq(SysLoginLog::getUsername, username)
                        .eq(SysLoginLog::getStatus, 1)
        );
        log.info("已解锁用户: {}", username);
    }
}
