package com.erp.jp.product.controller;

import com.erp.jp.product.dto.ProductSkuCreateRequest;
import com.erp.jp.product.dto.ProductSkuResponse;
import com.erp.jp.product.entity.ProductSku;
import com.erp.jp.product.service.ProductSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品 SKU 控制器
 */
@RestController
@RequestMapping("/api/v1/product/sku")
public class ProductSkuController {
    
    @Autowired
    private ProductSkuService skuService;
    
    /**
     * 创建商品 SKU
     */
    @PostMapping
    public ProductSkuResponse create(@RequestBody @Validated ProductSkuCreateRequest request) {
        ProductSku sku = new ProductSku();
        BeanUtils.copyProperties(request, sku);
        skuService.createSku(sku);
        
        ProductSkuResponse response = new ProductSkuResponse();
        BeanUtils.copyProperties(sku, response);
        return response;
    }
    
    /**
     * 批量创建 SKU
     */
    @PostMapping("/batch")
    public boolean batchCreate(@RequestBody @Validated List<ProductSkuCreateRequest> requests) {
        List<ProductSku> skus = requests.stream()
            .map(req -> {
                ProductSku sku = new ProductSku();
                BeanUtils.copyProperties(req, sku);
                return sku;
            })
            .collect(Collectors.toList());
        return skuService.batchCreateSkus(skus);
    }
    
    /**
     * 根据 SPU ID 查询 SKU 列表
     */
    @GetMapping("/spu/{spuId}")
    public List<ProductSkuResponse> listBySpuId(@PathVariable Long spuId) {
        List<ProductSku> skus = skuService.listBySpuId(spuId);
        return skus.stream()
            .map(sku -> {
                ProductSkuResponse response = new ProductSkuResponse();
                BeanUtils.copyProperties(sku, response);
                return response;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 更新 SKU 状态
     */
    @PutMapping("/{skuId}/status")
    public boolean updateStatus(
            @PathVariable Long skuId,
            @RequestParam Integer status) {
        ProductSku sku = new ProductSku();
        sku.setSkuId(skuId);
        sku.setStatus(status);
        return skuService.updateById(sku);
    }
    
    /**
     * 删除 SKU
     */
    @DeleteMapping("/{skuId}")
    public boolean delete(@PathVariable Long skuId) {
        return skuService.removeById(skuId);
    }
}
