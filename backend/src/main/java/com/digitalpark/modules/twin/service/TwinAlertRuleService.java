package com.digitalpark.modules.twin.service;

import com.digitalpark.modules.twin.entity.TwinAlertRule;

import java.util.List;

public interface TwinAlertRuleService {

    List<TwinAlertRule> list();

    TwinAlertRule save(TwinAlertRule rule);

    TwinAlertRule update(TwinAlertRule rule);

    void delete(Long id);
}
