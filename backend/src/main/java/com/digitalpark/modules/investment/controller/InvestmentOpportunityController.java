package com.digitalpark.modules.investment.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.investment.entity.InvestmentOpportunity;
import com.digitalpark.modules.investment.service.InvestmentOpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商机Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/investment/opportunity")
@RequiredArgsConstructor
public class InvestmentOpportunityController {

    private final InvestmentOpportunityService opportunityService;

    @PreAuthorize("@ss.hasPermi('investment:opportunity:list')")
    @GetMapping("/page")
    public R<PageResult<InvestmentOpportunity>> page(InvestmentOpportunity query,
                                                      @RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(opportunityService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('investment:opportunity:query')")
    @GetMapping("/{id}")
    public R<InvestmentOpportunity> getInfo(@PathVariable Long id) {
        return R.ok(opportunityService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('investment:opportunity:query')")
    @GetMapping("/customer/{customerId}")
    public R<List<InvestmentOpportunity>> getByCustomerId(@PathVariable Long customerId) {
        return R.ok(opportunityService.selectByCustomerId(customerId));
    }

    @PreAuthorize("@ss.hasPermi('investment:opportunity:query')")
    @GetMapping("/statistics/stage")
    public R<List<Map<String, Object>>> stageStatistics() {
        return R.ok(opportunityService.selectStageStatistics());
    }

    @PreAuthorize("@ss.hasPermi('investment:opportunity:add')")
    @PostMapping
    public R<InvestmentOpportunity> add(@RequestBody InvestmentOpportunity opportunity) {
        opportunityService.insert(opportunity);
        return R.ok(opportunity);
    }

    @PreAuthorize("@ss.hasPermi('investment:opportunity:edit')")
    @PutMapping
    public R<InvestmentOpportunity> edit(@RequestBody InvestmentOpportunity opportunity) {
        opportunityService.update(opportunity);
        return R.ok(opportunity);
    }

    @PreAuthorize("@ss.hasPermi('investment:opportunity:advance')")
    @PutMapping("/advance/{id}")
    public R<Void> advanceStage(@PathVariable Long id, @RequestParam String stage) {
        opportunityService.advanceStage(id, stage);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:opportunity:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        opportunityService.deleteById(id);
        return R.ok();
    }

}
