package com.erp.jp.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表
 * [by Agent]
 */
@Data
@TableName("t_order_master")
public class OrderMaster {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 租户 ID */
    private Long tenantId;

    /** 店铺 ID */
    private Long shopId;

    /** 平台编码：RAKUTEN/AMAZON_JP/YAHOO_JP */
    private String platformCode;

    /** 平台订单 ID */
    private String platformOrderId;

    /** 订单状态：10-待审核 20-已审核 30-已发货 40-已完成 0-已取消 50-售后中 */
    private Integer orderStatus;

    /** 订单类型：1-B2C 2-B2B */
    private Integer orderType;

    /** 下单时间 */
    private LocalDateTime orderTime;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 发货时间 */
    private LocalDateTime shipTime;

    /** 完成时间 */
    private LocalDateTime completeTime;

    /** 买家姓名 */
    private String buyerName;

    /** 买家邮箱 */
    private String buyerEmail;

    /** 买家电话 */
    private String buyerPhone;

    /** 收货人姓名 */
    private String shippingName;

    /** 收货人电话 */
    private String shippingPhone;

    /** 都道府县 */
    private String shippingProvince;

    /** 市区町村 */
    private String shippingCity;

    /** 区/街道 */
    private String shippingDistrict;

    /** 详细地址 */
    private String shippingAddress;

    /** 邮编 */
    private String shippingZip;

    /** 订单总额（JPY） */
    private BigDecimal totalAmount;

    /** 实付金额（JPY） */
    private BigDecimal payAmount;

    /** 运费（JPY） */
    private BigDecimal freightAmount;

    /** 优惠金额（JPY） */
    private BigDecimal discountAmount;

    /** 币种 */
    private String currency;

    /** 备注 */
    private String remark;

    /** 平台备注 */
    private String platformRemark;

    /** 原始数据 JSON */
    private String rawData;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除 */
    @TableLogic
    private Integer isDeleted;
}
