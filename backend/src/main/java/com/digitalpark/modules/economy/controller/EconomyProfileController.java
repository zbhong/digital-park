package com.digitalpark.modules.economy.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.economy.entity.EconomyEnterpriseProfile;
import com.digitalpark.modules.economy.service.EconomyEnterpriseProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 企业画像Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/economy/profile")
@RequiredArgsConstructor
public class EconomyProfileController {

    private final EconomyEnterpriseProfileService profileService;

    @PreAuthorize("@ss.hasPermi('economy:profile:list')")
    @GetMapping("/page")
    public R<PageResult<EconomyEnterpriseProfile>> page(EconomyEnterpriseProfile query,
                                                         @RequestParam(defaultValue = "1") int pageNum,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(profileService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:query')")
    @GetMapping("/{id}")
    public R<EconomyEnterpriseProfile> getInfo(@PathVariable Long id) {
        return R.ok(profileService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:query')")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<EconomyEnterpriseProfile> getByEnterpriseId(@PathVariable Long enterpriseId) {
        return R.ok(profileService.selectByEnterpriseId(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:query')")
    @GetMapping("/industry/{industry}")
    public R<List<EconomyEnterpriseProfile>> getByIndustry(@PathVariable String industry) {
        return R.ok(profileService.selectByIndustry(industry));
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:query')")
    @GetMapping("/statistics/industry-distribution")
    public R<List<Map<String, Object>>> industryDistribution() {
        return R.ok(profileService.selectIndustryDistribution());
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:query')")
    @GetMapping("/statistics/scale-distribution")
    public R<List<Map<String, Object>>> scaleDistribution() {
        return R.ok(profileService.selectScaleDistribution());
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:add')")
    @PostMapping
    public R<EconomyEnterpriseProfile> add(@RequestBody EconomyEnterpriseProfile profile) {
        profileService.insert(profile);
        return R.ok(profile);
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:edit')")
    @PutMapping
    public R<EconomyEnterpriseProfile> edit(@RequestBody EconomyEnterpriseProfile profile) {
        profileService.update(profile);
        return R.ok(profile);
    }

    @PreAuthorize("@ss.hasPermi('economy:profile:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        profileService.deleteById(id);
        return R.ok();
    }
}
