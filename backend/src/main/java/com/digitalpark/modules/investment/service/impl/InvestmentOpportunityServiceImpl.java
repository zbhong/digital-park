package com.digitalpark.modules.investment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentOpportunity;
import com.digitalpark.modules.investment.mapper.InvestmentOpportunityMapper;
import com.digitalpark.modules.investment.service.InvestmentOpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商机Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class InvestmentOpportunityServiceImpl implements InvestmentOpportunityService {

    private final InvestmentOpportunityMapper opportunityMapper;

    @Override
    public PageResult<InvestmentOpportunity> selectPage(InvestmentOpportunity query, int pageNum, int pageSize) {
        LambdaQueryWrapper<InvestmentOpportunity> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(InvestmentOpportunity::getCreateTime);
        Page<InvestmentOpportunity> page = opportunityMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public InvestmentOpportunity selectById(Long id) {
        return opportunityMapper.selectById(id);
    }

    @Override
    public List<InvestmentOpportunity> selectByCustomerId(Long customerId) {
        LambdaQueryWrapper<InvestmentOpportunity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentOpportunity::getCustomerId, customerId);
        wrapper.orderByDesc(InvestmentOpportunity::getCreateTime);
        return opportunityMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectStageStatistics() {
        List<InvestmentOpportunity> list = opportunityMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (InvestmentOpportunity opp : list) {
            String stage = StringUtils.hasText(opp.getStage()) ? opp.getStage() : "未知";
            stats.merge(stage, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("stage", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(InvestmentOpportunity opportunity) {
        if (!StringUtils.hasText(opportunity.getStage())) {
            opportunity.setStage("初次接洽");
        }
        return opportunityMapper.insert(opportunity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(InvestmentOpportunity opportunity) {
        return opportunityMapper.updateById(opportunity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int advanceStage(Long id, String stage) {
        LambdaUpdateWrapper<InvestmentOpportunity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentOpportunity::getId, id)
               .set(InvestmentOpportunity::getStage, stage);
        return opportunityMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return opportunityMapper.deleteById(id);
    }

    private LambdaQueryWrapper<InvestmentOpportunity> buildQueryWrapper(InvestmentOpportunity query) {
        LambdaQueryWrapper<InvestmentOpportunity> wrapper = new LambdaQueryWrapper<>();
        if (query.getCustomerId() != null) {
            wrapper.eq(InvestmentOpportunity::getCustomerId, query.getCustomerId());
        }
        if (StringUtils.hasText(query.getStage())) {
            wrapper.eq(InvestmentOpportunity::getStage, query.getStage());
        }
        return wrapper;
    }
}
