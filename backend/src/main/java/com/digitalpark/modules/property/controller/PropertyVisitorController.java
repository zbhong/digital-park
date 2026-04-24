package com.digitalpark.modules.property.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.property.entity.PropertyVisitor;
import com.digitalpark.modules.property.service.PropertyVisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 访客Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/property/visitor")
@RequiredArgsConstructor
public class PropertyVisitorController {

    private final PropertyVisitorService visitorService;

    @PreAuthorize("@ss.hasPermi('property:visitor:list')")
    @GetMapping("/page")
    public R<PageResult<PropertyVisitor>> page(PropertyVisitor query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(visitorService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('property:visitor:query')")
    @GetMapping("/{id}")
    public R<PropertyVisitor> getInfo(@PathVariable Long id) {
        return R.ok(visitorService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('property:visitor:add')")
    @PostMapping
    public R<PropertyVisitor> add(@RequestBody PropertyVisitor visitor) {
        visitorService.insert(visitor);
        return R.ok(visitor);
    }

    @PreAuthorize("@ss.hasPermi('property:visitor:approve')")
    @PutMapping("/approve/{id}")
    public R<Void> approve(@PathVariable Long id) {
        visitorService.approve(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:visitor:reject')")
    @PutMapping("/reject/{id}")
    public R<Void> reject(@PathVariable Long id) {
        visitorService.reject(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:visitor:leave')")
    @PutMapping("/leave/{id}")
    public R<Void> leave(@PathVariable Long id) {
        visitorService.leave(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:visitor:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        visitorService.deleteById(id);
        return R.ok();
    }
}
