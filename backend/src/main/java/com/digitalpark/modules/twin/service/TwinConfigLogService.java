package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinConfigLog;

public interface TwinConfigLogService {

    void log(String module, String actionType, String detail, Long targetId, String targetName, String operator, String ip);

    PageResult<TwinConfigLog> list(String module, int pageNum, int pageSize);
}
