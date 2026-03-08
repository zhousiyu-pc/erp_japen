package com.erp.jp.store.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 店铺列表 VO - 含授权信息
 */
public class StoreVO {
    private Long id;
    private String storeName;
    private String platformCode;
    private String platform;
    private String sellerId;
    private String currency;
    private String timezone;
    private String defaultWarehouse;
    private String manager;
    private Integer authStatus;
    private String status;
    private LocalDate expireDate;
    private Integer daysLeft;
    private Boolean isSandbox;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public String getPlatformCode() { return platformCode; }
    public void setPlatformCode(String platformCode) { this.platformCode = platformCode; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public String getDefaultWarehouse() { return defaultWarehouse; }
    public void setDefaultWarehouse(String defaultWarehouse) { this.defaultWarehouse = defaultWarehouse; }
    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }
    public Integer getAuthStatus() { return authStatus; }
    public void setAuthStatus(Integer authStatus) { this.authStatus = authStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDate expireDate) { this.expireDate = expireDate; }
    public Integer getDaysLeft() { return daysLeft; }
    public void setDaysLeft(Integer daysLeft) { this.daysLeft = daysLeft; }
    public Boolean getIsSandbox() { return isSandbox; }
    public void setIsSandbox(Boolean isSandbox) { this.isSandbox = isSandbox; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
