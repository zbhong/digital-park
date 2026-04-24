package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.modules.twin.entity.TwinIconLibrary;
import com.digitalpark.modules.twin.mapper.TwinIconLibraryMapper;
import com.digitalpark.modules.twin.service.TwinIconLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwinIconLibraryServiceImpl implements TwinIconLibraryService {

    private final TwinIconLibraryMapper iconLibraryMapper;

    @Override
    public List<TwinIconLibrary> list(String iconType) {
        LambdaQueryWrapper<TwinIconLibrary> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(iconType)) {
            wrapper.eq(TwinIconLibrary::getIconType, iconType);
        }
        wrapper.orderByAsc(TwinIconLibrary::getSort);
        return iconLibraryMapper.selectList(wrapper);
    }

    @Override
    public TwinIconLibrary upload(TwinIconLibrary icon) {
        icon.setIsSystem(0);
        iconLibraryMapper.insert(icon);
        return icon;
    }

    @Override
    public TwinIconLibrary update(TwinIconLibrary icon) {
        iconLibraryMapper.updateById(icon);
        return icon;
    }

    @Override
    public void delete(Long id) {
        iconLibraryMapper.deleteById(id);
    }
}
