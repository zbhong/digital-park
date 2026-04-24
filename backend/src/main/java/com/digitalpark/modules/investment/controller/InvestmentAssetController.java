package com.digitalpark.modules.investment.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.investment.entity.InvestmentAsset;
import com.digitalpark.modules.investment.service.InvestmentAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 招商房源Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/investment/asset")
@RequiredArgsConstructor
public class InvestmentAssetController {

    private final InvestmentAssetService assetService;

    @PreAuthorize("@ss.hasPermi('investment:asset:list')")
    @GetMapping("/page")
    public R<PageResult<InvestmentAsset>> page(InvestmentAsset query,
                                                @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(assetService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:query')")
    @GetMapping("/{id}")
    public R<InvestmentAsset> getInfo(@PathVariable Long id) {
        return R.ok(assetService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:query')")
    @GetMapping("/building/{buildingId}")
    public R<List<InvestmentAsset>> getByBuildingId(@PathVariable Long buildingId) {
        return R.ok(assetService.selectByBuildingId(buildingId));
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:query')")
    @GetMapping("/available")
    public R<List<InvestmentAsset>> getAvailable() {
        return R.ok(assetService.selectAvailable());
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:query')")
    @GetMapping("/rent-control")
    public R<List<Map<String, Object>>> rentControlData() {
        return R.ok(assetService.selectRentControlData());
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:query')")
    @GetMapping("/available-area")
    public R<BigDecimal> availableArea() {
        return R.ok(assetService.selectTotalAvailableArea());
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:add')")
    @PostMapping
    public R<InvestmentAsset> add(@RequestBody InvestmentAsset asset) {
        assetService.insert(asset);
        return R.ok(asset);
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:edit')")
    @PutMapping
    public R<InvestmentAsset> edit(@RequestBody InvestmentAsset asset) {
        assetService.update(asset);
        return R.ok(asset);
    }

    @PreAuthorize("@ss.hasPermi('investment:asset:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        assetService.deleteById(id);
        return R.ok();
    }

    /**
     * 租控状态看板数据（各状态房源数量）
     */
    @PreAuthorize("@ss.hasPermi('investment:asset:query')")
    @GetMapping("/rental-status")
    public R<Map<String, Object>> rentalStatus() {
        List<Map<String, Object>> rentControlData = assetService.selectRentControlData();
        Map<String, Object> result = rentControlData.isEmpty() ? new HashMap<>() : rentControlData.get(0);
        return R.ok(result);
    }
}
