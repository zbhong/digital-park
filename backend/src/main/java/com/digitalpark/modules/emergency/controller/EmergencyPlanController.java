package com.digitalpark.modules.emergency.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.emergency.entity.EmergencyPlan;
import com.digitalpark.modules.emergency.service.EmergencyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 应急预案Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/emergency/plan")
@RequiredArgsConstructor
public class EmergencyPlanController {

    private final EmergencyPlanService emergencyPlanService;

    @PreAuthorize("@ss.hasPermi('emergency:plan:list')")
    @GetMapping("/page")
    public R<PageResult<EmergencyPlan>> page(EmergencyPlan query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(emergencyPlanService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('emergency:plan:query')")
    @GetMapping("/{id}")
    public R<EmergencyPlan> getInfo(@PathVariable Long id) {
        return R.ok(emergencyPlanService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('emergency:plan:add')")
    @PostMapping
    public R<EmergencyPlan> add(@RequestBody EmergencyPlan plan) {
        emergencyPlanService.insert(plan);
        return R.ok(plan);
    }

    @PreAuthorize("@ss.hasPermi('emergency:plan:edit')")
    @PutMapping
    public R<EmergencyPlan> edit(@RequestBody EmergencyPlan plan) {
        emergencyPlanService.update(plan);
        return R.ok(plan);
    }

    @PreAuthorize("@ss.hasPermi('emergency:plan:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        emergencyPlanService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('emergency:plan:publish')")
    @PutMapping("/{id}/publish")
    public R<Void> publish(@PathVariable Long id) {
        emergencyPlanService.publish(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('emergency:plan:revoke')")
    @PutMapping("/{id}/revoke")
    public R<Void> revoke(@PathVariable Long id) {
        emergencyPlanService.revoke(id);
        return R.ok();
    }
}
