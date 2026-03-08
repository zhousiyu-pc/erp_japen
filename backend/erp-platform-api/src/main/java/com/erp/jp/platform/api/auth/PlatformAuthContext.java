package com.erp.jp.platform.api.auth;

/**
 * 平台授权上下文 - 各平台 OAuth/API Key 等差异化参数
 */
public class PlatformAuthContext {
    private Long storeId;
    private Long tenantId;
    private String redirectUri;
    private String state;
    private String apiKey;
    private String apiSecret;
    private String refreshToken;

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long v) { storeId = v; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long v) { tenantId = v; }
    public String getRedirectUri() { return redirectUri; }
    public void setRedirectUri(String v) { redirectUri = v; }
    public String getState() { return state; }
    public void setState(String v) { state = v; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String v) { apiKey = v; }
    public String getApiSecret() { return apiSecret; }
    public void setApiSecret(String v) { apiSecret = v; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String v) { refreshToken = v; }
}
