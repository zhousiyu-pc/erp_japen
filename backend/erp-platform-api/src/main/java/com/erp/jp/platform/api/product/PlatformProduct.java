package com.erp.jp.platform.api.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 平台商品 - 统一结构，各平台实现适配
 */
public class PlatformProduct {
    private String platformSku;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String status;
    private List<String> images;
    private Map<String, String> attributes;

    public String getPlatformSku() { return platformSku; }
    public void setPlatformSku(String v) { platformSku = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { title = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description = v; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal v) { price = v; }
    public Integer getStock() { return stock; }
    public void setStock(Integer v) { stock = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> v) { images = v; }
    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> v) { attributes = v; }
}
