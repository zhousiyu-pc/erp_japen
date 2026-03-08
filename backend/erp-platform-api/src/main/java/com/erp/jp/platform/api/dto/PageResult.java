package com.erp.jp.platform.api.dto;

import java.util.List;

/**
 * 分页结果 - 通用
 */
public class PageResult<T> {
    private final List<T> list;
    private final long total;
    private final int page;
    private final int pageSize;

    public PageResult(List<T> list, long total, int page, int pageSize) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public static <T> PageResult<T> of(List<T> list, long total, int page, int pageSize) {
        return new PageResult<>(list, total, page, pageSize);
    }

    public List<T> getList() { return list; }
    public long getTotal() { return total; }
    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }
}
