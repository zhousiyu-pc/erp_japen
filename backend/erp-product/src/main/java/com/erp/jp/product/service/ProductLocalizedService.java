package com.erp.jp.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.product.entity.ProductLocalized;
import com.erp.jp.product.mapper.ProductLocalizedMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品多语言服务
 */
@Service
public class ProductLocalizedService extends ServiceImpl<ProductLocalizedMapper, ProductLocalized> {
    
    /**
     * 保存或更新多语言文案
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductLocalized saveOrUpdateLocalized(ProductLocalized localized) {
        ProductLocalized exist = lambdaQuery()
            .eq(ProductLocalized::getSpuId, localized.getSpuId())
            .eq(ProductLocalized::getLocale, localized.getLocale())
            .one();
        
        if (exist != null) {
            localized.setId(exist.getId());
            updateById(localized);
        } else {
            save(localized);
        }
        return localized;
    }
    
    /**
     * 根据 SPU ID 和语言查询文案
     */
    public ProductLocalized getBySpuAndLocale(Long spuId, String locale) {
        return lambdaQuery()
            .eq(ProductLocalized::getSpuId, spuId)
            .eq(ProductLocalized::getLocale, locale)
            .one();
    }
}
