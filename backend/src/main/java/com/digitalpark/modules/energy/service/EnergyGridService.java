package com.digitalpark.modules.energy.service;

import com.digitalpark.modules.energy.entity.EnergyGrid;

import java.util.List;

/**
 * 配电网Service接口
 *
 * @author digitalpark
 */
public interface EnergyGridService {

    /**
     * 查询配电网列表
     */
    List<EnergyGrid> selectList(EnergyGrid query);

    /**
     * 根据ID查询
     */
    EnergyGrid selectById(Long id);

    /**
     * 新增配电网
     */
    int insert(EnergyGrid grid);

    /**
     * 修改配电网
     */
    int update(EnergyGrid grid);

    /**
     * 删除配电网
     */
    int deleteById(Long id);
}
