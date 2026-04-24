package com.digitalpark.modules.investment.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.investment.entity.InvestmentContract;
import com.digitalpark.modules.investment.service.InvestmentContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 合同Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/investment/contract")
@RequiredArgsConstructor
public class InvestmentContractController {

    private final InvestmentContractService contractService;

    @PreAuthorize("@ss.hasPermi('investment:contract:list')")
    @GetMapping("/page")
    public R<PageResult<InvestmentContract>> page(InvestmentContract query,
                                                   @RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(contractService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:query')")
    @GetMapping("/{id}")
    public R<InvestmentContract> getInfo(@PathVariable Long id) {
        return R.ok(contractService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:query')")
    @GetMapping("/no/{contractNo}")
    public R<InvestmentContract> getByContractNo(@PathVariable String contractNo) {
        return R.ok(contractService.selectByContractNo(contractNo));
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:query')")
    @GetMapping("/statistics/status")
    public R<List<Map<String, Object>>> statusStatistics() {
        return R.ok(contractService.selectStatusStatistics());
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:add')")
    @PostMapping
    public R<InvestmentContract> add(@RequestBody InvestmentContract contract) {
        contractService.insert(contract);
        return R.ok(contract);
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:edit')")
    @PutMapping
    public R<InvestmentContract> edit(@RequestBody InvestmentContract contract) {
        contractService.update(contract);
        return R.ok(contract);
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:submit')")
    @PutMapping("/submit/{id}")
    public R<Void> submitForApproval(@PathVariable Long id) {
        contractService.submitForApproval(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:approve')")
    @PutMapping("/approve/{id}")
    public R<Void> approve(@PathVariable Long id) {
        contractService.approve(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:reject')")
    @PutMapping("/reject/{id}")
    public R<Void> reject(@PathVariable Long id) {
        contractService.reject(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:terminate')")
    @PutMapping("/terminate/{id}")
    public R<Void> terminate(@PathVariable Long id) {
        contractService.terminate(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:contract:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        contractService.deleteById(id);
        return R.ok();
    }
}
