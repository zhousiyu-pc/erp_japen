package com.erp.jp.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.jp.common.exception.ErrorCode;
import com.erp.jp.common.exception.NotFoundException;
import com.erp.jp.common.result.R;
import com.erp.jp.product.dto.ProductSpuCreateRequest;
import com.erp.jp.product.dto.ProductSpuResponse;
import com.erp.jp.product.entity.ProductSpu;
import com.erp.jp.product.service.ProductSpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商品 SPU 控制器
 */
@RestController
@RequestMapping("/api/v1/product/spu")
public class ProductSpuController {
    
    @Autowired
    private ProductSpuService spuService;
    
    /**
     * 创建商品 SPU
     */
    @PostMapping
    public R<ProductSpuResponse> create(@RequestBody @Validated ProductSpuCreateRequest request) {
        ProductSpu spu = new ProductSpu();
        BeanUtils.copyProperties(request, spu);
        spuService.createSpu(spu);
        
        ProductSpuResponse response = new ProductSpuResponse();
        BeanUtils.copyProperties(spu, response);
        return R.ok(response);
    }
    
    /**
     * 查询商品 SPU 详情
     */
    @GetMapping("/{spuId}")
    public R<ProductSpuResponse> getById(@PathVariable Long spuId) {
        ProductSpu spu = spuService.getById(spuId);
        if (spu == null) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "商品不存在");
        }
        
        ProductSpuResponse response = new ProductSpuResponse();
        BeanUtils.copyProperties(spu, response);
        return R.ok(response);
    }
    
    /**
     * 分页查询商品 SPU 列表
     */
    @GetMapping("/page")
    public R<Page<ProductSpu>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        
        Page<ProductSpu> page = new Page<>(current, size);
        if (status != null) {
            return R.ok(spuService.lambdaQuery()
                .eq(ProductSpu::getStatus, status)
                .page(page));
        }
        return R.ok(spuService.page(page));
    }
    
    /**
     * 更新商品状态
     */
    @PutMapping("/{spuId}/status")
    public R<Void> updateStatus(
            @PathVariable Long spuId,
            @RequestParam Integer status) {
        spuService.updateStatus(spuId, status);
        return R.ok();
    }
    
    /**
     * 删除商品 SPU
     */
    @DeleteMapping("/{spuId}")
    public R<Void> delete(@PathVariable Long spuId) {
        spuService.removeById(spuId);
        return R.ok();
    }
}
