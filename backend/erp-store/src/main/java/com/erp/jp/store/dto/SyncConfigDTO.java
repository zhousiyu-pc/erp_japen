package com.erp.jp.store.dto;

import java.util.List;

/**
 * 同步策略配置 DTO
 */
public class SyncConfigDTO {
    private String orderSyncRate;
    private String stockSyncStrategy;
    private Integer stockReserve;
    private Boolean filterTestOrders;
    private List<String> excludeSkus;
    private Boolean globalSyncEnabled;

    public String getOrderSyncRate() { return orderSyncRate; }
    public void setOrderSyncRate(String orderSyncRate) { this.orderSyncRate = orderSyncRate; }
    public String getStockSyncStrategy() { return stockSyncStrategy; }
    public void setStockSyncStrategy(String stockSyncStrategy) { this.stockSyncStrategy = stockSyncStrategy; }
    public Integer getStockReserve() { return stockReserve; }
    public void setStockReserve(Integer stockReserve) { this.stockReserve = stockReserve; }
    public Boolean getFilterTestOrders() { return filterTestOrders; }
    public void setFilterTestOrders(Boolean filterTestOrders) { this.filterTestOrders = filterTestOrders; }
    public List<String> getExcludeSkus() { return excludeSkus; }
    public void setExcludeSkus(List<String> excludeSkus) { this.excludeSkus = excludeSkus; }
    public Boolean getGlobalSyncEnabled() { return globalSyncEnabled; }
    public void setGlobalSyncEnabled(Boolean globalSyncEnabled) { this.globalSyncEnabled = globalSyncEnabled; }
}
