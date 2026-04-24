package com.digitalpark.modules.investment.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.investment.entity.InvestmentCustomer;
import com.digitalpark.modules.investment.entity.InvestmentFollow;
import com.digitalpark.modules.investment.service.InvestmentCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客户Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/investment/customer")
@RequiredArgsConstructor
public class InvestmentCustomerController {

    private final InvestmentCustomerService customerService;

    @PreAuthorize("@ss.hasPermi('investment:customer:list')")
    @GetMapping("/page")
    public R<PageResult<InvestmentCustomer>> page(InvestmentCustomer query,
                                                   @RequestParam(defaultValue = "1") int pageNum,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(customerService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:query')")
    @GetMapping("/{id}")
    public R<InvestmentCustomer> getInfo(@PathVariable Long id) {
        return R.ok(customerService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:query')")
    @GetMapping("/public-pool")
    public R<List<InvestmentCustomer>> getPublicPool() {
        return R.ok(customerService.selectPublicPool());
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:query')")
    @GetMapping("/follow-user/{followUserId}")
    public R<List<InvestmentCustomer>> getByFollowUser(@PathVariable Long followUserId) {
        return R.ok(customerService.selectByFollowUserId(followUserId));
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:query')")
    @GetMapping("/statistics/status")
    public R<List<Map<String, Object>>> statusStatistics() {
        return R.ok(customerService.selectStatusStatistics());
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:query')")
    @GetMapping("/statistics/source")
    public R<List<Map<String, Object>>> sourceStatistics() {
        return R.ok(customerService.selectSourceStatistics());
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:query')")
    @GetMapping("/{customerId}/follow-records")
    public R<List<InvestmentFollow>> getFollowRecords(@PathVariable Long customerId) {
        return R.ok(customerService.selectFollowRecords(customerId));
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:add')")
    @PostMapping
    public R<InvestmentCustomer> add(@RequestBody InvestmentCustomer customer) {
        customerService.insert(customer);
        return R.ok(customer);
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:edit')")
    @PutMapping
    public R<InvestmentCustomer> edit(@RequestBody InvestmentCustomer customer) {
        customerService.update(customer);
        return R.ok(customer);
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:assign')")
    @PutMapping("/assign/{id}")
    public R<Void> assign(@PathVariable Long id, @RequestParam Long followUserId) {
        customerService.assign(id, followUserId);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:release')")
    @PutMapping("/release/{id}")
    public R<Void> release(@PathVariable Long id) {
        customerService.releaseToPublicPool(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:add')")
    @PostMapping("/follow")
    public R<Void> addFollowRecord(@RequestBody InvestmentFollow follow) {
        customerService.addFollowRecord(follow);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('investment:customer:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        customerService.deleteById(id);
        return R.ok();
    }

    /**
     * 更新跟进状态
     */
    @PreAuthorize("@ss.hasPermi('investment:customer:edit')")
    @PutMapping("/{id}/follow")
    public R<Void> updateFollowStatus(@PathVariable Long id, @RequestParam String status) {
        customerService.updateFollowStatus(id, status);
        return R.ok();
    }
}
