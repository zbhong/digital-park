package com.digitalpark.modules.economy.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.economy.entity.EconomyReport;
import com.digitalpark.modules.economy.service.EconomyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 经济报告Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/economy/report")
@RequiredArgsConstructor
public class EconomyReportController {

    private final EconomyReportService reportService;

    @PreAuthorize("@ss.hasPermi('economy:report:list')")
    @GetMapping("/page")
    public R<PageResult<EconomyReport>> page(EconomyReport query,
                                              @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(reportService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('economy:report:query')")
    @GetMapping("/{id}")
    public R<EconomyReport> getInfo(@PathVariable Long id) {
        return R.ok(reportService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('economy:report:add')")
    @PostMapping
    public R<EconomyReport> add(@RequestBody EconomyReport report) {
        reportService.insert(report);
        return R.ok(report);
    }

    @PreAuthorize("@ss.hasPermi('economy:report:edit')")
    @PutMapping
    public R<EconomyReport> edit(@RequestBody EconomyReport report) {
        reportService.update(report);
        return R.ok(report);
    }

    @PreAuthorize("@ss.hasPermi('economy:report:publish')")
    @PutMapping("/publish/{id}")
    public R<Void> publish(@PathVariable Long id) {
        reportService.publish(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('economy:report:generate')")
    @PostMapping("/generate")
    public R<Void> generate(@RequestParam String type, @RequestParam String period) {
        reportService.generateReport(type, period);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('economy:report:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        reportService.deleteById(id);
        return R.ok();
    }
}
