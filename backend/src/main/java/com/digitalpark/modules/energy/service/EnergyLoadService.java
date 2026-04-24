package com.digitalpark.modules.energy.service;

import com.digitalpark.modules.energy.entity.EnergyLoad;

import java.util.List;
import java.util.Map;

/**
 * 负荷管理Service接口
 *
 * @author digitalpark
 */
public interface EnergyLoadService {

    /**
     * 查询负荷列表
     */
    List<EnergyLoad> selectList(EnergyLoad query);

    /**
     * 根据ID查询
     */
    EnergyLoad selectById(Long id);

    /**
     * 新增负荷
     */
    int insert(EnergyLoad load);

    /**
     * 修改负荷
     */
    int update(EnergyLoad load);

    /**
     * 删除负荷
     */
    int deleteById(Long id);

    /**
     * 负荷统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 柔性调度
     */
    Map<String, Object> flexibleDispatch(Long loadId, String action, Double targetPower);

    /**
     * 负荷分析数据
     */
    Map<String, Object> getLoadAnalysis(String type, String startDate, String endDate, String granularity);
}
