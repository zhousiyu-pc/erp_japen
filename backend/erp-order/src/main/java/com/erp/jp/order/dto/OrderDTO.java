package com.erp.jp.order.dto;

import com.erp.jp.order.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单 DTO
 * [by Agent]
 */
@Data
public class OrderDTO {

    private Long id;
    private String orderNo;
    private Long shopId;
    private String platformCode;
    private String platformOrderId;
    private Integer orderStatus;
    private Integer orderType;
    private LocalDateTime orderTime;
    private String buyerName;
    private String buyerEmail;
    private String shippingName;
    private String shippingProvince;
    private String shippingCity;
    private String shippingAddress;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private String currency;
    private List<OrderItem> items;
}
