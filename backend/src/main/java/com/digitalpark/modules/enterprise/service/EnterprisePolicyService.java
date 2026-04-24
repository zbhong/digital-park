package com.digitalpark.modules.enterprise.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.enterprise.entity.EnterprisePolicy;

import java.util.List;

/**
 * 政策信息Service接口
 *
 * @author digitalpark
 */
public interface EnterprisePolicyService {

    PageResult<EnterprisePolicy> selectPage(EnterprisePolicy query, int pageNum, int pageSize);

    EnterprisePolicy selectById(Long id);

    List<EnterprisePolicy> selectPublished();

    List<EnterprisePolicy> selectByType(String type);

    int insert(EnterprisePolicy policy);

    int update(EnterprisePolicy policy);

    int publish(Long id);

    int expire(Long id);

    int deleteById(Long id);
}
