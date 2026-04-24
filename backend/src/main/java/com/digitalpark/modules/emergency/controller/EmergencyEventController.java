package com.digitalpark.modules.emergency.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.emergency.entity.EmergencyEvent;
import com.digitalpark.modules.emergency.service.EmergencyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 应急事件Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/emergency/event")
@RequiredArgsConstructor
public class EmergencyEventController {

    private final EmergencyEventService emergencyEventService;

    @PreAuthorize("@ss.hasPermi('emergency:event:list')")
    @GetMapping("/page")
    public R<PageResult<EmergencyEvent>> page(EmergencyEvent query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(emergencyEventService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('emergency:event:query')")
    @GetMapping("/{id}")
    public R<EmergencyEvent> getInfo(@PathVariable Long id) {
        return R.ok(emergencyEventService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('emergency:event:add')")
    @PostMapping
    public R<EmergencyEvent> add(@RequestBody EmergencyEvent event) {
        emergencyEventService.insert(event);
        return R.ok(event);
    }

    @PreAuthorize("@ss.hasPermi('emergency:event:edit')")
    @PutMapping
    public R<EmergencyEvent> edit(@RequestBody EmergencyEvent event) {
        emergencyEventService.update(event);
        return R.ok(event);
    }

    @PreAuthorize("@ss.hasPermi('emergency:event:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        emergencyEventService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('emergency:event:handle')")
    @PutMapping("/{id}/handle")
    public R<Void> handle(@PathVariable Long id, @RequestBody Map<String, String> params) {
        emergencyEventService.handle(id, params.get("handleResult"));
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('emergency:event:statistics')")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        return R.ok(emergencyEventService.selectStatistics());
    }
}
