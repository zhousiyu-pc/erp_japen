package com.erp.jp.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单物流表
 * [by Agent]
 */
@Data
@TableName("t_order_shipping")
public class OrderShipping {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单 ID */
    private Long orderId;

    /** 订单号 */
    private String orderNo;

    /** 物流公司 */
    private String shippingCompany;

    /** 物流单号 */
    private String shippingNo;

    /** 物流状态：0-未发货 1-已发货 2-运输中 3-已签收 4-异常 */
    private Integer shippingStatus;

    /** 发货时间 */
    private LocalDateTime shippingTime;

    /** 签收时间 */
    private LocalDateTime receiveTime;

    /** 收货地址 */
    private String shippingAddress;

    /** 收货人 */
    private String shippingName;

    /** 收货人电话 */
    private String shippingPhone;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
