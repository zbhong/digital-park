package com.digitalpark.modules.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyProduction;
import com.digitalpark.modules.safety.mapper.SafetyProductionMapper;
import com.digitalpark.modules.safety.service.SafetyProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 生产安全Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class SafetyProductionServiceImpl implements SafetyProductionService {

    private final SafetyProductionMapper safetyProductionMapper;

    @Override
    public PageResult<SafetyProduction> selectPage(SafetyProduction query, int pageNum, int pageSize) {
        LambdaQueryWrapper<SafetyProduction> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SafetyProduction::getCreateTime);
        Page<SafetyProduction> page = safetyProductionMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public SafetyProduction selectById(Long id) {
        return safetyProductionMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SafetyProduction production) {
        production.setStatus("待处理");
        return safetyProductionMapper.insert(production);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SafetyProduction production) {
        return safetyProductionMapper.updateById(production);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return safetyProductionMapper.deleteById(id);
    }

    private LambdaQueryWrapper<SafetyProduction> buildQueryWrapper(SafetyProduction query) {
        LambdaQueryWrapper<SafetyProduction> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(SafetyProduction::getType, query.getType());
        }
        if (query.getEnterpriseId() != null) {
            wrapper.eq(SafetyProduction::getEnterpriseId, query.getEnterpriseId());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(SafetyProduction::getAreaId, query.getAreaId());
        }
        if (StringUtils.hasText(query.getSeverity())) {
            wrapper.eq(SafetyProduction::getSeverity, query.getSeverity());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SafetyProduction::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
