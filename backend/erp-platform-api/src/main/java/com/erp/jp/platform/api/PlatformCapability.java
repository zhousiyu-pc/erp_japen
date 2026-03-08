package com.erp.jp.platform.api;

/**
 * 平台能力声明 - 各平台实现时声明支持的能力
 */
public class PlatformCapability {
    private final boolean productSync;
    private final boolean orderSync;
    private final boolean inventorySync;
    private final boolean adsSync;
    private final boolean reviewSync;
    private final String authType;

    public PlatformCapability(boolean productSync, boolean orderSync, boolean inventorySync,
                             boolean adsSync, boolean reviewSync, String authType) {
        this.productSync = productSync;
        this.orderSync = orderSync;
        this.inventorySync = inventorySync;
        this.adsSync = adsSync;
        this.reviewSync = reviewSync;
        this.authType = authType;
    }

    public boolean isProductSync() { return productSync; }
    public boolean isOrderSync() { return orderSync; }
    public boolean isInventorySync() { return inventorySync; }
    public boolean isAdsSync() { return adsSync; }
    public boolean isReviewSync() { return reviewSync; }
    public String getAuthType() { return authType; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean productSync;
        private boolean orderSync;
        private boolean inventorySync;
        private boolean adsSync;
        private boolean reviewSync;
        private String authType;

        public Builder productSync(boolean v) { productSync = v; return this; }
        public Builder orderSync(boolean v) { orderSync = v; return this; }
        public Builder inventorySync(boolean v) { inventorySync = v; return this; }
        public Builder adsSync(boolean v) { adsSync = v; return this; }
        public Builder reviewSync(boolean v) { reviewSync = v; return this; }
        public Builder authType(String v) { authType = v; return this; }

        public PlatformCapability build() {
            return new PlatformCapability(productSync, orderSync, inventorySync, adsSync, reviewSync, authType);
        }
    }
}
