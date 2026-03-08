package com.erp.jp.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 店铺配置表 (shop_id = store_id)
 */
@TableName("t_shop_config")
public class ShopConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long shopId;
    private Long tenantId;
    private String shopAlias;
    private Integer businessType;
    private Long defaultWarehouseId;
    private String currency;
    private String timezone;
    private String taxConfig;
    private String syncStrategy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getShopAlias() { return shopAlias; }
    public void setShopAlias(String shopAlias) { this.shopAlias = shopAlias; }
    public Integer getBusinessType() { return businessType; }
    public void setBusinessType(Integer businessType) { this.businessType = businessType; }
    public Long getDefaultWarehouseId() { return defaultWarehouseId; }
    public void setDefaultWarehouseId(Long defaultWarehouseId) { this.defaultWarehouseId = defaultWarehouseId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public String getTaxConfig() { return taxConfig; }
    public void setTaxConfig(String taxConfig) { this.taxConfig = taxConfig; }
    public String getSyncStrategy() { return syncStrategy; }
    public void setSyncStrategy(String syncStrategy) { this.syncStrategy = syncStrategy; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
