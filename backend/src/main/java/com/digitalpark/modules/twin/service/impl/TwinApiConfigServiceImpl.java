package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.modules.twin.entity.TwinApiConfig;
import com.digitalpark.modules.twin.mapper.TwinApiConfigMapper;
import com.digitalpark.modules.twin.service.TwinApiConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TwinApiConfigServiceImpl implements TwinApiConfigService {

    private final TwinApiConfigMapper apiConfigMapper;

    @Override
    public List<TwinApiConfig> list(String apiType) {
        LambdaQueryWrapper<TwinApiConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(apiType)) {
            wrapper.eq(TwinApiConfig::getApiType, apiType);
        }
        return apiConfigMapper.selectList(wrapper);
    }

    @Override
    public TwinApiConfig save(TwinApiConfig config) {
        apiConfigMapper.insert(config);
        return config;
    }

    @Override
    public TwinApiConfig update(TwinApiConfig config) {
        apiConfigMapper.updateById(config);
        return config;
    }

    @Override
    public void delete(Long id) {
        apiConfigMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> testConnection(Long id) {
        TwinApiConfig config = apiConfigMapper.selectById(id);
        Map<String, Object> result = new HashMap<>();
        if (config == null) {
            result.put("success", false);
            result.put("message", "配置不存在");
            return result;
        }
        // 模拟测试连接结果
        result.put("success", true);
        result.put("message", "连接成功");
        result.put("responseTime", 120);
        result.put("statusCode", 200);
        return result;
    }
}
