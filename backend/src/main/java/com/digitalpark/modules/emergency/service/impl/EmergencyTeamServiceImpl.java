package com.digitalpark.modules.emergency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyTeam;
import com.digitalpark.modules.emergency.mapper.EmergencyTeamMapper;
import com.digitalpark.modules.emergency.service.EmergencyTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 应急队伍Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EmergencyTeamServiceImpl implements EmergencyTeamService {

    private final EmergencyTeamMapper emergencyTeamMapper;

    @Override
    public PageResult<EmergencyTeam> selectPage(EmergencyTeam query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EmergencyTeam> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EmergencyTeam::getCreateTime);
        Page<EmergencyTeam> page = emergencyTeamMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<EmergencyTeam> selectList(EmergencyTeam query) {
        LambdaQueryWrapper<EmergencyTeam> wrapper = buildQueryWrapper(query);
        return emergencyTeamMapper.selectList(wrapper);
    }

    @Override
    public EmergencyTeam selectById(Long id) {
        return emergencyTeamMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EmergencyTeam team) {
        return emergencyTeamMapper.insert(team);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EmergencyTeam team) {
        return emergencyTeamMapper.updateById(team);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return emergencyTeamMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EmergencyTeam> buildQueryWrapper(EmergencyTeam query) {
        LambdaQueryWrapper<EmergencyTeam> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(EmergencyTeam::getName, query.getName());
        }
        if (StringUtils.hasText(query.getLeader())) {
            wrapper.like(EmergencyTeam::getLeader, query.getLeader());
        }
        return wrapper;
    }
}
