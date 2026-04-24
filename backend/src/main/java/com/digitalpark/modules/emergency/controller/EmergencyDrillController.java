package com.digitalpark.modules.emergency.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.emergency.entity.EmergencyDrill;
import com.digitalpark.modules.emergency.service.EmergencyDrillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 应急演练Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/emergency/drill")
@RequiredArgsConstructor
public class EmergencyDrillController {

    private final EmergencyDrillService emergencyDrillService;

    @PreAuthorize("@ss.hasPermi('emergency:drill:list')")
    @GetMapping("/page")
    public R<PageResult<EmergencyDrill>> page(EmergencyDrill query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(emergencyDrillService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('emergency:drill:query')")
    @GetMapping("/{id}")
    public R<EmergencyDrill> getInfo(@PathVariable Long id) {
        return R.ok(emergencyDrillService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('emergency:drill:add')")
    @PostMapping
    public R<EmergencyDrill> add(@RequestBody EmergencyDrill drill) {
        emergencyDrillService.insert(drill);
        return R.ok(drill);
    }

    @PreAuthorize("@ss.hasPermi('emergency:drill:edit')")
    @PutMapping
    public R<EmergencyDrill> edit(@RequestBody EmergencyDrill drill) {
        emergencyDrillService.update(drill);
        return R.ok(drill);
    }

    @PreAuthorize("@ss.hasPermi('emergency:drill:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        emergencyDrillService.deleteById(id);
        return R.ok();
    }
}
