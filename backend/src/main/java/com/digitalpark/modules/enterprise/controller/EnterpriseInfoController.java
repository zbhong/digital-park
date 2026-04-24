package com.digitalpark.modules.enterprise.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.enterprise.entity.EnterpriseInfo;
import com.digitalpark.modules.enterprise.service.EnterpriseInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 企业信息Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/enterprise/info")
@RequiredArgsConstructor
public class EnterpriseInfoController {

    private final EnterpriseInfoService infoService;

    @PreAuthorize("@ss.hasPermi('enterprise:info:list')")
    @GetMapping("/page")
    public R<PageResult<EnterpriseInfo>> page(EnterpriseInfo query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(infoService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:query')")
    @GetMapping("/{id}")
    public R<EnterpriseInfo> getInfo(@PathVariable Long id) {
        return R.ok(infoService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:query')")
    @GetMapping("/area/{areaId}")
    public R<List<EnterpriseInfo>> getByAreaId(@PathVariable Long areaId) {
        return R.ok(infoService.selectByAreaId(areaId));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:query')")
    @GetMapping("/building/{buildingId}")
    public R<List<EnterpriseInfo>> getByBuildingId(@PathVariable Long buildingId) {
        return R.ok(infoService.selectByBuildingId(buildingId));
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:query')")
    @GetMapping("/statistics/industry")
    public R<List<Map<String, Object>>> industryStatistics() {
        return R.ok(infoService.selectIndustryStatistics());
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:query')")
    @GetMapping("/statistics/status")
    public R<List<Map<String, Object>>> statusStatistics() {
        return R.ok(infoService.selectStatusStatistics());
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:add')")
    @PostMapping
    public R<EnterpriseInfo> add(@RequestBody EnterpriseInfo info) {
        infoService.insert(info);
        return R.ok(info);
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:edit')")
    @PutMapping
    public R<EnterpriseInfo> edit(@RequestBody EnterpriseInfo info) {
        infoService.update(info);
        return R.ok(info);
    }

    @PreAuthorize("@ss.hasPermi('enterprise:info:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        infoService.deleteById(id);
        return R.ok();
    }
}
