package com.erp.jp.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.jp.common.result.R;
import com.erp.jp.order.dto.OrderDTO;
import com.erp.jp.order.entity.OrderItem;
import com.erp.jp.order.entity.OrderMaster;
import com.erp.jp.order.mapper.OrderItemMapper;
import com.erp.jp.order.mapper.OrderMasterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务
 * [by Agent]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMasterMapper orderMasterMapper;
    private final OrderItemMapper orderItemMapper;

    /**
     * 订单列表（分页）
     */
    public R<Page<OrderDTO>> listOrders(Long tenantId, Long shopId, String platformCode,
                                         Integer orderStatus, LocalDateTime startTime, LocalDateTime endTime,
                                         int page, int size) {
        if (tenantId == null) tenantId = 1L;

        Page<OrderMaster> orderPage = new Page<>(page, size);
        LambdaQueryWrapper<OrderMaster> wrapper = new LambdaQueryWrapper<OrderMaster>()
                .eq(OrderMaster::getTenantId, tenantId)
                .eq(shopId != null, OrderMaster::getShopId, shopId)
                .eq(platformCode != null && !platformCode.equals("all"), OrderMaster::getPlatformCode, platformCode)
                .eq(orderStatus != null, OrderMaster::getOrderStatus, orderStatus)
                .ge(startTime != null, OrderMaster::getOrderTime, startTime)
                .le(endTime != null, OrderMaster::getOrderTime, endTime)
                .orderByDesc(OrderMaster::getOrderTime);

        Page<OrderMaster> result = orderMasterMapper.selectPage(orderPage, wrapper);

        // 转换为 DTO
        List<OrderDTO> dtoList = result.getRecords().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        Page<OrderDTO> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(dtoList);

        return R.ok(dtoPage);
    }

    /**
     * 订单详情
     */
    public R<OrderDTO> getOrderDetail(Long orderId) {
        OrderMaster order = orderMasterMapper.selectById(orderId);
        if (order == null) {
            return R.fail("订单不存在");
        }

        OrderDTO dto = toDTO(order);

        // 查询订单明细
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        dto.setItems(items);

        return R.ok(dto);
    }

    /**
     * 创建订单（从平台同步）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> createOrder(OrderMaster order, List<OrderItem> items) {
        // 检查订单是否已存在
        Long count = orderMasterMapper.selectCount(
                new LambdaQueryWrapper<OrderMaster>()
                        .eq(OrderMaster::getPlatformCode, order.getPlatformCode())
                        .eq(OrderMaster::getPlatformOrderId, order.getPlatformOrderId()));

        if (count > 0) {
            return R.fail("订单已存在");
        }

        // 生成内部订单号
        String orderNo = "ORD" + System.currentTimeMillis() + (int)(Math.random() * 10000);
        order.setOrderNo(orderNo);
        order.setOrderStatus(10); // 待审核
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setIsDeleted(0);

        orderMasterMapper.insert(order);

        // 插入订单明细
        if (items != null && !items.isEmpty()) {
            for (OrderItem item : items) {
                item.setOrderId(order.getId());
                item.setOrderNo(orderNo);
                orderItemMapper.insert(item);
            }
        }

        return R.ok(order.getId());
    }

    /**
     * 更新订单状态
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateOrderStatus(Long orderId, Integer newStatus) {
        OrderMaster order = orderMasterMapper.selectById(orderId);
        if (order == null) {
            return R.fail("订单不存在");
        }

        // 状态机校验
        if (!isValidStatusTransition(order.getOrderStatus(), newStatus)) {
            return R.fail("订单状态流转不合法");
        }

        order.setOrderStatus(newStatus);
        order.setUpdateTime(LocalDateTime.now());

        // 根据状态设置时间
        if (newStatus == 20) {
            order.setPayTime(LocalDateTime.now());
        } else if (newStatus == 30) {
            order.setShipTime(LocalDateTime.now());
        } else if (newStatus == 40) {
            order.setCompleteTime(LocalDateTime.now());
        }

        orderMasterMapper.updateById(order);
        return R.ok();
    }

    /**
     * 批量导入乐天订单（模拟数据）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Integer> importRakutenOrders(Long shopId, int count) {
        int successCount = 0;

        for (int i = 0; i < count; i++) {
            // 创建模拟订单
            OrderMaster order = new OrderMaster();
            order.setTenantId(1L);
            order.setShopId(shopId);
            order.setPlatformCode("RAKUTEN");
            order.setPlatformOrderId("R" + System.currentTimeMillis() + "_" + i);
            order.setOrderType(1);
            order.setOrderTime(LocalDateTime.now().minusDays((long)(Math.random() * 30)));
            order.setBuyerName("山田" + i);
            order.setBuyerEmail("yamada" + i + "@example.jp");
            order.setBuyerPhone("03-1234-567" + i);
            order.setShippingName("山田" + i);
            order.setShippingProvince("東京都");
            order.setShippingCity("渋谷区");
            order.setShippingAddress("宇田川町 1-" + i);
            order.setShippingZip("150-0042");
            order.setTotalAmount(new BigDecimal("5000").add(new BigDecimal(i * 500)));
            order.setPayAmount(order.getTotalAmount());
            order.setFreightAmount(new BigDecimal("500"));
            order.setCurrency("JPY");
            order.setRawData("{\"mock\":true,\"index\":" + i + "}");

            // 创建模拟订单项
            List<OrderItem> items = List.of(
                createMockItem(order, "模拟商品_" + i, 1, 4500.0)
            );

            R<Long> result = createOrder(order, items);
            if (result.getCode() == 200) {
                successCount++;
            }
        }

        return R.ok(successCount);
    }

    // 辅助方法
    private OrderDTO toDTO(OrderMaster order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setShopId(order.getShopId());
        dto.setPlatformCode(order.getPlatformCode());
        dto.setPlatformOrderId(order.getPlatformOrderId());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setOrderType(order.getOrderType());
        dto.setOrderTime(order.getOrderTime());
        dto.setBuyerName(order.getBuyerName());
        dto.setBuyerEmail(order.getBuyerEmail());
        dto.setShippingName(order.getShippingName());
        dto.setShippingProvince(order.getShippingProvince());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPayAmount(order.getPayAmount());
        dto.setCurrency(order.getCurrency());
        return dto;
    }

    private OrderItem createMockItem(OrderMaster order, String productName, int qty, double price) {
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setOrderNo(order.getOrderNo());
        item.setProductName(productName);
        item.setQuantity(qty);
        item.setUnitPrice(new BigDecimal(price));
        item.setTotalPrice(new BigDecimal(price * qty));
        item.setCurrency("JPY");
        return item;
    }

    /**
     * 状态机校验
     */
    private boolean isValidStatusTransition(Integer fromStatus, Integer toStatus) {
        // 10-待审核 20-已审核 30-已发货 40-已完成 0-已取消 50-售后中
        if (fromStatus == null || toStatus == null) return false;

        // 允许的状态流转
        return (fromStatus == 10 && toStatus == 20) ||  // 待审核→已审核
               (fromStatus == 10 && toStatus == 0)  ||  // 待审核→已取消
               (fromStatus == 20 && toStatus == 30) ||  // 已审核→已发货
               (fromStatus == 20 && toStatus == 0)  ||  // 已审核→已取消
               (fromStatus == 30 && toStatus == 40) ||  // 已发货→已完成
               (fromStatus == 30 && toStatus == 50) ||  // 已发货→售后中
               (fromStatus == 40 && toStatus == 50);    // 已完成→售后中
    }
}
