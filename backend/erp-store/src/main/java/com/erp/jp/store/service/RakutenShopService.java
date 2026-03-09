package com.erp.jp.store.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.jp.common.result.R;
import com.erp.jp.store.entity.RakutenShop;
import com.erp.jp.store.mapper.RakutenShopMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 乐天店铺配置服务
 * [by Agent]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RakutenShopService {

    private final RakutenShopMapper rakutenShopMapper;

    /**
     * 创建或更新乐天店铺配置
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> saveOrUpdate(Long storeId, RakutenShop shop) {
        // 检查是否已存在
        RakutenShop existing = rakutenShopMapper.selectOne(
                new LambdaQueryWrapper<RakutenShop>().eq(RakutenShop::getStoreId, storeId));
        
        if (existing != null) {
            // 更新
            shop.setId(existing.getId());
            shop.setUpdateTime(LocalDateTime.now());
            rakutenShopMapper.updateById(shop);
            log.info("乐天店铺配置更新：storeId={}", storeId);
            return R.ok(existing.getId());
        } else {
            // 新增
            shop.setStoreId(storeId);
            shop.setIsMock(1); // 默认模拟模式
            shop.setCreateTime(LocalDateTime.now());
            shop.setUpdateTime(LocalDateTime.now());
            rakutenShopMapper.insert(shop);
            log.info("乐天店铺配置创建：storeId={}", storeId);
            return R.ok(shop.getId());
        }
    }

    /**
     * 获取乐天店铺配置
     */
    public R<RakutenShop> getRakutenShop(Long storeId) {
        if (storeId == null) {
            return R.fail("店铺 ID 不能为空");
        }
        
        RakutenShop shop = rakutenShopMapper.selectOne(
                new LambdaQueryWrapper<RakutenShop>().eq(RakutenShop::getStoreId, storeId));
        
        if (shop == null) {
            return R.fail("乐天店铺配置不存在");
        }
        
        return R.ok(shop);
    }

    /**
     * 切换模拟/真实模式
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> toggleMockMode(Long storeId, Boolean isMock) {
        RakutenShop shop = rakutenShopMapper.selectOne(
                new LambdaQueryWrapper<RakutenShop>().eq(RakutenShop::getStoreId, storeId));
        
        if (shop == null) {
            return R.fail("乐天店铺配置不存在");
        }
        
        shop.setIsMock(isMock ? 1 : 0);
        shop.setUpdateTime(LocalDateTime.now());
        rakutenShopMapper.updateById(shop);
        
        log.info("乐天店铺模式切换：storeId={}, isMock={}", storeId, isMock);
        return R.ok();
    }

    /**
     * 更新 Token
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateToken(Long storeId, String accessToken, String refreshToken, LocalDateTime expireTime) {
        RakutenShop shop = rakutenShopMapper.selectOne(
                new LambdaQueryWrapper<RakutenShop>().eq(RakutenShop::getStoreId, storeId));
        
        if (shop == null) {
            return R.fail("乐天店铺配置不存在");
        }
        
        shop.setAccessToken(accessToken);
        shop.setRefreshToken(refreshToken);
        shop.setTokenExpireTime(expireTime);
        shop.setUpdateTime(LocalDateTime.now());
        rakutenShopMapper.updateById(shop);
        
        log.info("乐天店铺 Token 更新：storeId={}, expireTime={}", storeId, expireTime);
        return R.ok();
    }
}
