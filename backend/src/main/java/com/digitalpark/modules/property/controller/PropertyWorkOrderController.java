package com.digitalpark.modules.property.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.property.entity.PropertyWorkOrder;
import com.digitalpark.modules.property.service.PropertyWorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物业工单Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/property/work-order")
@RequiredArgsConstructor
public class PropertyWorkOrderController {

    private final PropertyWorkOrderService workOrderService;

    @PreAuthorize("@ss.hasPermi('property:workOrder:list')")
    @GetMapping("/page")
    public R<PageResult<PropertyWorkOrder>> page(PropertyWorkOrder query,
                                                  @RequestParam(defaultValue = "1") int pageNum,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(workOrderService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:query')")
    @GetMapping("/{id}")
    public R<PropertyWorkOrder> getInfo(@PathVariable Long id) {
        return R.ok(workOrderService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:query')")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<List<PropertyWorkOrder>> getByEnterpriseId(@PathVariable Long enterpriseId) {
        return R.ok(workOrderService.selectByEnterpriseId(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:query')")
    @GetMapping("/statistics/type")
    public R<List<Map<String, Object>>> typeStatistics() {
        return R.ok(workOrderService.selectTypeStatistics());
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:query')")
    @GetMapping("/statistics/status")
    public R<List<Map<String, Object>>> statusStatistics() {
        return R.ok(workOrderService.selectStatusStatistics());
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:query')")
    @GetMapping("/statistics/priority")
    public R<List<Map<String, Object>>> priorityStatistics() {
        return R.ok(workOrderService.selectPriorityStatistics());
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:add')")
    @PostMapping
    public R<PropertyWorkOrder> add(@RequestBody PropertyWorkOrder order) {
        workOrderService.insert(order);
        return R.ok(order);
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:dispatch')")
    @PutMapping("/dispatch/{id}")
    public R<Void> dispatch(@PathVariable Long id, @RequestParam Long handlerId) {
        workOrderService.dispatch(id, handlerId);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:handle')")
    @PutMapping("/handle/{id}")
    public R<Void> handle(@PathVariable Long id, @RequestParam String handleResult) {
        workOrderService.handle(id, handleResult);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:complete')")
    @PutMapping("/complete/{id}")
    public R<Void> complete(@PathVariable Long id) {
        workOrderService.complete(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:evaluate')")
    @PutMapping("/evaluate/{id}")
    public R<Void> evaluate(@PathVariable Long id, @RequestParam Integer satisfaction) {
        workOrderService.evaluate(id, satisfaction);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:workOrder:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        workOrderService.deleteById(id);
        return R.ok();
    }

    /**
     * 工单综合统计（按类型/状态/优先级）
     */
    @PreAuthorize("@ss.hasPermi('property:workOrder:query')")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("typeStatistics", workOrderService.selectTypeStatistics());
        result.put("statusStatistics", workOrderService.selectStatusStatistics());
        result.put("priorityStatistics", workOrderService.selectPriorityStatistics());
        return R.ok(result);
    }
}
