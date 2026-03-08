package com.erp.jp.platform.api.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 平台订单 - 统一结构
 */
public class PlatformOrder {
    private String platformOrderId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private Instant orderTime;
    private List<OrderItem> items;
    private String shippingAddress;

    public String getPlatformOrderId() { return platformOrderId; }
    public void setPlatformOrderId(String v) { platformOrderId = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal v) { amount = v; }
    public String getCurrency() { return currency; }
    public void setCurrency(String v) { currency = v; }
    public Instant getOrderTime() { return orderTime; }
    public void setOrderTime(Instant v) { orderTime = v; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> v) { items = v; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String v) { shippingAddress = v; }

    public static class OrderItem {
        private String platformSku;
        private String title;
        private int quantity;
        private BigDecimal price;

        public String getPlatformSku() { return platformSku; }
        public void setPlatformSku(String v) { platformSku = v; }
        public String getTitle() { return title; }
        public void setTitle(String v) { title = v; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int v) { quantity = v; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal v) { price = v; }
    }
}
