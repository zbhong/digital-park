package com.digitalpark.modules.safety.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.safety.entity.SafetyMonitorPoint;
import com.digitalpark.modules.safety.service.SafetyMonitorPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安防监测点位Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/safety/monitor-point")
@RequiredArgsConstructor
public class SafetyMonitorPointController {

    private final SafetyMonitorPointService safetyMonitorPointService;

    @PreAuthorize("@ss.hasPermi('safety:monitorPoint:list')")
    @GetMapping("/page")
    public R<PageResult<SafetyMonitorPoint>> page(SafetyMonitorPoint query,
                                                   @RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(safetyMonitorPointService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('safety:monitorPoint:list')")
    @GetMapping("/list")
    public R<List<SafetyMonitorPoint>> list(SafetyMonitorPoint query) {
        return R.ok(safetyMonitorPointService.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('safety:monitorPoint:query')")
    @GetMapping("/{id}")
    public R<SafetyMonitorPoint> getInfo(@PathVariable Long id) {
        return R.ok(safetyMonitorPointService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('safety:monitorPoint:add')")
    @PostMapping
    public R<SafetyMonitorPoint> add(@RequestBody SafetyMonitorPoint point) {
        safetyMonitorPointService.insert(point);
        return R.ok(point);
    }

    @PreAuthorize("@ss.hasPermi('safety:monitorPoint:edit')")
    @PutMapping
    public R<SafetyMonitorPoint> edit(@RequestBody SafetyMonitorPoint point) {
        safetyMonitorPointService.update(point);
        return R.ok(point);
    }

    @PreAuthorize("@ss.hasPermi('safety:monitorPoint:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        safetyMonitorPointService.deleteById(id);
        return R.ok();
    }
}
