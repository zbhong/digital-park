package com.digitalpark.modules.safety.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.safety.entity.SafetyFireEquipment;
import com.digitalpark.modules.safety.service.SafetyFireEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消防设备Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/safety/fire-equipment")
@RequiredArgsConstructor
public class SafetyFireEquipmentController {

    private final SafetyFireEquipmentService safetyFireEquipmentService;

    @PreAuthorize("@ss.hasPermi('safety:fireEquipment:list')")
    @GetMapping("/page")
    public R<PageResult<SafetyFireEquipment>> page(SafetyFireEquipment query,
                                                    @RequestParam(defaultValue = "1") int pageNum,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(safetyFireEquipmentService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('safety:fireEquipment:list')")
    @GetMapping("/list")
    public R<List<SafetyFireEquipment>> list(SafetyFireEquipment query) {
        return R.ok(safetyFireEquipmentService.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('safety:fireEquipment:query')")
    @GetMapping("/{id}")
    public R<SafetyFireEquipment> getInfo(@PathVariable Long id) {
        return R.ok(safetyFireEquipmentService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('safety:fireEquipment:add')")
    @PostMapping
    public R<SafetyFireEquipment> add(@RequestBody SafetyFireEquipment equipment) {
        safetyFireEquipmentService.insert(equipment);
        return R.ok(equipment);
    }

    @PreAuthorize("@ss.hasPermi('safety:fireEquipment:edit')")
    @PutMapping
    public R<SafetyFireEquipment> edit(@RequestBody SafetyFireEquipment equipment) {
        safetyFireEquipmentService.update(equipment);
        return R.ok(equipment);
    }

    @PreAuthorize("@ss.hasPermi('safety:fireEquipment:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        safetyFireEquipmentService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('safety:fireEquipment:list')")
    @GetMapping("/expiring")
    public R<List<SafetyFireEquipment>> expiring(@RequestParam(defaultValue = "30") int days) {
        return R.ok(safetyFireEquipmentService.selectExpiring(days));
    }
}
