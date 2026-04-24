package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.modules.twin.entity.TwinConfigVersion;
import com.digitalpark.modules.twin.mapper.TwinConfigVersionMapper;
import com.digitalpark.modules.twin.service.TwinConfigVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TwinConfigVersionServiceImpl implements TwinConfigVersionService {

    private final TwinConfigVersionMapper configVersionMapper;

    @Override
    public List<TwinConfigVersion> list() {
        LambdaQueryWrapper<TwinConfigVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TwinConfigVersion::getId);
        return configVersionMapper.selectList(wrapper);
    }

    @Override
    public TwinConfigVersion saveDraft(String configSnapshot, String changeLog) {
        TwinConfigVersion version = new TwinConfigVersion();
        version.setVersionNo("v" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        version.setStatus("draft");
        version.setConfigSnapshot(configSnapshot);
        version.setChangeLog(changeLog);
        configVersionMapper.insert(version);
        return version;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long versionId) {
        TwinConfigVersion version = configVersionMapper.selectById(versionId);
        if (version == null) {
            throw new RuntimeException("版本不存在");
        }
        version.setStatus("published");
        version.setPublishTime(LocalDateTime.now());
        configVersionMapper.updateById(version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(Long versionId) {
        TwinConfigVersion version = configVersionMapper.selectById(versionId);
        if (version == null) {
            throw new RuntimeException("版本不存在");
        }
        // 创建新版本作为回滚快照
        TwinConfigVersion rollback = new TwinConfigVersion();
        rollback.setVersionNo("v" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-rollback");
        rollback.setStatus("published");
        rollback.setConfigSnapshot(version.getConfigSnapshot());
        rollback.setChangeLog("回滚至版本: " + version.getVersionNo());
        rollback.setPublishTime(LocalDateTime.now());
        configVersionMapper.insert(rollback);
    }

    @Override
    public String exportConfig(Long versionId) {
        TwinConfigVersion version = configVersionMapper.selectById(versionId);
        if (version == null) {
            throw new RuntimeException("版本不存在");
        }
        return version.getConfigSnapshot();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importConfig(String json) {
        TwinConfigVersion version = new TwinConfigVersion();
        version.setVersionNo("v" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-import");
        version.setStatus("draft");
        version.setConfigSnapshot(json);
        version.setChangeLog("导入配置");
        configVersionMapper.insert(version);
    }
}
