package com.digitalpark.modules.economy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.economy.entity.EconomyReport;
import com.digitalpark.modules.economy.mapper.EconomyReportMapper;
import com.digitalpark.modules.economy.service.EconomyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 经济报告Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EconomyReportServiceImpl implements EconomyReportService {

    private final EconomyReportMapper reportMapper;

    @Override
    public PageResult<EconomyReport> selectPage(EconomyReport query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EconomyReport> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EconomyReport::getCreateTime);
        Page<EconomyReport> page = reportMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EconomyReport selectById(Long id) {
        return reportMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EconomyReport report) {
        if (!StringUtils.hasText(report.getStatus())) {
            report.setStatus("草稿");
        }
        return reportMapper.insert(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EconomyReport report) {
        return reportMapper.updateById(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int publish(Long id) {
        LambdaUpdateWrapper<EconomyReport> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EconomyReport::getId, id)
               .set(EconomyReport::getStatus, "已发布");
        return reportMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateReport(String type, String period) {
        EconomyReport report = new EconomyReport();
        report.setTitle(type + "经济运行报告 - " + period);
        report.setType(type);
        report.setPeriod(period);
        report.setStatus("草稿");
        report.setContent("自动生成的" + type + "经济运行报告内容，周期：" + period);
        return reportMapper.insert(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return reportMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EconomyReport> buildQueryWrapper(EconomyReport query) {
        LambdaQueryWrapper<EconomyReport> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(EconomyReport::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(EconomyReport::getType, query.getType());
        }
        if (StringUtils.hasText(query.getPeriod())) {
            wrapper.eq(EconomyReport::getPeriod, query.getPeriod());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(EconomyReport::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
