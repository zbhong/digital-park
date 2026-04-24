package com.digitalpark.modules.safety.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.safety.entity.SafetyNetwork;
import com.digitalpark.modules.safety.service.SafetyNetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 网络安全Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/safety/network")
@RequiredArgsConstructor
public class SafetyNetworkController {

    private final SafetyNetworkService safetyNetworkService;

    @PreAuthorize("@ss.hasPermi('safety:network:list')")
    @GetMapping("/page")
    public R<PageResult<SafetyNetwork>> page(SafetyNetwork query,
                                              @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(safetyNetworkService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('safety:network:query')")
    @GetMapping("/{id}")
    public R<SafetyNetwork> getInfo(@PathVariable Long id) {
        return R.ok(safetyNetworkService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('safety:network:add')")
    @PostMapping
    public R<SafetyNetwork> add(@RequestBody SafetyNetwork network) {
        safetyNetworkService.insert(network);
        return R.ok(network);
    }

    @PreAuthorize("@ss.hasPermi('safety:network:edit')")
    @PutMapping
    public R<SafetyNetwork> edit(@RequestBody SafetyNetwork network) {
        safetyNetworkService.update(network);
        return R.ok(network);
    }

    @PreAuthorize("@ss.hasPermi('safety:network:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        safetyNetworkService.deleteById(id);
        return R.ok();
    }
}
