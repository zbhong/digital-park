package com.digitalpark.modules.enterprise.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.enterprise.entity.EnterprisePolicy;
import com.digitalpark.modules.enterprise.service.EnterprisePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 政策信息Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/enterprise/policy")
@RequiredArgsConstructor
public class EnterprisePolicyController {

    private final EnterprisePolicyService policyService;

    @PreAuthorize("@ss.hasPermi('enterprise:policy:list')")
    @GetMapping("/page")
    public R<PageResult<EnterprisePolicy>> page(EnterprisePolicy query,
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(policyService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:query')")
    @GetMapping("/{id}")
    public R<EnterprisePolicy> getInfo(@PathVariable Long id) {
        return R.ok(policyService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:query')")
    @GetMapping("/published")
    public R<List<EnterprisePolicy>> getPublished() {
        return R.ok(policyService.selectPublished());
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:query')")
    @GetMapping("/type/{type}")
    public R<List<EnterprisePolicy>> getByType(@PathVariable String type) {
        return R.ok(policyService.selectByType(type));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:add')")
    @PostMapping
    public R<EnterprisePolicy> add(@RequestBody EnterprisePolicy policy) {
        policyService.insert(policy);
        return R.ok(policy);
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:edit')")
    @PutMapping
    public R<EnterprisePolicy> edit(@RequestBody EnterprisePolicy policy) {
        policyService.update(policy);
        return R.ok(policy);
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:publish')")
    @PutMapping("/publish/{id}")
    public R<Void> publish(@PathVariable Long id) {
        policyService.publish(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:expire')")
    @PutMapping("/expire/{id}")
    public R<Void> expire(@PathVariable Long id) {
        policyService.expire(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('enterprise:policy:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        policyService.deleteById(id);
        return R.ok();
    }
}
