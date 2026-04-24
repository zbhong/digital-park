package com.digitalpark.modules.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyFacility;
import com.digitalpark.modules.property.mapper.PropertyFacilityMapper;
import com.digitalpark.modules.property.service.PropertyFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 设施运维Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class PropertyFacilityServiceImpl implements PropertyFacilityService {

    private final PropertyFacilityMapper facilityMapper;

    @Override
    public PageResult<PropertyFacility> selectPage(PropertyFacility query, int pageNum, int pageSize) {
        LambdaQueryWrapper<PropertyFacility> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(PropertyFacility::getCreateTime);
        Page<PropertyFacility> page = facilityMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PropertyFacility selectById(Long id) {
        return facilityMapper.selectById(id);
    }

    @Override
    public List<PropertyFacility> selectByType(String type) {
        LambdaQueryWrapper<PropertyFacility> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyFacility::getType, type);
        return facilityMapper.selectList(wrapper);
    }

    @Override
    public List<PropertyFacility> selectByAreaId(Long areaId) {
        LambdaQueryWrapper<PropertyFacility> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyFacility::getAreaId, areaId);
        return facilityMapper.selectList(wrapper);
    }

    @Override
    public List<PropertyFacility> selectCheckReminders() {
        LambdaQueryWrapper<PropertyFacility> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(PropertyFacility::getNextCheckDate, LocalDate.now().plusDays(7));
        wrapper.ne(PropertyFacility::getStatus, "报废");
        return facilityMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(PropertyFacility facility) {
        if (!StringUtils.hasText(facility.getStatus())) {
            facility.setStatus("正常");
        }
        return facilityMapper.insert(facility);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(PropertyFacility facility) {
        return facilityMapper.updateById(facility);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int check(Long id) {
        LambdaUpdateWrapper<PropertyFacility> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyFacility::getId, id)
               .set(PropertyFacility::getLastCheckDate, LocalDate.now());
        return facilityMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return facilityMapper.deleteById(id);
    }

    private LambdaQueryWrapper<PropertyFacility> buildQueryWrapper(PropertyFacility query) {
        LambdaQueryWrapper<PropertyFacility> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(PropertyFacility::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(PropertyFacility::getType, query.getType());
        }
        if (StringUtils.hasText(query.getLocation())) {
            wrapper.like(PropertyFacility::getLocation, query.getLocation());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(PropertyFacility::getAreaId, query.getAreaId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(PropertyFacility::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
