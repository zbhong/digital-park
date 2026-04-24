package com.digitalpark.modules.system.controller;

import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.dto.OrgDTO;
import com.digitalpark.modules.system.entity.SysOrganization;
import com.digitalpark.modules.system.service.SysOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统组织控制器
 * /system/org 路径，组织树CRUD
 *
 * @author digitalpark
 */
@Tag(name = "组织管理")
@RestController
@RequestMapping("/system/org")
@RequiredArgsConstructor
public class SysOrgController extends BaseController {

    private final SysOrganizationService sysOrganizationService;

    /**
     * 获取组织树
     */
    @Operation(summary = "组织树")
    @PreAuthorize("@ss.hasPermi('system:org:list')")
    @GetMapping("/tree")
    public R<List<SysOrganization>> tree() {
        List<SysOrganization> orgTree = sysOrganizationService.getOrgTree();
        return success(orgTree);
    }

    /**
     * 获取组织列表（分页）
     */
    @Operation(summary = "组织列表")
    @PreAuthorize("@ss.hasPermi('system:org:list')")
    @GetMapping("/list")
    public R<PageResult<SysOrganization>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        PageResult<SysOrganization> result = sysOrganizationService.getOrgPage(current, size, keyword);
        return success(result);
    }

    /**
     * 获取单个组织详情
     */
    @Operation(summary = "组织详情")
    @PreAuthorize("@ss.hasPermi('system:org:query')")
    @GetMapping("/{id}")
    public R<SysOrganization> getInfo(@PathVariable Long id) {
        SysOrganization org = sysOrganizationService.getOrgById(id);
        return success(org);
    }

    /**
     * 新增组织
     */
    @Operation(summary = "新增组织")
    @PreAuthorize("@ss.hasPermi('system:org:add')")
    @Log(module = "组织管理", type = Log.OperationType.INSERT, description = "新增组织")
    @PostMapping
    public R<Void> add(@Valid @RequestBody OrgDTO orgDTO) {
        sysOrganizationService.createOrg(orgDTO);
        return success();
    }

    /**
     * 修改组织
     */
    @Operation(summary = "修改组织")
    @PreAuthorize("@ss.hasPermi('system:org:edit')")
    @Log(module = "组织管理", type = Log.OperationType.UPDATE, description = "修改组织")
    @PutMapping
    public R<Void> edit(@Valid @RequestBody OrgDTO orgDTO) {
        sysOrganizationService.updateOrg(orgDTO);
        return success();
    }

    /**
     * 删除组织
     */
    @Operation(summary = "删除组织")
    @PreAuthorize("@ss.hasPermi('system:org:remove')")
    @Log(module = "组织管理", type = Log.OperationType.DELETE, description = "删除组织")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysOrganizationService.deleteOrg(id);
        return success();
    }
}
