package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinAlert;
import com.digitalpark.modules.twin.mapper.TwinAlertMapper;
import com.digitalpark.modules.twin.service.TwinAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 告警记录Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class TwinAlertServiceImpl implements TwinAlertService {

    private final TwinAlertMapper alertMapper;

    @Override
    public PageResult<TwinAlert> selectPage(TwinAlert query, int pageNum, int pageSize) {
        LambdaQueryWrapper<TwinAlert> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(TwinAlert::getCreateTime);
        Page<TwinAlert> page = alertMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public TwinAlert selectById(Long id) {
        return alertMapper.selectById(id);
    }

    @Override
    public List<TwinAlert> selectByType(String type) {
        LambdaQueryWrapper<TwinAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TwinAlert::getType, type);
        wrapper.orderByDesc(TwinAlert::getCreateTime);
        return alertMapper.selectList(wrapper);
    }

    @Override
    public List<TwinAlert> selectByStatus(String status) {
        LambdaQueryWrapper<TwinAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TwinAlert::getStatus, status);
        wrapper.orderByDesc(TwinAlert::getCreateTime);
        return alertMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> selectStatistics() {
        Map<String, Object> result = new HashMap<>();

        // 总数统计
        long total = alertMapper.selectCount(new LambdaQueryWrapper<>());
        result.put("total", total);

        // 按状态统计
        Map<String, Long> statusStats = new HashMap<>();
        String[] statuses = {"待处理", "处理中", "已处理", "已关闭"};
        for (String status : statuses) {
            long count = alertMapper.selectCount(new LambdaQueryWrapper<TwinAlert>()
                    .eq(TwinAlert::getStatus, status));
            statusStats.put(status, count);
        }
        result.put("statusStats", statusStats);

        // 按类型统计
        Map<String, Long> typeStats = new HashMap<>();
        String[] types = {"安全", "设备", "环境", "能源", "消防"};
        for (String type : types) {
            long count = alertMapper.selectCount(new LambdaQueryWrapper<TwinAlert>()
                    .eq(TwinAlert::getType, type));
            typeStats.put(type, count);
        }
        result.put("typeStats", typeStats);

        // 按级别统计
        Map<String, Long> levelStats = new HashMap<>();
        String[] levels = {"提示", "一般", "较重", "严重", "紧急"};
        for (String level : levels) {
            long count = alertMapper.selectCount(new LambdaQueryWrapper<TwinAlert>()
                    .eq(TwinAlert::getLevel, level));
            levelStats.put(level, count);
        }
        result.put("levelStats", levelStats);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TwinAlert alert) {
        if (!StringUtils.hasText(alert.getStatus())) {
            alert.setStatus("待处理");
        }
        return alertMapper.insert(alert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TwinAlert alert) {
        return alertMapper.updateById(alert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handleAlert(Long id, Long handlerId, String handleResult) {
        LambdaUpdateWrapper<TwinAlert> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TwinAlert::getId, id)
               .set(TwinAlert::getStatus, "已处理")
               .set(TwinAlert::getHandlerId, handlerId)
               .set(TwinAlert::getHandleTime, LocalDateTime.now())
               .set(TwinAlert::getHandleResult, handleResult);
        return alertMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return alertMapper.deleteById(id);
    }

    private LambdaQueryWrapper<TwinAlert> buildQueryWrapper(TwinAlert query) {
        LambdaQueryWrapper<TwinAlert> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(TwinAlert::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(TwinAlert::getType, query.getType());
        }
        if (StringUtils.hasText(query.getLevel())) {
            wrapper.eq(TwinAlert::getLevel, query.getLevel());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(TwinAlert::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getSource())) {
            wrapper.eq(TwinAlert::getSource, query.getSource());
        }
        return wrapper;
    }
}
