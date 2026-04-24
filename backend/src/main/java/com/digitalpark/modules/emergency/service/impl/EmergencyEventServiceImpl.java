package com.digitalpark.modules.emergency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyEvent;
import com.digitalpark.modules.emergency.mapper.EmergencyEventMapper;
import com.digitalpark.modules.emergency.service.EmergencyEventService;
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
 * 应急事件Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EmergencyEventServiceImpl implements EmergencyEventService {

    private final EmergencyEventMapper emergencyEventMapper;

    @Override
    public PageResult<EmergencyEvent> selectPage(EmergencyEvent query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EmergencyEvent> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EmergencyEvent::getCreateTime);
        Page<EmergencyEvent> page = emergencyEventMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EmergencyEvent selectById(Long id) {
        return emergencyEventMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EmergencyEvent event) {
        event.setStatus("待响应");
        return emergencyEventMapper.insert(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EmergencyEvent event) {
        return emergencyEventMapper.updateById(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return emergencyEventMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handle(Long id, String handleResult) {
        EmergencyEvent event = emergencyEventMapper.selectById(id);
        if (event == null) {
            throw new BusinessException("应急事件不存在");
        }
        if ("已完成".equals(event.getStatus())) {
            throw new BusinessException("该事件已完成");
        }
        EmergencyEvent update = new EmergencyEvent();
        update.setId(id);
        update.setStatus("已完成");
        update.setHandleResult(handleResult);
        update.setHandleTime(LocalDateTime.now());
        return emergencyEventMapper.updateById(update);
    }

    @Override
    public Map<String, Object> selectStatistics() {
        List<EmergencyEvent> allEvents = emergencyEventMapper.selectList(null);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", allEvents.size());

        // 按类型统计
        Map<String, Long> typeStats = allEvents.stream()
                .filter(e -> e.getType() != null)
                .collect(Collectors.groupingBy(EmergencyEvent::getType, Collectors.counting()));
        result.put("typeStatistics", typeStats);

        // 按等级统计
        Map<String, Long> levelStats = allEvents.stream()
                .filter(e -> e.getLevel() != null)
                .collect(Collectors.groupingBy(EmergencyEvent::getLevel, Collectors.counting()));
        result.put("levelStatistics", levelStats);

        // 按状态统计
        Map<String, Long> statusStats = allEvents.stream()
                .filter(e -> e.getStatus() != null)
                .collect(Collectors.groupingBy(EmergencyEvent::getStatus, Collectors.counting()));
        result.put("statusStatistics", statusStats);

        // 待响应数量
        long pendingCount = allEvents.stream()
                .filter(e -> "待响应".equals(e.getStatus()))
                .count();
        result.put("pendingCount", pendingCount);

        return result;
    }

    private LambdaQueryWrapper<EmergencyEvent> buildQueryWrapper(EmergencyEvent query) {
        LambdaQueryWrapper<EmergencyEvent> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(EmergencyEvent::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(EmergencyEvent::getType, query.getType());
        }
        if (StringUtils.hasText(query.getLevel())) {
            wrapper.eq(EmergencyEvent::getLevel, query.getLevel());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(EmergencyEvent::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getLocation())) {
            wrapper.like(EmergencyEvent::getLocation, query.getLocation());
        }
        return wrapper;
    }
}
