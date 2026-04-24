package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.dto.OrgDTO;
import com.digitalpark.modules.system.entity.SysOrganization;
import com.digitalpark.modules.system.mapper.SysOrganizationMapper;
import com.digitalpark.modules.system.service.SysOrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统组织 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOrganizationServiceImpl implements SysOrganizationService {

    private final SysOrganizationMapper sysOrganizationMapper;

    @Override
    public List<SysOrganization> getOrgTree() {
        List<SysOrganization> allOrgs = sysOrganizationMapper.selectList(
                new LambdaQueryWrapper<SysOrganization>().orderByAsc(SysOrganization::getSortOrder)
        );
        return buildOrgTree(allOrgs, 0L);
    }

    @Override
    public PageResult<SysOrganization> getOrgPage(Integer current, Integer size, String keyword) {
        LambdaQueryWrapper<SysOrganization> wrapper = new LambdaQueryWrapper<SysOrganization>()
                .orderByAsc(SysOrganization::getSortOrder);
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(SysOrganization::getOrgName, keyword)
                   .or().like(SysOrganization::getOrgCode, keyword);
        }
        Page<SysOrganization> page = sysOrganizationMapper.selectPage(new Page<>(current, size), wrapper);
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysOrganization getOrgById(Long id) {
        return sysOrganizationMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrg(OrgDTO orgDTO) {
        SysOrganization org = new SysOrganization();
        org.setParentId(orgDTO.getParentId() != null ? orgDTO.getParentId() : 0L);
        org.setOrgName(orgDTO.getOrgName());
        org.setOrgCode(orgDTO.getOrgCode());
        org.setLeader(orgDTO.getLeader());
        org.setPhone(orgDTO.getPhone());
        org.setSortOrder(orgDTO.getSortOrder() != null ? orgDTO.getSortOrder() : 0);
        org.setStatus(orgDTO.getStatus() != null ? orgDTO.getStatus() : 0);
        org.setRemark(orgDTO.getRemark());
        sysOrganizationMapper.insert(org);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrg(OrgDTO orgDTO) {
        if (orgDTO.getId() == null) {
            throw new BusinessException("组织ID不能为空");
        }
        SysOrganization org = sysOrganizationMapper.selectById(orgDTO.getId());
        if (org == null) {
            throw new BusinessException("组织不存在");
        }
        // 不能将自身设置为父组织
        if (orgDTO.getParentId() != null && orgDTO.getParentId().equals(orgDTO.getId())) {
            throw new BusinessException("不能将自身设置为父组织");
        }
        org.setParentId(orgDTO.getParentId());
        org.setOrgName(orgDTO.getOrgName());
        org.setOrgCode(orgDTO.getOrgCode());
        org.setLeader(orgDTO.getLeader());
        org.setPhone(orgDTO.getPhone());
        org.setSortOrder(orgDTO.getSortOrder());
        org.setStatus(orgDTO.getStatus());
        org.setRemark(orgDTO.getRemark());
        sysOrganizationMapper.updateById(org);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrg(Long id) {
        SysOrganization org = sysOrganizationMapper.selectById(id);
        if (org == null) {
            throw new BusinessException("组织不存在");
        }
        // 检查是否有子组织
        Long childCount = sysOrganizationMapper.selectCount(
                new LambdaQueryWrapper<SysOrganization>().eq(SysOrganization::getParentId, id)
        );
        if (childCount > 0) {
            throw new BusinessException("存在子组织，不允许删除");
        }
        sysOrganizationMapper.deleteById(id);
    }

    // ==================== 私有方法 ====================

    /**
     * 构建组织树
     */
    private List<SysOrganization> buildOrgTree(List<SysOrganization> orgs, Long parentId) {
        return orgs.stream()
                .filter(org -> Objects.equals(org.getParentId(), parentId))
                .peek(org -> org.setChildren(buildOrgTree(orgs, org.getId())))
                .sorted(Comparator.comparingInt(org -> org.getSortOrder() != null ? org.getSortOrder() : 0))
                .collect(Collectors.toList());
    }
}
