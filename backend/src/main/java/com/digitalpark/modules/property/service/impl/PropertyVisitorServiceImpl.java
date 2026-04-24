package com.digitalpark.modules.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyVisitor;
import com.digitalpark.modules.property.mapper.PropertyVisitorMapper;
import com.digitalpark.modules.property.service.PropertyVisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 访客Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class PropertyVisitorServiceImpl implements PropertyVisitorService {

    private final PropertyVisitorMapper visitorMapper;

    @Override
    public PageResult<PropertyVisitor> selectPage(PropertyVisitor query, int pageNum, int pageSize) {
        LambdaQueryWrapper<PropertyVisitor> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(PropertyVisitor::getCreateTime);
        Page<PropertyVisitor> page = visitorMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PropertyVisitor selectById(Long id) {
        return visitorMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(PropertyVisitor visitor) {
        if (!StringUtils.hasText(visitor.getStatus())) {
            visitor.setStatus("待审核");
        }
        return visitorMapper.insert(visitor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(Long id) {
        LambdaUpdateWrapper<PropertyVisitor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyVisitor::getId, id).set(PropertyVisitor::getStatus, "已通过");
        return visitorMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(Long id) {
        LambdaUpdateWrapper<PropertyVisitor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyVisitor::getId, id).set(PropertyVisitor::getStatus, "已拒绝");
        return visitorMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int leave(Long id) {
        LambdaUpdateWrapper<PropertyVisitor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyVisitor::getId, id).set(PropertyVisitor::getStatus, "已离园");
        return visitorMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return visitorMapper.deleteById(id);
    }

    private LambdaQueryWrapper<PropertyVisitor> buildQueryWrapper(PropertyVisitor query) {
        LambdaQueryWrapper<PropertyVisitor> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(PropertyVisitor::getName, query.getName());
        }
        if (StringUtils.hasText(query.getPhone())) {
            wrapper.eq(PropertyVisitor::getPhone, query.getPhone());
        }
        if (StringUtils.hasText(query.getVisitEnterprise())) {
            wrapper.like(PropertyVisitor::getVisitEnterprise, query.getVisitEnterprise());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(PropertyVisitor::getStatus, query.getStatus());
        }
        if (query.getVisitDate() != null) {
            wrapper.eq(PropertyVisitor::getVisitDate, query.getVisitDate());
        }
        return wrapper;
    }
}
