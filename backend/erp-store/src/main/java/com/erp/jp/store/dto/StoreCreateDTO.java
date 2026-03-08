package com.erp.jp.store.dto;

/**
 * 店铺创建/接入 DTO
 */
public class StoreCreateDTO {
    private String storeName;
    private String platformCode;
    private String sellerId;
    private String apiKey;
    private String apiSecret;
    private String redirectUri;
    private Long defaultWarehouseId;
    private Long managerId;
    private Boolean isSandbox;

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getPlatformCode() { return platformCode; }
    public void setPlatformCode(String platformCode) { this.platformCode = platformCode; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getApiSecret() { return apiSecret; }
    public void setApiSecret(String apiSecret) { this.apiSecret = apiSecret; }
    public String getRedirectUri() { return redirectUri; }
    public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }
    public Long getDefaultWarehouseId() { return defaultWarehouseId; }
    public void setDefaultWarehouseId(Long defaultWarehouseId) { this.defaultWarehouseId = defaultWarehouseId; }
    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
    public Boolean getIsSandbox() { return isSandbox; }
    public void setIsSandbox(Boolean isSandbox) { this.isSandbox = isSandbox; }
}
