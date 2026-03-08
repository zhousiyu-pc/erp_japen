package com.erp.jp.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.product.entity.ProductSpu;
import com.erp.jp.product.mapper.ProductSpuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品 SPU 服务
 */
@Service
public class ProductSpuService extends ServiceImpl<ProductSpuMapper, ProductSpu> {
    
    /**
     * 创建商品 SPU
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductSpu createSpu(ProductSpu spu) {
        // TODO: 校验 SPU 编码唯一性
        // TODO: 校验租户配额
        save(spu);
        return spu;
    }
    
    /**
     * 更新商品状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long spuId, Integer status) {
        ProductSpu spu = new ProductSpu();
        spu.setSpuId(spuId);
        spu.setStatus(status);
        return updateById(spu);
    }
}
