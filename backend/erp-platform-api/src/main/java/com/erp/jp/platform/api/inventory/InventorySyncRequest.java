package com.erp.jp.platform.api.inventory;

/**
 * 库存同步请求
 */
public class InventorySyncRequest {
    private String platformSku;
    private int quantity;

    public String getPlatformSku() { return platformSku; }
    public void setPlatformSku(String v) { platformSku = v; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int v) { quantity = v; }
}
