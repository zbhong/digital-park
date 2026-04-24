package com.digitalpark.modules.enterprise.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.enterprise.entity.EnterpriseServiceRequest;
import com.digitalpark.modules.enterprise.service.EnterpriseServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务请求Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/enterprise/service")
@RequiredArgsConstructor
public class EnterpriseServiceController {

    private final EnterpriseServiceRequestService serviceRequestService;

    @PreAuthorize("@ss.hasPermi('enterprise:service:list')")
    @GetMapping("/page")
    public R<PageResult<EnterpriseServiceRequest>> page(EnterpriseServiceRequest query,
                                                         @RequestParam(defaultValue = "1") int pageNum,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(serviceRequestService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:query')")
    @GetMapping("/{id}")
    public R<EnterpriseServiceRequest> getInfo(@PathVariable Long id) {
        return R.ok(serviceRequestService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:query')")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<List<EnterpriseServiceRequest>> getByEnterpriseId(@PathVariable Long enterpriseId) {
        return R.ok(serviceRequestService.selectByEnterpriseId(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:query')")
    @GetMapping("/statistics/type")
    public R<List<Map<String, Object>>> typeStatistics() {
        return R.ok(serviceRequestService.selectTypeStatistics());
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:query')")
    @GetMapping("/statistics/status")
    public R<List<Map<String, Object>>> statusStatistics() {
        return R.ok(serviceRequestService.selectStatusStatistics());
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:add')")
    @PostMapping
    public R<EnterpriseServiceRequest> add(@RequestBody EnterpriseServiceRequest request) {
        serviceRequestService.insert(request);
        return R.ok(request);
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:handle')")
    @PutMapping("/handle/{id}")
    public R<Void> handle(@PathVariable Long id,
                          @RequestParam Long handlerId,
                          @RequestParam String handleResult) {
        serviceRequestService.handle(id, handlerId, handleResult);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:close')")
    @PutMapping("/close/{id}")
    public R<Void> close(@PathVariable Long id) {
        serviceRequestService.close(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('enterprise:service:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        serviceRequestService.deleteById(id);
        return R.ok();
    }

    /**
     * 服务请求综合统计
     */
    @PreAuthorize("@ss.hasPermi('enterprise:service:query')")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("typeStatistics", serviceRequestService.selectTypeStatistics());
        result.put("statusStatistics", serviceRequestService.selectStatusStatistics());
        return R.ok(result);
    }
}
