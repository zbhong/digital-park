package com.digitalpark.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 *
 * @param <T> 数据类型
 * @author digitalpark
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页码
     */
    private long current;

    /**
     * 每页大小
     */
    private long size;

    /**
     * 总页数
     */
    private long pages;

    public PageResult() {
    }

    public PageResult(List<T> records, long total, long current, long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = Math.max(size, 1);
        this.pages = size > 0 ? (total + size - 1) / size : 0;
    }

    /**
     * 构建分页结果（简化版，仅记录和总数）
     */
    public PageResult(List<T> records, long total) {
        this.records = records;
        this.total = total;
        this.current = 1;
        this.size = records != null ? Math.max(records.size(), 1) : 1;
        this.pages = this.size > 0 ? (total + this.size - 1) / this.size : 0;
    }

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> of(List<T> records, long total, long current, long size) {
        return new PageResult<>(records, total, current, size);
    }
}
