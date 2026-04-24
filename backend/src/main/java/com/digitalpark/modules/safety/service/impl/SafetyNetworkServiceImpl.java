package com.digitalpark.modules.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyNetwork;
import com.digitalpark.modules.safety.mapper.SafetyNetworkMapper;
import com.digitalpark.modules.safety.service.SafetyNetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 网络安全Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class SafetyNetworkServiceImpl implements SafetyNetworkService {

    private final SafetyNetworkMapper safetyNetworkMapper;

    @Override
    public PageResult<SafetyNetwork> selectPage(SafetyNetwork query, int pageNum, int pageSize) {
        LambdaQueryWrapper<SafetyNetwork> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SafetyNetwork::getCreateTime);
        Page<SafetyNetwork> page = safetyNetworkMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public SafetyNetwork selectById(Long id) {
        return safetyNetworkMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SafetyNetwork network) {
        network.setStatus("待处理");
        return safetyNetworkMapper.insert(network);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SafetyNetwork network) {
        return safetyNetworkMapper.updateById(network);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return safetyNetworkMapper.deleteById(id);
    }

    private LambdaQueryWrapper<SafetyNetwork> buildQueryWrapper(SafetyNetwork query) {
        LambdaQueryWrapper<SafetyNetwork> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(SafetyNetwork::getType, query.getType());
        }
        if (StringUtils.hasText(query.getEventType())) {
            wrapper.eq(SafetyNetwork::getEventType, query.getEventType());
        }
        if (StringUtils.hasText(query.getSourceIp())) {
            wrapper.like(SafetyNetwork::getSourceIp, query.getSourceIp());
        }
        if (StringUtils.hasText(query.getSeverity())) {
            wrapper.eq(SafetyNetwork::getSeverity, query.getSeverity());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SafetyNetwork::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
