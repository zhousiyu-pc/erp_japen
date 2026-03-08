package com.erp.jp.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.product.entity.ProductSku;
import com.erp.jp.product.mapper.ProductSkuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品 SKU 服务
 */
@Service
public class ProductSkuService extends ServiceImpl<ProductSkuMapper, ProductSku> {
    
    /**
     * 创建商品 SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductSku createSku(ProductSku sku) {
        // TODO: 校验 SKU 编码唯一性
        // TODO: 校验 JAN 码格式
        save(sku);
        return sku;
    }
    
    /**
     * 批量创建 SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateSkus(List<ProductSku> skus) {
        return saveBatch(skus);
    }
    
    /**
     * 根据 SPU ID 查询 SKU 列表
     */
    public List<ProductSku> listBySpuId(Long spuId) {
        return lambdaQuery()
            .eq(ProductSku::getSpuId, spuId)
            .list();
    }
}
