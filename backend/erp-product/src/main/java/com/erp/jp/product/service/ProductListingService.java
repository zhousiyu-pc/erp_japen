package com.erp.jp.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.product.entity.ProductListing;
import com.erp.jp.product.mapper.ProductListingMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺商品映射服务
 */
@Service
public class ProductListingService extends ServiceImpl<ProductListingMapper, ProductListing> {
    
    /**
     * 创建店铺商品映射
     */
    public ProductListing createListing(ProductListing listing) {
        save(listing);
        return listing;
    }
    
    /**
     * 根据店铺 ID 查询商品列表
     */
    public List<ProductListing> listByShopId(Long shopId) {
        return lambdaQuery()
            .eq(ProductListing::getShopId, shopId)
            .list();
    }
    
    /**
     * 根据 SPU ID 查询所有店铺映射
     */
    public List<ProductListing> listBySpuId(Long spuId) {
        return lambdaQuery()
            .eq(ProductListing::getSpuId, spuId)
            .list();
    }
    
    /**
     * 更新同步状态
     */
    public boolean updateSyncStatus(Long listingId, Integer syncStatus) {
        ProductListing listing = new ProductListing();
        listing.setListingId(listingId);
        listing.setSyncStatus(syncStatus);
        return updateById(listing);
    }
}
