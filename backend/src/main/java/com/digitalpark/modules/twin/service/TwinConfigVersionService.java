package com.digitalpark.modules.twin.service;

import com.digitalpark.modules.twin.entity.TwinConfigVersion;

import java.util.List;

public interface TwinConfigVersionService {

    List<TwinConfigVersion> list();

    TwinConfigVersion saveDraft(String configSnapshot, String changeLog);

    void publish(Long versionId);

    void rollback(Long versionId);

    String exportConfig(Long versionId);

    void importConfig(String json);
}
