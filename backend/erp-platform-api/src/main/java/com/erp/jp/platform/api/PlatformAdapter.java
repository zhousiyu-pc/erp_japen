package com.erp.jp.platform.api;

import com.erp.jp.platform.api.auth.PlatformAuthContext;
import com.erp.jp.platform.api.auth.PlatformAuthResult;
import com.erp.jp.platform.api.dto.PageResult;
import com.erp.jp.platform.api.order.PlatformOrder;
import com.erp.jp.platform.api.order.OrderQuery;
import com.erp.jp.platform.api.product.PlatformProduct;
import com.erp.jp.platform.api.product.ProductQuery;
import com.erp.jp.platform.api.inventory.InventorySyncRequest;
import com.erp.jp.platform.api.inventory.InventorySyncResult;

import java.util.List;

/**
 * 平台适配器接口 - 各平台实现此接口，实现可插拔
 * 业务层通过 PlatformRegistry 按 platformCode 获取适配器，无需关心具体平台
 */
public interface PlatformAdapter {

    /** 返回本适配器支持的平台编码 */
    PlatformCode getPlatformCode();

    /**
     * 授权相关
     */
    String getAuthUrl(PlatformAuthContext ctx);
    PlatformAuthResult exchangeToken(PlatformAuthContext ctx, String authCode);
    PlatformAuthResult refreshToken(PlatformAuthContext ctx);

    /**
     * 商品相关
     */
    PageResult<PlatformProduct> listProducts(Long storeId, ProductQuery query);
    PlatformProduct getProduct(Long storeId, String platformSku);
    PlatformProduct createOrUpdateProduct(Long storeId, PlatformProduct product);

    /**
     * 订单相关
     */
    PageResult<PlatformOrder> listOrders(Long storeId, OrderQuery query);
    PlatformOrder getOrder(Long storeId, String platformOrderId);

    /**
     * 库存相关
     */
    InventorySyncResult syncInventory(Long storeId, List<InventorySyncRequest> requests);

    /**
     * 平台能力声明 - 未实现的能力可抛 UnsupportedOperationException
     */
    PlatformCapability getCapability();
}
