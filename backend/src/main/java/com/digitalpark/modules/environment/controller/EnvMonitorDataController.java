package com.digitalpark.modules.environment.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.environment.dto.EnvMonitorDataDTO;
import com.digitalpark.modules.environment.entity.EnvMonitorData;
import com.digitalpark.modules.environment.service.EnvMonitorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 环境监测数据Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/environment/data")
@RequiredArgsConstructor
public class EnvMonitorDataController {

    private final EnvMonitorDataService envMonitorDataService;

    @PreAuthorize("@ss.hasPermi('environment:data:list')")
    @GetMapping("/page")
    public R<PageResult<EnvMonitorData>> page(EnvMonitorData query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(envMonitorDataService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('environment:data:query')")
    @GetMapping("/{id}")
    public R<EnvMonitorData> getInfo(@PathVariable Long id) {
        return R.ok(envMonitorDataService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('environment:data:add')")
    @PostMapping
    public R<EnvMonitorData> add(@RequestBody EnvMonitorData data) {
        envMonitorDataService.insert(data);
        return R.ok(data);
    }

    @PreAuthorize("@ss.hasPermi('environment:data:add')")
    @PostMapping("/batch")
    public R<Void> batchAdd(@RequestBody List<EnvMonitorData> dataList) {
        envMonitorDataService.batchInsert(dataList);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('environment:data:edit')")
    @PutMapping
    public R<EnvMonitorData> edit(@RequestBody EnvMonitorData data) {
        envMonitorDataService.update(data);
        return R.ok(data);
    }

    @PreAuthorize("@ss.hasPermi('environment:data:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        envMonitorDataService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('environment:data:statistics')")
    @GetMapping("/statistics/time")
    public R<Map<String, Object>> statisticsByTime(EnvMonitorDataDTO dto) {
        return R.ok(envMonitorDataService.selectStatisticsByTime(dto));
    }

    @PreAuthorize("@ss.hasPermi('environment:data:statistics')")
    @GetMapping("/statistics/area")
    public R<Map<String, Object>> statisticsByArea(EnvMonitorDataDTO dto) {
        return R.ok(envMonitorDataService.selectStatisticsByArea(dto));
    }

    @PreAuthorize("@ss.hasPermi('environment:data:statistics')")
    @GetMapping("/statistics/indicator")
    public R<Map<String, Object>> statisticsByIndicator(EnvMonitorDataDTO dto) {
        return R.ok(envMonitorDataService.selectStatisticsByIndicator(dto));
    }

    @PreAuthorize("@ss.hasPermi('environment:data:warning')")
    @GetMapping("/warnings")
    public R<List<Map<String, Object>>> overStandardWarnings(EnvMonitorDataDTO dto) {
        return R.ok(envMonitorDataService.selectOverStandardWarnings(dto));
    }

    @PreAuthorize("@ss.hasPermi('environment:data:analysis')")
    @GetMapping("/pollution-source")
    public R<Map<String, Object>> pollutionSourceAnalysis(EnvMonitorDataDTO dto) {
        return R.ok(envMonitorDataService.selectPollutionSourceAnalysis(dto));
    }
}
