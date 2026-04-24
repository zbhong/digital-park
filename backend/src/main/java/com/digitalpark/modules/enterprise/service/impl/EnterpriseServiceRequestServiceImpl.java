package com.digitalpark.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.enterprise.entity.EnterpriseServiceRequest;
import com.digitalpark.modules.enterprise.mapper.EnterpriseServiceRequestMapper;
import com.digitalpark.modules.enterprise.service.EnterpriseServiceRequestService;
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
 * 服务请求Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnterpriseServiceRequestServiceImpl implements EnterpriseServiceRequestService {

    private final EnterpriseServiceRequestMapper requestMapper;

    @Override
    public PageResult<EnterpriseServiceRequest> selectPage(EnterpriseServiceRequest query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EnterpriseServiceRequest> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EnterpriseServiceRequest::getCreateTime);
        Page<EnterpriseServiceRequest> page = requestMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EnterpriseServiceRequest selectById(Long id) {
        return requestMapper.selectById(id);
    }

    @Override
    public List<EnterpriseServiceRequest> selectByEnterpriseId(Long enterpriseId) {
        LambdaQueryWrapper<EnterpriseServiceRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseServiceRequest::getEnterpriseId, enterpriseId);
        wrapper.orderByDesc(EnterpriseServiceRequest::getCreateTime);
        return requestMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTypeStatistics() {
        List<EnterpriseServiceRequest> list = requestMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (EnterpriseServiceRequest request : list) {
            String type = StringUtils.hasText(request.getType()) ? request.getType() : "未分类";
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
        List<EnterpriseServiceRequest> list = requestMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (EnterpriseServiceRequest request : list) {
            String status = StringUtils.hasText(request.getStatus()) ? request.getStatus() : "未知";
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
    @Transactional(rollbackFor = Exception.class)
    public int insert(EnterpriseServiceRequest request) {
        if (!StringUtils.hasText(request.getStatus())) {
            request.setStatus("待处理");
        }
        return requestMapper.insert(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handle(Long id, Long handlerId, String handleResult) {
        LambdaUpdateWrapper<EnterpriseServiceRequest> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EnterpriseServiceRequest::getId, id)
               .set(EnterpriseServiceRequest::getHandlerId, handlerId)
               .set(EnterpriseServiceRequest::getHandleResult, handleResult)
               .set(EnterpriseServiceRequest::getHandleTime, LocalDateTime.now())
               .set(EnterpriseServiceRequest::getStatus, "已完成");
        return requestMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int close(Long id) {
        LambdaUpdateWrapper<EnterpriseServiceRequest> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EnterpriseServiceRequest::getId, id)
               .set(EnterpriseServiceRequest::getStatus, "已关闭");
        return requestMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return requestMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EnterpriseServiceRequest> buildQueryWrapper(EnterpriseServiceRequest query) {
        LambdaQueryWrapper<EnterpriseServiceRequest> wrapper = new LambdaQueryWrapper<>();
        if (query.getEnterpriseId() != null) {
            wrapper.eq(EnterpriseServiceRequest::getEnterpriseId, query.getEnterpriseId());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(EnterpriseServiceRequest::getType, query.getType());
        }
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(EnterpriseServiceRequest::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(EnterpriseServiceRequest::getStatus, query.getStatus());
        }
        if (query.getHandlerId() != null) {
            wrapper.eq(EnterpriseServiceRequest::getHandlerId, query.getHandlerId());
        }
        return wrapper;
    }
}
