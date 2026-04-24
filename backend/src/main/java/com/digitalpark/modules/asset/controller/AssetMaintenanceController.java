package com.digitalpark.modules.asset.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.asset.entity.AssetMaintenance;
import com.digitalpark.modules.asset.service.AssetMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产维保Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/asset/maintenance")
@RequiredArgsConstructor
public class AssetMaintenanceController {

    private final AssetMaintenanceService assetMaintenanceService;

    @PreAuthorize("@ss.hasPermi('asset:maintenance:list')")
    @GetMapping("/page")
    public R<PageResult<AssetMaintenance>> page(AssetMaintenance query,
                                                 @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(assetMaintenanceService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('asset:maintenance:list')")
    @GetMapping("/asset/{assetId}")
    public R<List<AssetMaintenance>> listByAssetId(@PathVariable Long assetId) {
        return R.ok(assetMaintenanceService.selectListByAssetId(assetId));
    }

    @PreAuthorize("@ss.hasPermi('asset:maintenance:query')")
    @GetMapping("/{id}")
    public R<AssetMaintenance> getInfo(@PathVariable Long id) {
        return R.ok(assetMaintenanceService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('asset:maintenance:add')")
    @PostMapping
    public R<AssetMaintenance> add(@RequestBody AssetMaintenance maintenance) {
        assetMaintenanceService.insert(maintenance);
        return R.ok(maintenance);
    }

    @PreAuthorize("@ss.hasPermi('asset:maintenance:edit')")
    @PutMapping
    public R<AssetMaintenance> edit(@RequestBody AssetMaintenance maintenance) {
        assetMaintenanceService.update(maintenance);
        return R.ok(maintenance);
    }

    @PreAuthorize("@ss.hasPermi('asset:maintenance:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        assetMaintenanceService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('asset:maintenance:list')")
    @GetMapping("/expiring")
    public R<List<AssetMaintenance>> expiringSoon(
            @RequestParam(defaultValue = "7") int days) {
        return R.ok(assetMaintenanceService.selectExpiringSoon(days));
    }
}
