package com.digitalpark.modules.emergency.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyTeam;

import java.util.List;

/**
 * 应急队伍Service接口
 *
 * @author digitalpark
 */
public interface EmergencyTeamService {

    PageResult<EmergencyTeam> selectPage(EmergencyTeam query, int pageNum, int pageSize);

    List<EmergencyTeam> selectList(EmergencyTeam query);

    EmergencyTeam selectById(Long id);

    int insert(EmergencyTeam team);

    int update(EmergencyTeam team);

    int deleteById(Long id);
}
