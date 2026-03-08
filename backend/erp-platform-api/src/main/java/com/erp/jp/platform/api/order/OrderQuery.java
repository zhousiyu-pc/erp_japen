package com.erp.jp.platform.api.order;

import java.time.Instant;

/**
 * 订单查询参数
 */
public class OrderQuery {
    private int page;
    private int pageSize;
    private String status;
    private Instant startTime;
    private Instant endTime;

    public int getPage() { return page; }
    public void setPage(int v) { page = v; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int v) { pageSize = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant v) { startTime = v; }
    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant v) { endTime = v; }
}
