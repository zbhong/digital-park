package com.digitalpark.modules.enterprise.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.enterprise.entity.EnterpriseServiceRequest;

import java.util.List;
import java.util.Map;

/**
 * 服务请求Service接口
 *
 * @author digitalpark
 */
public interface EnterpriseServiceRequestService {

    PageResult<EnterpriseServiceRequest> selectPage(EnterpriseServiceRequest query, int pageNum, int pageSize);

    EnterpriseServiceRequest selectById(Long id);

    List<EnterpriseServiceRequest> selectByEnterpriseId(Long enterpriseId);

    List<Map<String, Object>> selectTypeStatistics();

    List<Map<String, Object>> selectStatusStatistics();

    int insert(EnterpriseServiceRequest request);

    int handle(Long id, Long handlerId, String handleResult);

    int close(Long id);

    int deleteById(Long id);
}
