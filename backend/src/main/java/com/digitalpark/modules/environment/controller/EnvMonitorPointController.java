package com.digitalpark.modules.environment.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.environment.entity.EnvMonitorPoint;
import com.digitalpark.modules.environment.service.EnvMonitorPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 环境监测点位Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/environment/point")
@RequiredArgsConstructor
public class EnvMonitorPointController {

    private final EnvMonitorPointService envMonitorPointService;

    @PreAuthorize("@ss.hasPermi('environment:point:list')")
    @GetMapping("/page")
    public R<PageResult<EnvMonitorPoint>> page(EnvMonitorPoint query,
                                                @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(envMonitorPointService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('environment:point:list')")
    @GetMapping("/list")
    public R<List<EnvMonitorPoint>> list(EnvMonitorPoint query) {
        return R.ok(envMonitorPointService.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('environment:point:query')")
    @GetMapping("/{id}")
    public R<EnvMonitorPoint> getInfo(@PathVariable Long id) {
        return R.ok(envMonitorPointService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('environment:point:add')")
    @PostMapping
    public R<EnvMonitorPoint> add(@RequestBody EnvMonitorPoint point) {
        envMonitorPointService.insert(point);
        return R.ok(point);
    }

    @PreAuthorize("@ss.hasPermi('environment:point:edit')")
    @PutMapping
    public R<EnvMonitorPoint> edit(@RequestBody EnvMonitorPoint point) {
        envMonitorPointService.update(point);
        return R.ok(point);
    }

    @PreAuthorize("@ss.hasPermi('environment:point:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        envMonitorPointService.deleteById(id);
        return R.ok();
    }
}
