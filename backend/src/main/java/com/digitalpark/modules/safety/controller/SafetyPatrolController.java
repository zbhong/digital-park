package com.digitalpark.modules.safety.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.safety.entity.SafetyPatrol;
import com.digitalpark.modules.safety.service.SafetyPatrolService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 安全巡检Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/safety/patrol")
@RequiredArgsConstructor
public class SafetyPatrolController {

    private final SafetyPatrolService safetyPatrolService;

    @PreAuthorize("@ss.hasPermi('safety:patrol:list')")
    @GetMapping("/page")
    public R<PageResult<SafetyPatrol>> page(SafetyPatrol query,
                                             @RequestParam(defaultValue = "1") int pageNum,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(safetyPatrolService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('safety:patrol:query')")
    @GetMapping("/{id}")
    public R<SafetyPatrol> getInfo(@PathVariable Long id) {
        return R.ok(safetyPatrolService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('safety:patrol:add')")
    @PostMapping
    public R<SafetyPatrol> add(@RequestBody SafetyPatrol patrol) {
        safetyPatrolService.insert(patrol);
        return R.ok(patrol);
    }

    @PreAuthorize("@ss.hasPermi('safety:patrol:edit')")
    @PutMapping
    public R<SafetyPatrol> edit(@RequestBody SafetyPatrol patrol) {
        safetyPatrolService.update(patrol);
        return R.ok(patrol);
    }

    @PreAuthorize("@ss.hasPermi('safety:patrol:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        safetyPatrolService.deleteById(id);
        return R.ok();
    }
}
