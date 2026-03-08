package com.erp.jp.platform.api.product;

/**
 * 商品查询参数
 */
public class ProductQuery {
    private int page;
    private int pageSize;
    private String keyword;
    private String status;

    public int getPage() { return page; }
    public void setPage(int v) { page = v; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int v) { pageSize = v; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String v) { keyword = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
}
