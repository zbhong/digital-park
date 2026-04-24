package com.digitalpark.modules.system.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.dto.OrgDTO;
import com.digitalpark.modules.system.entity.SysOrganization;

import java.util.List;

/**
 * 系统组织 Service 接口
 *
 * @author digitalpark
 */
public interface SysOrganizationService {

    /**
     * 获取组织树
     *
     * @return 组织树形结构
     */
    List<SysOrganization> getOrgTree();

    /**
     * 获取组织分页列表
     *
     * @param current 当前页码
     * @param size    每页大小
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageResult<SysOrganization> getOrgPage(Integer current, Integer size, String keyword);

    /**
     * 根据ID获取组织详情
     *
     * @param id 组织ID
     * @return 组织信息
     */
    SysOrganization getOrgById(Long id);

    /**
     * 创建组织
     *
     * @param orgDTO 组织信息
     */
    void createOrg(OrgDTO orgDTO);

    /**
     * 更新组织
     *
     * @param orgDTO 组织信息
     */
    void updateOrg(OrgDTO orgDTO);

    /**
     * 删除组织
     *
     * @param id 组织ID
     */
    void deleteOrg(Long id);
}
