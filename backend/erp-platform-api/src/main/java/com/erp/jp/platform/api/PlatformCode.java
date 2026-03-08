package com.erp.jp.platform.api;

/**
 * 平台编码枚举 - 与 t_store_master.platform_code 对应
 * 新增平台时在此扩展，并实现对应 PlatformAdapter
 */
public enum PlatformCode {
    /** 乐天市场（日本）- 优先实现 */
    RAKUTEN("RAKUTEN", "乐天市场", "日本", true),

    /** 亚马逊日本 - 预留 */
    AMAZON_JP("AMAZON_JP", "亚马逊日本", "日本", false),

    /** 雅虎购物日本 - 预留 */
    YAHOO_JP("YAHOO_JP", "雅虎购物", "日本", false);

    private final String code;
    private final String displayName;
    private final String region;
    private final boolean enabled;

    PlatformCode(String code, String displayName, String region, boolean enabled) {
        this.code = code;
        this.displayName = displayName;
        this.region = region;
        this.enabled = enabled;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
    public String getRegion() { return region; }
    public boolean isEnabled() { return enabled; }

    public static PlatformCode of(String code) {
        for (PlatformCode pc : values()) {
            if (pc.code.equals(code)) {
                return pc;
            }
        }
        throw new IllegalArgumentException("Unknown platform code: " + code);
    }
}
