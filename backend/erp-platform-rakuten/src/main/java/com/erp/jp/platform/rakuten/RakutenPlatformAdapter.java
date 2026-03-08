package com.erp.jp.platform.rakuten;

import com.erp.jp.platform.api.*;
import com.erp.jp.platform.api.auth.PlatformAuthContext;
import com.erp.jp.platform.api.auth.PlatformAuthResult;
import com.erp.jp.platform.api.dto.PageResult;
import com.erp.jp.platform.api.inventory.InventorySyncRequest;
import com.erp.jp.platform.api.inventory.InventorySyncResult;
import com.erp.jp.platform.api.order.OrderQuery;
import com.erp.jp.platform.api.order.PlatformOrder;
import com.erp.jp.platform.api.product.PlatformProduct;
import com.erp.jp.platform.api.product.ProductQuery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * 乐天市场（日本）平台适配器
 * 实现 PlatformAdapter 接口，通过 Spring 自动注册到 PlatformRegistry
 * 乐天 API 文档: https://webservice.rakuten.co.jp/
 */
@Component
@ConditionalOnProperty(name = "erp.platform.rakuten.enabled", havingValue = "true", matchIfMissing = true)
public class RakutenPlatformAdapter implements PlatformAdapter {

    private static final PlatformCapability CAPABILITY = PlatformCapability.builder()
            .productSync(true).orderSync(true).inventorySync(true)
            .adsSync(false).reviewSync(false)
            .authType("API_KEY")
            .build();

    @Override
    public PlatformCode getPlatformCode() {
        return PlatformCode.RAKUTEN;
    }

    @Override
    public String getAuthUrl(PlatformAuthContext ctx) {
        // 乐天使用 Application ID + 授权链接，此处返回授权 URL
        return "https://auth.rakuten.co.jp/authorize?client_id=" + ctx.getApiKey()
                + "&redirect_uri=" + ctx.getRedirectUri()
                + "&response_type=code&state=" + ctx.getState();
    }

    @Override
    public PlatformAuthResult exchangeToken(PlatformAuthContext ctx, String authCode) {
        // TODO: 调用乐天 token 接口换取 access_token
        return PlatformAuthResult.of("rakuten_access_token", ctx.getRefreshToken(),
                Instant.now().plusSeconds(3600), "rakuten_seller");
    }

    @Override
    public PlatformAuthResult refreshToken(PlatformAuthContext ctx) {
        // TODO: 调用乐天 refresh 接口
        return PlatformAuthResult.of("rakuten_refreshed_token", ctx.getRefreshToken(),
                Instant.now().plusSeconds(3600), "rakuten_seller");
    }

    @Override
    public PageResult<PlatformProduct> listProducts(Long storeId, ProductQuery query) {
        // TODO: 调用乐天 商品一覧 API (IchibaItem/Search)
        return PageResult.of(Collections.emptyList(), 0, query.getPage(), query.getPageSize());
    }

    @Override
    public PlatformProduct getProduct(Long storeId, String platformSku) {
        // TODO: 调用乐天 商品詳細 API
        throw new UnsupportedOperationException("Rakuten getProduct not implemented yet");
    }

    @Override
    public PlatformProduct createOrUpdateProduct(Long storeId, PlatformProduct product) {
        // TODO: 调用乐天 商品登録/更新 API
        throw new UnsupportedOperationException("Rakuten createOrUpdateProduct not implemented yet");
    }

    @Override
    public PageResult<PlatformOrder> listOrders(Long storeId, OrderQuery query) {
        // TODO: 调用乐天 注文一覧 API (RMS Order API)
        return PageResult.of(Collections.emptyList(), 0, query.getPage(), query.getPageSize());
    }

    @Override
    public PlatformOrder getOrder(Long storeId, String platformOrderId) {
        // TODO: 调用乐天 注文詳細 API
        throw new UnsupportedOperationException("Rakuten getOrder not implemented yet");
    }

    @Override
    public InventorySyncResult syncInventory(Long storeId, List<InventorySyncRequest> requests) {
        // TODO: 调用乐天 在庫更新 API
        return InventorySyncResult.of(true, requests.size(), 0, Collections.emptyList());
    }

    @Override
    public PlatformCapability getCapability() {
        return CAPABILITY;
    }
}
