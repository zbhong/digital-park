package com.digitalpark.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.system.entity.SysDictItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统字典项 Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    /**
     * 根据字典类型查询字典项列表
     */
    @Select("SELECT di.* FROM sys_dict_item di " +
            "INNER JOIN sys_dict d ON di.dict_id = d.id " +
            "WHERE d.dict_type = #{dictType} AND di.status = 0 AND di.deleted = 0 " +
            "ORDER BY di.sort_order ASC")
    List<SysDictItem> selectByDictType(@Param("dictType") String dictType);
}
