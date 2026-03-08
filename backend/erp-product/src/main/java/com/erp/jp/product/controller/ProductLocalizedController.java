package com.erp.jp.product.controller;

import com.erp.jp.product.dto.ProductLocalizedRequest;
import com.erp.jp.product.dto.ProductLocalizedResponse;
import com.erp.jp.product.entity.ProductLocalized;
import com.erp.jp.product.service.ProductLocalizedService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商品多语言文案控制器
 */
@RestController
@RequestMapping("/api/v1/product/localized")
public class ProductLocalizedController {
    
    @Autowired
    private ProductLocalizedService localizedService;
    
    /**
     * 保存或更新多语言文案
     */
    @PostMapping
    public ProductLocalizedResponse saveOrUpdate(@RequestBody @Validated ProductLocalizedRequest request) {
        ProductLocalized localized = new ProductLocalized();
        BeanUtils.copyProperties(request, localized);
        localizedService.saveOrUpdateLocalized(localized);
        
        ProductLocalizedResponse response = new ProductLocalizedResponse();
        BeanUtils.copyProperties(localized, response);
        return response;
    }
    
    /**
     * 查询指定语言的文案
     */
    @GetMapping("/{spuId}/{locale}")
    public ProductLocalizedResponse getBySpuAndLocale(
            @PathVariable Long spuId,
            @PathVariable String locale) {
        ProductLocalized localized = localizedService.getBySpuAndLocale(spuId, locale);
        ProductLocalizedResponse response = new ProductLocalizedResponse();
        BeanUtils.copyProperties(localized, response);
        return response;
    }
    
    /**
     * 查询 SPU 的所有语言文案
     */
    @GetMapping("/{spuId}")
    public Object listBySpuId(@PathVariable Long spuId) {
        return localizedService.lambdaQuery()
            .eq(ProductLocalized::getSpuId, spuId)
            .list();
    }
}
