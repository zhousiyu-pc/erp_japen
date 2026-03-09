package com.erp.jp.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 乐天店铺配置表
 * [by Agent]
 */
@Data
@TableName("t_rakuten_shop")
public class RakutenShop {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联店铺 ID */
    private Long storeId;

    /** 乐天 App ID */
    private String appId;

    /** 乐天 App Secret(加密) */
    private String appSecret;

    /** 访问令牌 (加密) */
    private String accessToken;

    /** 刷新令牌 (加密) */
    private String refreshToken;

    /** 令牌过期时间 */
    private LocalDateTime tokenExpireTime;

    /** 乐天店铺名称 */
    private String shopName;

    /** 乐天店铺编号 */
    private String shopNumber;

    /** 是否模拟模式：0-真实 1-模拟 */
    private Integer isMock;

    /** 同步配置 JSON */
    private String syncConfig;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
