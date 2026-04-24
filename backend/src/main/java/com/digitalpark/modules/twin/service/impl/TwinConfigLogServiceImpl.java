package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinConfigLog;
import com.digitalpark.modules.twin.mapper.TwinConfigLogMapper;
import com.digitalpark.modules.twin.service.TwinConfigLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TwinConfigLogServiceImpl implements TwinConfigLogService {

    private final TwinConfigLogMapper configLogMapper;

    @Override
    public void log(String module, String actionType, String detail, Long targetId, String targetName, String operator, String ip) {
        TwinConfigLog log = new TwinConfigLog();
        log.setModule(module);
        log.setActionType(actionType);
        log.setDetail(detail);
        log.setTargetId(targetId);
        log.setTargetName(targetName);
        log.setOperator(operator);
        log.setIp(ip);
        configLogMapper.insert(log);
    }

    @Override
    public PageResult<TwinConfigLog> list(String module, int pageNum, int pageSize) {
        LambdaQueryWrapper<TwinConfigLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(module)) {
            wrapper.eq(TwinConfigLog::getModule, module);
        }
        wrapper.orderByDesc(TwinConfigLog::getId);
        Page<TwinConfigLog> page = configLogMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }
}
