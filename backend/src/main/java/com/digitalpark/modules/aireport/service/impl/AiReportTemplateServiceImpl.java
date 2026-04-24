package com.digitalpark.modules.aireport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.aireport.entity.AiReportTemplate;
import com.digitalpark.modules.aireport.mapper.AiReportTemplateMapper;
import com.digitalpark.modules.aireport.service.AiReportTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 报告模板Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class AiReportTemplateServiceImpl implements AiReportTemplateService {

    private final AiReportTemplateMapper templateMapper;

    @Override
    public PageResult<AiReportTemplate> selectPage(AiReportTemplate query, int pageNum, int pageSize) {
        LambdaQueryWrapper<AiReportTemplate> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(AiReportTemplate::getCreateTime);
        Page<AiReportTemplate> page = templateMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public AiReportTemplate selectById(Long id) {
        return templateMapper.selectById(id);
    }

    @Override
    public List<AiReportTemplate> selectAll() {
        return templateMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<AiReportTemplate> selectByModule(String module) {
        LambdaQueryWrapper<AiReportTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportTemplate::getModule, module);
        return templateMapper.selectList(wrapper);
    }

    @Override
    public List<AiReportTemplate> selectByType(String type) {
        LambdaQueryWrapper<AiReportTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportTemplate::getType, type);
        return templateMapper.selectList(wrapper);
    }

    @Override
    public List<AiReportTemplate> selectEnabled() {
        LambdaQueryWrapper<AiReportTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportTemplate::getStatus, "启用");
        return templateMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(AiReportTemplate template) {
        if (!StringUtils.hasText(template.getStatus())) {
            template.setStatus("启用");
        }
        return templateMapper.insert(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AiReportTemplate template) {
        return templateMapper.updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return templateMapper.deleteById(id);
    }

    private LambdaQueryWrapper<AiReportTemplate> buildQueryWrapper(AiReportTemplate query) {
        LambdaQueryWrapper<AiReportTemplate> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(AiReportTemplate::getName, query.getName());
        }
        if (StringUtils.hasText(query.getModule())) {
            wrapper.eq(AiReportTemplate::getModule, query.getModule());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(AiReportTemplate::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(AiReportTemplate::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
