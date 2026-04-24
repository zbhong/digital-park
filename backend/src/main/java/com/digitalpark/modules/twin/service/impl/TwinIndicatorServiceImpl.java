package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinIndicator;
import com.digitalpark.modules.twin.mapper.TwinIndicatorMapper;
import com.digitalpark.modules.twin.service.TwinIndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 核心指标Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class TwinIndicatorServiceImpl implements TwinIndicatorService {

    private final TwinIndicatorMapper indicatorMapper;

    @Override
    public PageResult<TwinIndicator> selectPage(TwinIndicator query, int pageNum, int pageSize) {
        LambdaQueryWrapper<TwinIndicator> wrapper = buildQueryWrapper(query);
        wrapper.orderByAsc(TwinIndicator::getCode);
        Page<TwinIndicator> page = indicatorMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public TwinIndicator selectById(Long id) {
        return indicatorMapper.selectById(id);
    }

    @Override
    public List<TwinIndicator> selectAll() {
        return indicatorMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<TwinIndicator> selectByCategory(String category) {
        LambdaQueryWrapper<TwinIndicator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TwinIndicator::getCategory, category);
        return indicatorMapper.selectList(wrapper);
    }

    @Override
    public List<TwinIndicator> selectRealtimeData() {
        List<TwinIndicator> indicators = indicatorMapper.selectList(new LambdaQueryWrapper<>());
        // 模拟实时数据刷新：在目标值附近随机波动
        for (TwinIndicator indicator : indicators) {
            if (indicator.getTargetValue() != null) {
                double base = indicator.getTargetValue().doubleValue();
                double fluctuation = base * 0.1 * (ThreadLocalRandom.current().nextDouble() * 2 - 1);
                indicator.setCurrentValue(BigDecimal.valueOf(base + fluctuation).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return indicators;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TwinIndicator indicator) {
        return indicatorMapper.insert(indicator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TwinIndicator indicator) {
        return indicatorMapper.updateById(indicator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return indicatorMapper.deleteById(id);
    }

    private LambdaQueryWrapper<TwinIndicator> buildQueryWrapper(TwinIndicator query) {
        LambdaQueryWrapper<TwinIndicator> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(TwinIndicator::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.eq(TwinIndicator::getCode, query.getCode());
        }
        if (StringUtils.hasText(query.getCategory())) {
            wrapper.eq(TwinIndicator::getCategory, query.getCategory());
        }
        if (StringUtils.hasText(query.getChartType())) {
            wrapper.eq(TwinIndicator::getChartType, query.getChartType());
        }
        return wrapper;
    }
}
