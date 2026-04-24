package com.digitalpark.modules.safety.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.safety.entity.SafetyProduction;
import com.digitalpark.modules.safety.service.SafetyProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 生产安全Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/safety/production")
@RequiredArgsConstructor
public class SafetyProductionController {

    private final SafetyProductionService safetyProductionService;

    @PreAuthorize("@ss.hasPermi('safety:production:list')")
    @GetMapping("/page")
    public R<PageResult<SafetyProduction>> page(SafetyProduction query,
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(safetyProductionService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('safety:production:query')")
    @GetMapping("/{id}")
    public R<SafetyProduction> getInfo(@PathVariable Long id) {
        return R.ok(safetyProductionService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('safety:production:add')")
    @PostMapping
    public R<SafetyProduction> add(@RequestBody SafetyProduction production) {
        safetyProductionService.insert(production);
        return R.ok(production);
    }

    @PreAuthorize("@ss.hasPermi('safety:production:edit')")
    @PutMapping
    public R<SafetyProduction> edit(@RequestBody SafetyProduction production) {
        safetyProductionService.update(production);
        return R.ok(production);
    }

    @PreAuthorize("@ss.hasPermi('safety:production:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        safetyProductionService.deleteById(id);
        return R.ok();
    }
}
