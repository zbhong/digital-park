package com.digitalpark.modules.aireport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.aireport.entity.AiReportInstance;
import com.digitalpark.modules.aireport.entity.AiReportTemplate;
import com.digitalpark.modules.aireport.mapper.AiReportInstanceMapper;
import com.digitalpark.modules.aireport.service.AiReportInstanceService;
import com.digitalpark.modules.aireport.service.AiReportTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报告实例Service实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiReportInstanceServiceImpl implements AiReportInstanceService {

    private final AiReportInstanceMapper instanceMapper;
    private final AiReportTemplateService templateService;

    @Override
    public PageResult<AiReportInstance> selectPage(AiReportInstance query, int pageNum, int pageSize) {
        LambdaQueryWrapper<AiReportInstance> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(AiReportInstance::getCreateTime);
        Page<AiReportInstance> page = instanceMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public AiReportInstance selectById(Long id) {
        return instanceMapper.selectById(id);
    }

    @Override
    public List<AiReportInstance> selectByTemplateId(Long templateId) {
        LambdaQueryWrapper<AiReportInstance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportInstance::getTemplateId, templateId);
        wrapper.orderByDesc(AiReportInstance::getCreateTime);
        return instanceMapper.selectList(wrapper);
    }

    @Override
    public List<AiReportInstance> selectByModule(String module) {
        LambdaQueryWrapper<AiReportInstance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportInstance::getModule, module);
        wrapper.orderByDesc(AiReportInstance::getCreateTime);
        return instanceMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> selectStatisticsByModule() {
        Map<String, Object> result = new HashMap<>();
        String[] modules = {"安全", "应急", "能源", "环境", "经济", "企业", "物业", "招商", "设备", "综合"};
        Map<String, Long> moduleStats = new HashMap<>();
        long total = 0;
        for (String module : modules) {
            long count = instanceMapper.selectCount(new LambdaQueryWrapper<AiReportInstance>()
                    .eq(AiReportInstance::getModule, module));
            moduleStats.put(module, count);
            total += count;
        }
        result.put("total", total);
        result.put("moduleStats", moduleStats);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiReportInstance generateReport(Long templateId, String period, Map<String, Object> customParams) {
        AiReportTemplate template = templateService.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("报告模板不存在");
        }

        // 创建报告实例
        AiReportInstance instance = new AiReportInstance();
        instance.setTemplateId(templateId);
        instance.setTitle(generateTitle(template, period));
        instance.setModule(template.getModule());
        instance.setType(template.getType());
        instance.setPeriod(period);
        instance.setStatus("生成中");
        instance.setGenerateTime(LocalDateTime.now());
        instanceMapper.insert(instance);

        try {
            // 模拟AI生成报告内容
            String content = simulateAiGeneration(template, period, customParams);
            instance.setContent(content);
            instance.setStatus("已完成");
            instance.setFileSize((long) content.getBytes().length / 1024);
            instanceMapper.updateById(instance);
        } catch (Exception e) {
            log.error("生成报告失败, templateId={}, period={}", templateId, period, e);
            instance.setStatus("失败");
            instanceMapper.updateById(instance);
            throw new RuntimeException("报告生成失败: " + e.getMessage());
        }

        return instance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(AiReportInstance instance) {
        return instanceMapper.insert(instance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AiReportInstance instance) {
        return instanceMapper.updateById(instance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return instanceMapper.deleteById(id);
    }

    /**
     * 生成报告标题
     */
    private String generateTitle(AiReportTemplate template, String period) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        if (StringUtils.hasText(period)) {
            return template.getName() + " - " + period;
        }
        return template.getName() + " - " + now;
    }

    /**
     * 模拟AI生成报告内容
     */
    private String simulateAiGeneration(AiReportTemplate template, String period, Map<String, Object> customParams) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(template.getName()).append("\n\n");
        sb.append("## 一、概述\n\n");
        sb.append("本报告由AI智能分析系统自动生成，报告周期：").append(period != null ? period : "当前周期").append("。\n\n");
        sb.append("所属模块：").append(template.getModule()).append("\n");
        sb.append("报告类型：").append(template.getType()).append("\n\n");

        sb.append("## 二、核心数据\n\n");
        sb.append("- 综合评分：92.5分\n");
        sb.append("- 同比增长：8.3%\n");
        sb.append("- 环比变化：+2.1%\n\n");

        sb.append("## 三、趋势分析\n\n");
        sb.append("本期各项指标整体呈上升趋势，其中核心运营指标表现优异，\n");
        sb.append("较上期有明显改善。建议持续关注关键指标的波动情况。\n\n");

        sb.append("## 四、风险提示\n\n");
        sb.append("- 部分设备运行参数接近预警阈值，建议加强巡检\n");
        sb.append("- 能源消耗较上月有所上升，建议优化用能策略\n\n");

        sb.append("## 五、改进建议\n\n");
        sb.append("1. 加强设备日常维护，降低故障率\n");
        sb.append("2. 优化能源管理策略，提升能效\n");
        sb.append("3. 完善安全巡检机制，消除安全隐患\n\n");

        sb.append("---\n");
        sb.append("报告生成时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");

        return sb.toString();
    }

    private LambdaQueryWrapper<AiReportInstance> buildQueryWrapper(AiReportInstance query) {
        LambdaQueryWrapper<AiReportInstance> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(AiReportInstance::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getModule())) {
            wrapper.eq(AiReportInstance::getModule, query.getModule());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(AiReportInstance::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(AiReportInstance::getStatus, query.getStatus());
        }
        if (query.getTemplateId() != null) {
            wrapper.eq(AiReportInstance::getTemplateId, query.getTemplateId());
        }
        return wrapper;
    }
}
