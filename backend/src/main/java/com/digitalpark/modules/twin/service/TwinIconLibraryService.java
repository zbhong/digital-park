package com.digitalpark.modules.twin.service;

import com.digitalpark.modules.twin.entity.TwinIconLibrary;

import java.util.List;

public interface TwinIconLibraryService {

    List<TwinIconLibrary> list(String iconType);

    TwinIconLibrary upload(TwinIconLibrary icon);

    TwinIconLibrary update(TwinIconLibrary icon);

    void delete(Long id);
}
