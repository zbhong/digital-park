package com.digitalpark.modules.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyWorkOrder;
import com.digitalpark.modules.property.mapper.PropertyWorkOrderMapper;
import com.digitalpark.modules.property.service.PropertyWorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物业工单Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class PropertyWorkOrderServiceImpl implements PropertyWorkOrderService {

    private final PropertyWorkOrderMapper orderMapper;

    @Override
    public PageResult<PropertyWorkOrder> selectPage(PropertyWorkOrder query, int pageNum, int pageSize) {
        LambdaQueryWrapper<PropertyWorkOrder> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(PropertyWorkOrder::getCreateTime);
        Page<PropertyWorkOrder> page = orderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PropertyWorkOrder selectById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public List<PropertyWorkOrder> selectByEnterpriseId(Long enterpriseId) {
        LambdaQueryWrapper<PropertyWorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyWorkOrder::getEnterpriseId, enterpriseId);
        wrapper.orderByDesc(PropertyWorkOrder::getCreateTime);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTypeStatistics() {
        List<PropertyWorkOrder> list = orderMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (PropertyWorkOrder order : list) {
            String type = StringUtils.hasText(order.getType()) ? order.getType() : "未分类";
            stats.merge(type, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectStatusStatistics() {
        List<PropertyWorkOrder> list = orderMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (PropertyWorkOrder order : list) {
            String status = StringUtils.hasText(order.getStatus()) ? order.getStatus() : "未知";
            stats.merge(status, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("status", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectPriorityStatistics() {
        List<PropertyWorkOrder> list = orderMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (PropertyWorkOrder order : list) {
            String priority = StringUtils.hasText(order.getPriority()) ? order.getPriority() : "未知";
            stats.merge(priority, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("priority", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(PropertyWorkOrder order) {
        if (!StringUtils.hasText(order.getStatus())) {
            order.setStatus("待派单");
        }
        return orderMapper.insert(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int dispatch(Long id, Long handlerId) {
        LambdaUpdateWrapper<PropertyWorkOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyWorkOrder::getId, id)
               .set(PropertyWorkOrder::getHandlerId, handlerId)
               .set(PropertyWorkOrder::getStatus, "已派单");
        return orderMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handle(Long id, String handleResult) {
        LambdaUpdateWrapper<PropertyWorkOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyWorkOrder::getId, id)
               .set(PropertyWorkOrder::getHandleResult, handleResult)
               .set(PropertyWorkOrder::getHandleTime, LocalDateTime.now())
               .set(PropertyWorkOrder::getStatus, "处理中");
        return orderMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int complete(Long id) {
        LambdaUpdateWrapper<PropertyWorkOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyWorkOrder::getId, id)
               .set(PropertyWorkOrder::getHandleTime, LocalDateTime.now())
               .set(PropertyWorkOrder::getStatus, "已完成");
        return orderMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int evaluate(Long id, Integer satisfaction) {
        LambdaUpdateWrapper<PropertyWorkOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyWorkOrder::getId, id)
               .set(PropertyWorkOrder::getSatisfaction, satisfaction)
               .set(PropertyWorkOrder::getStatus, "已评价");
        return orderMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return orderMapper.deleteById(id);
    }

    private LambdaQueryWrapper<PropertyWorkOrder> buildQueryWrapper(PropertyWorkOrder query) {
        LambdaQueryWrapper<PropertyWorkOrder> wrapper = new LambdaQueryWrapper<>();
        if (query.getEnterpriseId() != null) {
            wrapper.eq(PropertyWorkOrder::getEnterpriseId, query.getEnterpriseId());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(PropertyWorkOrder::getType, query.getType());
        }
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(PropertyWorkOrder::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getPriority())) {
            wrapper.eq(PropertyWorkOrder::getPriority, query.getPriority());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(PropertyWorkOrder::getStatus, query.getStatus());
        }
        if (query.getHandlerId() != null) {
            wrapper.eq(PropertyWorkOrder::getHandlerId, query.getHandlerId());
        }
        return wrapper;
    }
}
