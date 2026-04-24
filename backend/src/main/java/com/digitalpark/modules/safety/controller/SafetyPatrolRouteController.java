package com.digitalpark.modules.safety.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.safety.entity.SafetyPatrolRoute;
import com.digitalpark.modules.safety.service.SafetyPatrolRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 巡检路线Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/safety/patrol-route")
@RequiredArgsConstructor
public class SafetyPatrolRouteController {

    private final SafetyPatrolRouteService safetyPatrolRouteService;

    @PreAuthorize("@ss.hasPermi('safety:patrolRoute:list')")
    @GetMapping("/page")
    public R<PageResult<SafetyPatrolRoute>> page(SafetyPatrolRoute query,
                                                   @RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(safetyPatrolRouteService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('safety:patrolRoute:list')")
    @GetMapping("/list")
    public R<List<SafetyPatrolRoute>> list(SafetyPatrolRoute query) {
        return R.ok(safetyPatrolRouteService.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('safety:patrolRoute:query')")
    @GetMapping("/{id}")
    public R<SafetyPatrolRoute> getInfo(@PathVariable Long id) {
        return R.ok(safetyPatrolRouteService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('safety:patrolRoute:add')")
    @PostMapping
    public R<SafetyPatrolRoute> add(@RequestBody SafetyPatrolRoute route) {
        safetyPatrolRouteService.insert(route);
        return R.ok(route);
    }

    @PreAuthorize("@ss.hasPermi('safety:patrolRoute:edit')")
    @PutMapping
    public R<SafetyPatrolRoute> edit(@RequestBody SafetyPatrolRoute route) {
        safetyPatrolRouteService.update(route);
        return R.ok(route);
    }

    @PreAuthorize("@ss.hasPermi('safety:patrolRoute:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        safetyPatrolRouteService.deleteById(id);
        return R.ok();
    }
}
