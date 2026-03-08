package com.erp.jp.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细表
 * [by Agent]
 */
@Data
@TableName("t_order_item")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单 ID */
    private Long orderId;

    /** 订单号 */
    private String orderNo;

    /** 商品 ID */
    private Long productId;

    /** SKU ID */
    private Long skuId;

    /** 平台商品 ID */
    private String platformProductId;

    /** 平台 SKU ID */
    private String platformSkuId;

    /** 商品名称 */
    private String productName;

    /** 商品图片 */
    private String productImage;

    /** SKU 规格 */
    private String skuSpec;

    /** 数量 */
    private Integer quantity;

    /** 单价（JPY） */
    private BigDecimal unitPrice;

    /** 总价（JPY） */
    private BigDecimal totalPrice;

    /** 币种 */
    private String currency;
}
