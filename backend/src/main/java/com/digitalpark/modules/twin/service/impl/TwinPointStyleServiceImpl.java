package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.modules.twin.entity.TwinPointStyle;
import com.digitalpark.modules.twin.mapper.TwinPointStyleMapper;
import com.digitalpark.modules.twin.service.TwinPointStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwinPointStyleServiceImpl implements TwinPointStyleService {

    private final TwinPointStyleMapper pointStyleMapper;

    @Override
    public List<TwinPointStyle> list(String iconType) {
        LambdaQueryWrapper<TwinPointStyle> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(iconType)) {
            wrapper.eq(TwinPointStyle::getIconType, iconType);
        }
        wrapper.orderByDesc(TwinPointStyle::getIsDefault);
        return pointStyleMapper.selectList(wrapper);
    }

    @Override
    public TwinPointStyle save(TwinPointStyle style) {
        pointStyleMapper.insert(style);
        return style;
    }

    @Override
    public TwinPointStyle update(TwinPointStyle style) {
        pointStyleMapper.updateById(style);
        return style;
    }

    @Override
    public void delete(Long id) {
        pointStyleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyToGroup(Long styleId, String iconType) {
        TwinPointStyle style = pointStyleMapper.selectById(styleId);
        if (style == null) {
            return 0;
        }
        return 1;
    }
}
