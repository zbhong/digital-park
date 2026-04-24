package com.digitalpark.modules.emergency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyMaterial;
import com.digitalpark.modules.emergency.mapper.EmergencyMaterialMapper;
import com.digitalpark.modules.emergency.service.EmergencyMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 应急物资Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EmergencyMaterialServiceImpl implements EmergencyMaterialService {

    private final EmergencyMaterialMapper emergencyMaterialMapper;

    @Override
    public PageResult<EmergencyMaterial> selectPage(EmergencyMaterial query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EmergencyMaterial> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EmergencyMaterial::getCreateTime);
        Page<EmergencyMaterial> page = emergencyMaterialMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<EmergencyMaterial> selectList(EmergencyMaterial query) {
        LambdaQueryWrapper<EmergencyMaterial> wrapper = buildQueryWrapper(query);
        return emergencyMaterialMapper.selectList(wrapper);
    }

    @Override
    public EmergencyMaterial selectById(Long id) {
        return emergencyMaterialMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EmergencyMaterial material) {
        return emergencyMaterialMapper.insert(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EmergencyMaterial material) {
        return emergencyMaterialMapper.updateById(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return emergencyMaterialMapper.deleteById(id);
    }

    @Override
    public List<EmergencyMaterial> selectLowStock(int threshold) {
        LambdaQueryWrapper<EmergencyMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmergencyMaterial::getStatus, 1);
        wrapper.le(EmergencyMaterial::getQuantity, threshold);
        wrapper.orderByAsc(EmergencyMaterial::getQuantity);
        return emergencyMaterialMapper.selectList(wrapper);
    }

    private LambdaQueryWrapper<EmergencyMaterial> buildQueryWrapper(EmergencyMaterial query) {
        LambdaQueryWrapper<EmergencyMaterial> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(EmergencyMaterial::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCategory())) {
            wrapper.eq(EmergencyMaterial::getCategory, query.getCategory());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EmergencyMaterial::getAreaId, query.getAreaId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(EmergencyMaterial::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
