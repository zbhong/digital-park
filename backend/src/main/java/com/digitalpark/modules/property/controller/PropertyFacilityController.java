package com.digitalpark.modules.property.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.property.entity.PropertyFacility;
import com.digitalpark.modules.property.service.PropertyFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设施运维Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/property/facility")
@RequiredArgsConstructor
public class PropertyFacilityController {

    private final PropertyFacilityService facilityService;

    @PreAuthorize("@ss.hasPermi('property:facility:list')")
    @GetMapping("/page")
    public R<PageResult<PropertyFacility>> page(PropertyFacility query,
                                                @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(facilityService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('property:facility:query')")
    @GetMapping("/{id}")
    public R<PropertyFacility> getInfo(@PathVariable Long id) {
        return R.ok(facilityService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('property:facility:query')")
    @GetMapping("/type/{type}")
    public R<List<PropertyFacility>> getByType(@PathVariable String type) {
        return R.ok(facilityService.selectByType(type));
    }

    @PreAuthorize("@ss.hasPermi('property:facility:query')")
    @GetMapping("/area/{areaId}")
    public R<List<PropertyFacility>> getByAreaId(@PathVariable Long areaId) {
        return R.ok(facilityService.selectByAreaId(areaId));
    }

    @PreAuthorize("@ss.hasPermi('property:facility:query')")
    @GetMapping("/check-reminders")
    public R<List<PropertyFacility>> checkReminders() {
        return R.ok(facilityService.selectCheckReminders());
    }

    @PreAuthorize("@ss.hasPermi('property:facility:add')")
    @PostMapping
    public R<PropertyFacility> add(@RequestBody PropertyFacility facility) {
        facilityService.insert(facility);
        return R.ok(facility);
    }

    @PreAuthorize("@ss.hasPermi('property:facility:edit')")
    @PutMapping
    public R<PropertyFacility> edit(@RequestBody PropertyFacility facility) {
        facilityService.update(facility);
        return R.ok(facility);
    }

    @PreAuthorize("@ss.hasPermi('property:facility:check')")
    @PutMapping("/check/{id}")
    public R<Void> check(@PathVariable Long id) {
        facilityService.check(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:facility:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        facilityService.deleteById(id);
        return R.ok();
    }
}
