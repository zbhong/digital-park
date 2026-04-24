package com.digitalpark.modules.investment.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.investment.entity.InvestmentPolicy;
import com.digitalpark.modules.investment.service.InvestmentPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 招商政策Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/investment/policy")
@RequiredArgsConstructor
public class InvestmentPolicyController {

    private final InvestmentPolicyService policyService;

    @PreAuthorize("@ss.hasPermi('investment:policy:list')")
    @GetMapping("/page")
    public R<PageResult<InvestmentPolicy>> page(InvestmentPolicy query,
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(policyService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:query')")
    @GetMapping("/{id}")
    public R<InvestmentPolicy> getInfo(@PathVariable Long id) {
        return R.ok(policyService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:query')")
    @GetMapping("/enabled")
    public R<List<InvestmentPolicy>> getEnabled() {
        return R.ok(policyService.selectEnabled());
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:query')")
    @GetMapping("/type/{type}")
    public R<List<InvestmentPolicy>> getByType(@PathVariable String type) {
        return R.ok(policyService.selectByType(type));
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:query')")
    @GetMapping("/match")
    public R<List<InvestmentPolicy>> matchPolicies(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String scale) {
        return R.ok(policyService.matchPolicies(industry, scale));
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:add')")
    @PostMapping
    public R<InvestmentPolicy> add(@RequestBody InvestmentPolicy policy) {
        policyService.insert(policy);
        return R.ok(policy);
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:edit')")
    @PutMapping
    public R<InvestmentPolicy> edit(@RequestBody InvestmentPolicy policy) {
        policyService.update(policy);
        return R.ok(policy);
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:enable')")
    @PutMapping("/enable/{id}")
    public R<Void> enable(@PathVariable Long id) {
        policyService.enable(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:disable')")
    @PutMapping("/disable/{id}")
    public R<Void> disable(@PathVariable Long id) {
        policyService.disable(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:policy:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        policyService.deleteById(id);
        return R.ok();
    }
}
