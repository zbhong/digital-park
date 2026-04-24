package com.digitalpark.modules.property.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.property.entity.PropertyFee;
import com.digitalpark.modules.property.service.PropertyFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 物业费Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/property/fee")
@RequiredArgsConstructor
public class PropertyFeeController {

    private final PropertyFeeService feeService;

    @PreAuthorize("@ss.hasPermi('property:fee:list')")
    @GetMapping("/page")
    public R<PageResult<PropertyFee>> page(PropertyFee query,
                                            @RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(feeService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('property:fee:query')")
    @GetMapping("/{id}")
    public R<PropertyFee> getInfo(@PathVariable Long id) {
        return R.ok(feeService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('property:fee:query')")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<List<PropertyFee>> getByEnterpriseId(@PathVariable Long enterpriseId) {
        return R.ok(feeService.selectByEnterpriseId(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('property:fee:query')")
    @GetMapping("/unpaid/{enterpriseId}")
    public R<BigDecimal> getUnpaidTotal(@PathVariable Long enterpriseId) {
        return R.ok(feeService.selectTotalUnpaid(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('property:fee:query')")
    @GetMapping("/statistics")
    public R<List<Map<String, Object>>> statistics() {
        return R.ok(feeService.selectFeeStatistics());
    }

    @PreAuthorize("@ss.hasPermi('property:fee:query')")
    @GetMapping("/statistics/type")
    public R<List<Map<String, Object>>> typeStatistics() {
        return R.ok(feeService.selectFeeTypeStatistics());
    }

    @PreAuthorize("@ss.hasPermi('property:fee:add')")
    @PostMapping
    public R<PropertyFee> add(@RequestBody PropertyFee fee) {
        feeService.insert(fee);
        return R.ok(fee);
    }

    @PreAuthorize("@ss.hasPermi('property:fee:pay')")
    @PutMapping("/pay/{id}")
    public R<Void> pay(@PathVariable Long id) {
        feeService.pay(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:fee:pay')")
    @PutMapping("/batch-pay")
    public R<Void> batchPay(@RequestBody List<Long> ids) {
        feeService.batchPay(ids);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:fee:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        feeService.deleteById(id);
        return R.ok();
    }
}
