package com.digitalpark.modules.safety.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyNetwork;

/**
 * 网络安全Service接口
 *
 * @author digitalpark
 */
public interface SafetyNetworkService {

    PageResult<SafetyNetwork> selectPage(SafetyNetwork query, int pageNum, int pageSize);

    SafetyNetwork selectById(Long id);

    int insert(SafetyNetwork network);

    int update(SafetyNetwork network);

    int deleteById(Long id);
}
