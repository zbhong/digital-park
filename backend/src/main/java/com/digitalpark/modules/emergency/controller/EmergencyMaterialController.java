package com.digitalpark.modules.emergency.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.emergency.entity.EmergencyMaterial;
import com.digitalpark.modules.emergency.service.EmergencyMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急物资Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/emergency/material")
@RequiredArgsConstructor
public class EmergencyMaterialController {

    private final EmergencyMaterialService emergencyMaterialService;

    @PreAuthorize("@ss.hasPermi('emergency:material:list')")
    @GetMapping("/page")
    public R<PageResult<EmergencyMaterial>> page(EmergencyMaterial query,
                                                   @RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(emergencyMaterialService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('emergency:material:list')")
    @GetMapping("/list")
    public R<List<EmergencyMaterial>> list(EmergencyMaterial query) {
        return R.ok(emergencyMaterialService.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('emergency:material:query')")
    @GetMapping("/{id}")
    public R<EmergencyMaterial> getInfo(@PathVariable Long id) {
        return R.ok(emergencyMaterialService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('emergency:material:add')")
    @PostMapping
    public R<EmergencyMaterial> add(@RequestBody EmergencyMaterial material) {
        emergencyMaterialService.insert(material);
        return R.ok(material);
    }

    @PreAuthorize("@ss.hasPermi('emergency:material:edit')")
    @PutMapping
    public R<EmergencyMaterial> edit(@RequestBody EmergencyMaterial material) {
        emergencyMaterialService.update(material);
        return R.ok(material);
    }

    @PreAuthorize("@ss.hasPermi('emergency:material:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        emergencyMaterialService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('emergency:material:list')")
    @GetMapping("/low-stock")
    public R<List<EmergencyMaterial>> lowStock(@RequestParam(defaultValue = "10") int threshold) {
        return R.ok(emergencyMaterialService.selectLowStock(threshold));
    }
}
