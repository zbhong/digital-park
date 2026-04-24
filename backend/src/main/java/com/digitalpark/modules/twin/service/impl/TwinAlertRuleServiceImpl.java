package com.digitalpark.modules.twin.service.impl;

import com.digitalpark.modules.twin.entity.TwinAlertRule;
import com.digitalpark.modules.twin.mapper.TwinAlertRuleMapper;
import com.digitalpark.modules.twin.service.TwinAlertRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwinAlertRuleServiceImpl implements TwinAlertRuleService {

    private final TwinAlertRuleMapper alertRuleMapper;

    @Override
    public List<TwinAlertRule> list() {
        return alertRuleMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>());
    }

    @Override
    public TwinAlertRule save(TwinAlertRule rule) {
        alertRuleMapper.insert(rule);
        return rule;
    }

    @Override
    public TwinAlertRule update(TwinAlertRule rule) {
        alertRuleMapper.updateById(rule);
        return rule;
    }

    @Override
    public void delete(Long id) {
        alertRuleMapper.deleteById(id);
    }
}
