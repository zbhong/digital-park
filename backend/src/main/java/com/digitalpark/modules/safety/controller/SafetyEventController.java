package com.digitalpark.modules.safety.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.safety.entity.SafetyEvent;
import com.digitalpark.modules.safety.service.SafetyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 安全事件Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/safety/event")
@RequiredArgsConstructor
public class SafetyEventController {

    private final SafetyEventService safetyEventService;

    @PreAuthorize("@ss.hasPermi('safety:event:list')")
    @GetMapping("/page")
    public R<PageResult<SafetyEvent>> page(SafetyEvent query,
                                            @RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(safetyEventService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('safety:event:query')")
    @GetMapping("/{id}")
    public R<SafetyEvent> getInfo(@PathVariable Long id) {
        return R.ok(safetyEventService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('safety:event:add')")
    @PostMapping
    public R<SafetyEvent> add(@RequestBody SafetyEvent event) {
        safetyEventService.insert(event);
        return R.ok(event);
    }

    @PreAuthorize("@ss.hasPermi('safety:event:edit')")
    @PutMapping
    public R<SafetyEvent> edit(@RequestBody SafetyEvent event) {
        safetyEventService.update(event);
        return R.ok(event);
    }

    @PreAuthorize("@ss.hasPermi('safety:event:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        safetyEventService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('safety:event:handle')")
    @PutMapping("/{id}/handle")
    public R<Void> handle(@PathVariable Long id, @RequestBody Map<String, String> params) {
        safetyEventService.handle(id, params.get("handleResult"));
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('safety:event:statistics')")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        return R.ok(safetyEventService.selectStatistics());
    }
}
