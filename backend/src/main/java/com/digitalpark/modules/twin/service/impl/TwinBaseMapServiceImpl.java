package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.modules.twin.entity.TwinBaseMap;
import com.digitalpark.modules.twin.mapper.TwinBaseMapMapper;
import com.digitalpark.modules.twin.service.TwinBaseMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwinBaseMapServiceImpl implements TwinBaseMapService {

    private final TwinBaseMapMapper baseMapMapper;

    @Override
    public List<TwinBaseMap> list(String mapType, Long bindId) {
        LambdaQueryWrapper<TwinBaseMap> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(mapType)) {
            wrapper.eq(TwinBaseMap::getMapType, mapType);
        }
        if (bindId != null) {
            wrapper.eq(TwinBaseMap::getBindId, bindId);
        }
        wrapper.orderByAsc(TwinBaseMap::getSort);
        return baseMapMapper.selectList(wrapper);
    }

    @Override
    public TwinBaseMap upload(TwinBaseMap map) {
        baseMapMapper.insert(map);
        return map;
    }

    @Override
    public TwinBaseMap update(TwinBaseMap map) {
        baseMapMapper.updateById(map);
        return map;
    }

    @Override
    public void delete(Long id) {
        baseMapMapper.deleteById(id);
    }

    @Override
    public TwinBaseMap preview(Long id) {
        return baseMapMapper.selectById(id);
    }
}
