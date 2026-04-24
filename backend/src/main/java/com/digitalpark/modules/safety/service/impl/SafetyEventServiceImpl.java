package com.digitalpark.modules.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyEvent;
import com.digitalpark.modules.safety.mapper.SafetyEventMapper;
import com.digitalpark.modules.safety.service.SafetyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 安全事件Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class SafetyEventServiceImpl implements SafetyEventService {

    private final SafetyEventMapper safetyEventMapper;

    @Override
    public PageResult<SafetyEvent> selectPage(SafetyEvent query, int pageNum, int pageSize) {
        LambdaQueryWrapper<SafetyEvent> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SafetyEvent::getCreateTime);
        Page<SafetyEvent> page = safetyEventMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public SafetyEvent selectById(Long id) {
        return safetyEventMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SafetyEvent event) {
        event.setStatus("待处理");
        return safetyEventMapper.insert(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SafetyEvent event) {
        return safetyEventMapper.updateById(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return safetyEventMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handle(Long id, String handleResult) {
        SafetyEvent event = safetyEventMapper.selectById(id);
        if (event == null) {
            throw new BusinessException("安全事件不存在");
        }
        if ("已处理".equals(event.getStatus()) || "已关闭".equals(event.getStatus())) {
            throw new BusinessException("该事件已处理或已关闭");
        }
        SafetyEvent update = new SafetyEvent();
        update.setId(id);
        update.setStatus("已处理");
        update.setHandleResult(handleResult);
        update.setHandleTime(LocalDateTime.now());
        return safetyEventMapper.updateById(update);
    }

    @Override
    public Map<String, Object> selectStatistics() {
        List<SafetyEvent> allEvents = safetyEventMapper.selectList(null);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", allEvents.size());

        // 按类型统计
        Map<String, Long> typeStats = allEvents.stream()
                .filter(e -> e.getType() != null)
                .collect(Collectors.groupingBy(SafetyEvent::getType, Collectors.counting()));
        result.put("typeStatistics", typeStats);

        // 按等级统计
        Map<String, Long> levelStats = allEvents.stream()
                .filter(e -> e.getLevel() != null)
                .collect(Collectors.groupingBy(SafetyEvent::getLevel, Collectors.counting()));
        result.put("levelStatistics", levelStats);

        // 按状态统计
        Map<String, Long> statusStats = allEvents.stream()
                .filter(e -> e.getStatus() != null)
                .collect(Collectors.groupingBy(SafetyEvent::getStatus, Collectors.counting()));
        result.put("statusStatistics", statusStats);

        // 待处理数量
        long pendingCount = allEvents.stream()
                .filter(e -> "待处理".equals(e.getStatus()))
                .count();
        result.put("pendingCount", pendingCount);

        return result;
    }

    private LambdaQueryWrapper<SafetyEvent> buildQueryWrapper(SafetyEvent query) {
        LambdaQueryWrapper<SafetyEvent> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(SafetyEvent::getType, query.getType());
        }
        if (StringUtils.hasText(query.getLevel())) {
            wrapper.eq(SafetyEvent::getLevel, query.getLevel());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SafetyEvent::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getLocation())) {
            wrapper.like(SafetyEvent::getLocation, query.getLocation());
        }
        if (query.getMonitorPointId() != null) {
            wrapper.eq(SafetyEvent::getMonitorPointId, query.getMonitorPointId());
        }
        return wrapper;
    }
}
