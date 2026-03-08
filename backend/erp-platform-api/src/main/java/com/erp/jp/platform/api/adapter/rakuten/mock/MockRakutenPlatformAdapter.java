package com.erp.jp.platform.api.adapter.rakuten.mock;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 乐天平台适配器 - 模拟模式
 * 用于开发和测试环境，生成模拟数据
 * [by Agent]
 */
@Component
@ConditionalOnProperty(name = "erp.platform.rakuten.mock", havingValue = "true")
public class MockRakutenPlatformAdapter implements PlatformAdapter {

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
        return "https://auth.rakuten.co.jp/mock/authorize?client_id=" + ctx.getApiKey();
    }

    @Override
    public PlatformAuthResult exchangeToken(PlatformAuthContext ctx, String authCode) {
        return PlatformAuthResult.of("mock_rakuten_token_" + UUID.randomUUID(),
                "mock_refresh_token", Instant.now().plusSeconds(3600), "mock_seller");
    }

    @Override
    public PlatformAuthResult refreshToken(PlatformAuthContext ctx) {
        return PlatformAuthResult.of("mock_rakuten_token_" + UUID.randomUUID(),
                "mock_refresh_token", Instant.now().plusSeconds(3600), "mock_seller");
    }

    @Override
    public PageResult<PlatformProduct> listProducts(Long storeId, ProductQuery query) {
        // 生成模拟商品数据
        List<PlatformProduct> products = new ArrayList<>();
        for (int i = 0; i < query.getPageSize(); i++) {
            PlatformProduct product = new PlatformProduct();
            product.setProductId("mock_product_" + i);
            product.setProductName("模拟商品_" + i);
            product.setPrice(1000.0 + i * 100);
            product.setStock(100 + i * 10);
            product.setPlatformCode("RAKUTEN");
            product.setStoreId(storeId);
            products.add(product);
        }
        return PageResult.of(products, 100L, query.getPage(), query.getPageSize());
    }

    @Override
    public PlatformProduct getProduct(Long storeId, String platformSku) {
        PlatformProduct product = new PlatformProduct();
        product.setProductId(platformSku);
        product.setProductName("模拟商品_" + platformSku);
        product.setPrice(2000.0);
        product.setStock(50);
        product.setPlatformCode("RAKUTEN");
        product.setStoreId(storeId);
        return product;
    }

    @Override
    public PlatformProduct createOrUpdateProduct(Long storeId, PlatformProduct product) {
        product.setProductId(product.getProductId() != null ? product.getProductId() : "mock_" + UUID.randomUUID());
        return product;
    }

    @Override
    public PageResult<PlatformOrder> listOrders(Long storeId, OrderQuery query) {
        // 生成模拟订单数据
        List<PlatformOrder> orders = new ArrayList<>();
        for (int i = 0; i < query.getPageSize(); i++) {
            PlatformOrder order = new PlatformOrder();
            order.setOrderId("mock_order_" + i);
            order.setPlatformOrderId("R" + System.currentTimeMillis() + "_" + i);
            order.setStoreId(storeId);
            order.setPlatformCode("RAKUTEN");
            order.setOrderStatus("NEW");
            order.setOrderTime(LocalDateTime.now().minusDays(i));
            order.setTotalAmount(5000.0 + i * 500);
            order.setCurrency("JPY");
            
            // 买家信息
            order.setBuyerName("山田" + i);
            order.setBuyerEmail("yamada" + i + "@example.jp");
            order.setBuyerPhone("03-1234-567" + i);
            
            // 收货地址
            order.setShippingName("山田" + i);
            order.setShippingProvince("東京都");
            order.setShippingCity("渋谷区");
            order.setShippingAddress("宇田川町 1-" + i);
            order.setShippingZip("150-0042");
            
            orders.add(order);
        }
        return PageResult.of(orders, 50L, query.getPage(), query.getPageSize());
    }

    @Override
    public PlatformOrder getOrder(Long storeId, String platformOrderId) {
        PlatformOrder order = new PlatformOrder();
        order.setOrderId(platformOrderId);
        order.setPlatformOrderId(platformOrderId);
        order.setStoreId(storeId);
        order.setPlatformCode("RAKUTEN");
        order.setOrderStatus("NEW");
        order.setOrderTime(LocalDateTime.now());
        order.setTotalAmount(5000.0);
        order.setCurrency("JPY");
        return order;
    }

    @Override
    public InventorySyncResult syncInventory(Long storeId, List<InventorySyncRequest> requests) {
        // 模拟库存同步成功
        return InventorySyncResult.of(true, requests.size(), 0, List.of());
    }

    @Override
    public PlatformCapability getCapability() {
        return CAPABILITY;
    }
}
