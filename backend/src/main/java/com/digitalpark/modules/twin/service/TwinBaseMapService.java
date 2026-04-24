package com.digitalpark.modules.twin.service;

import com.digitalpark.modules.twin.entity.TwinBaseMap;

import java.util.List;

public interface TwinBaseMapService {

    List<TwinBaseMap> list(String mapType, Long bindId);

    TwinBaseMap upload(TwinBaseMap map);

    TwinBaseMap update(TwinBaseMap map);

    void delete(Long id);

    TwinBaseMap preview(Long id);
}
