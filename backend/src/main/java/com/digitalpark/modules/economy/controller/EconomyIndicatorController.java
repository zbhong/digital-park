package com.digitalpark.modules.economy.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.economy.entity.EconomyIndicator;
import com.digitalpark.modules.economy.service.EconomyIndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 经济指标Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/economy/indicator")
@RequiredArgsConstructor
public class EconomyIndicatorController {

    private final EconomyIndicatorService indicatorService;

    @PreAuthorize("@ss.hasPermi('economy:indicator:list')")
    @GetMapping("/page")
    public R<PageResult<EconomyIndicator>> page(EconomyIndicator query,
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(indicatorService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/{id}")
    public R<EconomyIndicator> getInfo(@PathVariable Long id) {
        return R.ok(indicatorService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/type/{type}")
    public R<List<EconomyIndicator>> getByType(@PathVariable String type) {
        return R.ok(indicatorService.selectByType(type));
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/period/{period}")
    public R<List<EconomyIndicator>> getByPeriod(@PathVariable String period) {
        return R.ok(indicatorService.selectByPeriod(period));
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/statistics")
    public R<List<Map<String, Object>>> statistics(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String period) {
        return R.ok(indicatorService.selectStatistics(type, period));
    }

    /**
     * 园区经济概览数据（产值、税收、就业等汇总）
     */
    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        return R.ok(indicatorService.selectOverview());
    }

    /**
     * 产值统计数据
     */
    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/output")
    public R<List<Map<String, Object>>> output(
            @RequestParam(required = false) String period) {
        return R.ok(indicatorService.selectStatistics("产值", period));
    }

    /**
     * 税收统计数据
     */
    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/tax")
    public R<List<Map<String, Object>>> tax(
            @RequestParam(required = false) String period) {
        return R.ok(indicatorService.selectStatistics("税收", period));
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:query')")
    @GetMapping("/trend")
    public R<List<Map<String, Object>>> trend(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String period) {
        return R.ok(indicatorService.selectTrend(name, period));
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:add')")
    @PostMapping
    public R<EconomyIndicator> add(@RequestBody EconomyIndicator indicator) {
        indicatorService.insert(indicator);
        return R.ok(indicator);
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:edit')")
    @PutMapping
    public R<EconomyIndicator> edit(@RequestBody EconomyIndicator indicator) {
        indicatorService.update(indicator);
        return R.ok(indicator);
    }

    @PreAuthorize("@ss.hasPermi('economy:indicator:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        indicatorService.deleteById(id);
        return R.ok();
    }
}
