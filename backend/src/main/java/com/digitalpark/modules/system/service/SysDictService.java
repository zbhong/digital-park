package com.digitalpark.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.entity.SysDict;
import com.digitalpark.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * 系统字典 Service 接口
 *
 * @author digitalpark
 */
public interface SysDictService {

    /**
     * 获取字典列表（分页）
     *
     * @param page     分页参数
     * @param dictName 字典名称
     * @param dictType 字典类型
     * @return 分页结果
     */
    PageResult<SysDict> getDictList(Page<?> page, String dictName, String dictType);

    /**
     * 创建字典
     *
     * @param dict 字典信息
     */
    void createDict(SysDict dict);

    /**
     * 更新字典
     *
     * @param dict 字典信息
     */
    void updateDict(SysDict dict);

    /**
     * 删除字典
     *
     * @param id 字典ID
     */
    void deleteDict(Long id);

    /**
     * 获取字典项列表
     *
     * @param dictType 字典类型
     * @return 字典项列表
     */
    List<SysDictItem> getDictItems(String dictType);

    /**
     * 根据字典类型获取字典项（带缓存）
     *
     * @param dictType 字典类型
     * @return 字典项列表
     */
    List<SysDictItem> getDictByType(String dictType);

    /**
     * 刷新字典缓存
     */
    void refreshCache();
}
