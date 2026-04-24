package com.digitalpark.modules.twin.service;

import com.digitalpark.modules.twin.entity.TwinPointStyle;

import java.util.List;

public interface TwinPointStyleService {

    List<TwinPointStyle> list(String iconType);

    TwinPointStyle save(TwinPointStyle style);

    TwinPointStyle update(TwinPointStyle style);

    void delete(Long id);

    int applyToGroup(Long styleId, String iconType);
}
