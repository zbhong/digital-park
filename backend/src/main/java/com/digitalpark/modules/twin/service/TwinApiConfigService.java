package com.digitalpark.modules.twin.service;

import com.digitalpark.modules.twin.entity.TwinApiConfig;

import java.util.List;
import java.util.Map;

public interface TwinApiConfigService {

    List<TwinApiConfig> list(String apiType);

    TwinApiConfig save(TwinApiConfig config);

    TwinApiConfig update(TwinApiConfig config);

    void delete(Long id);

    Map<String, Object> testConnection(Long id);
}
