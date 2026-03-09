package com.erp.jp.job.task;

import com.erp.jp.platform.api.PlatformAdapter;
import com.erp.jp.platform.api.PlatformCode;
import com.erp.jp.platform.api.PlatformRegistry;
import com.erp.jp.platform.api.dto.PageResult;
import com.erp.jp.platform.api.order.OrderQuery;
import com.erp.jp.platform.api.order.PlatformOrder;
import com.erp.jp.order.entity.OrderItem;
import com.erp.jp.order.entity.OrderMaster;
import com.erp.jp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 乐天订单同步任务
 * [by Agent]
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "erp.job.order-sync.enabled", havingValue = "true", matchIfMissing = true)
public class RakutenOrderSyncTask {

    private final PlatformRegistry platformRegistry;
    private final OrderService orderService;

    /**
     * 每 15 分钟同步一次乐天订单
     * 模拟模式：生成测试数据
     * 真实模式：调用乐天 API
     */
    @Scheduled(fixedRateString = "${erp.job.order-sync.rate:900000}")
    public void syncOrders() {
        log.info("====== 开始执行乐天订单同步任务 ======");

        try {
            // 获取乐天平台适配器（自动切换模拟/真实模式）
            PlatformAdapter adapter = platformRegistry.getAdapter(PlatformCode.RAKUTEN);
            if (adapter == null) {
                log.warn("乐天平台适配器未找到，跳过同步");
                return;
            }

            // 模拟店铺 ID（实际应从数据库获取所有乐天店铺）
            Long shopId = 1L;

            // 查询最近 30 分钟的订单
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusMinutes(30);

            OrderQuery query = new OrderQuery();
            query.setStartTime(startTime);
            query.setEndTime(endTime);
            query.setPage(1);
            query.setPageSize(100);

            // 调用平台 API 拉取订单
            PageResult<PlatformOrder> result = adapter.listOrders(shopId, query);
            List<PlatformOrder> orders = result.getData();

            if (orders == null || orders.isEmpty()) {
                log.info("未同步到新订单");
                return;
            }

            // 导入订单到系统
            int successCount = 0;
            for (PlatformOrder platformOrder : orders) {
                try {
                    OrderMaster order = toOrderMaster(platformOrder, shopId);
                    List<OrderItem> items = toOrderItems(platformOrder, order);

                    orderService.createOrder(order, items);
                    successCount++;
                } catch (Exception e) {
                    log.error("订单导入失败：platformOrderId={}", platformOrder.getPlatformOrderId(), e);
                }
            }

            log.info("====== 乐天订单同步完成：共{}单，成功{}单 ======", orders.size(), successCount);

        } catch (Exception e) {
            log.error("乐天订单同步任务执行失败", e);
        }
    }

    // 转换方法：PlatformOrder -> OrderMaster
    private OrderMaster toOrderMaster(PlatformOrder platformOrder, Long shopId) {
        OrderMaster order = new OrderMaster();
        order.setTenantId(1L);
        order.setShopId(shopId);
        order.setPlatformCode(platformOrder.getPlatformCode());
        order.setPlatformOrderId(platformOrder.getPlatformOrderId());
        order.setOrderType(1); // B2C
        order.setOrderTime(platformOrder.getOrderTime());
        order.setOrderStatus(10); // 待审核

        // 买家信息
        order.setBuyerName(platformOrder.getBuyerName());
        order.setBuyerEmail(platformOrder.getBuyerEmail());
        order.setBuyerPhone(platformOrder.getBuyerPhone());

        // 收货地址
        order.setShippingName(platformOrder.getShippingName());
        order.setShippingPhone(platformOrder.getShippingPhone());
        order.setShippingProvince(platformOrder.getShippingProvince());
        order.setShippingCity(platformOrder.getShippingCity());
        order.setShippingAddress(platformOrder.getShippingAddress());
        order.setShippingZip(platformOrder.getShippingZip());

        // 金额信息
        order.setTotalAmount(platformOrder.getTotalAmount());
        order.setPayAmount(platformOrder.getPayAmount() != null ? platformOrder.getPayAmount() : platformOrder.getTotalAmount());
        order.setFreightAmount(platformOrder.getFreightAmount() != null ? platformOrder.getFreightAmount() : BigDecimal.ZERO);
        order.setCurrency("JPY");

        // 原始数据
        order.setRawData("{\"platform\":\"RAKUTEN\",\"syncTime\":\"" + LocalDateTime.now() + "\"}");

        return order;
    }

    // 转换方法：PlatformOrder -> List<OrderItem>
    private List<OrderItem> toOrderItems(PlatformOrder platformOrder, OrderMaster order) {
        // 模拟订单项（实际应从 platformOrder.getItems() 获取）
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setOrderNo(order.getOrderNo());
        item.setProductName("乐天商品");
        item.setQuantity(1);
        item.setUnitPrice(order.getTotalAmount());
        item.setTotalPrice(order.getTotalAmount());
        item.setCurrency("JPY");
        return List.of(item);
    }
}
