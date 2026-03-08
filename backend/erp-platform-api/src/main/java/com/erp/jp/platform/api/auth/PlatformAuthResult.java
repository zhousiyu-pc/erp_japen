package com.erp.jp.platform.api.auth;

import java.time.Instant;

/**
 * 平台授权结果 - 统一 token 结构
 */
public class PlatformAuthResult {
    private String accessToken;
    private String refreshToken;
    private Instant expireTime;
    private String sellerId;

    public static PlatformAuthResult of(String accessToken, String refreshToken, Instant expireTime, String sellerId) {
        PlatformAuthResult r = new PlatformAuthResult();
        r.accessToken = accessToken;
        r.refreshToken = refreshToken;
        r.expireTime = expireTime;
        r.sellerId = sellerId;
        return r;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public Instant getExpireTime() { return expireTime; }
    public String getSellerId() { return sellerId; }
}
