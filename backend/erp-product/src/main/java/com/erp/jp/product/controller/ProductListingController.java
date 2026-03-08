package com.erp.jp.product.controller;

import com.erp.jp.product.entity.DistributeTask;
import com.erp.jp.product.entity.ProductListing;
import com.erp.jp.product.service.DistributeTaskService;
import com.erp.jp.product.service.ProductListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺商品映射控制器
 */
@RestController
@RequestMapping("/api/v1/product/listing")
public class ProductListingController {
    
    @Autowired
    private ProductListingService listingService;
    
    @Autowired
    private DistributeTaskService distributeTaskService;
    
    /**
     * 创建店铺商品映射
     */
    @PostMapping
    public ProductListing create(@RequestBody ProductListing listing) {
        return listingService.createListing(listing);
    }
    
    /**
     * 根据店铺查询商品列表
     */
    @GetMapping("/shop/{shopId}")
    public List<ProductListing> listByShopId(@PathVariable Long shopId) {
        return listingService.listByShopId(shopId);
    }
    
    /**
     * 根据 SPU 查询所有店铺映射
     */
    @GetMapping("/spu/{spuId}")
    public List<ProductListing> listBySpuId(@PathVariable Long spuId) {
        return listingService.listBySpuId(spuId);
    }
    
    /**
     * 更新同步状态
     */
    @PutMapping("/{listingId}/status")
    public boolean updateStatus(
            @PathVariable Long listingId,
            @RequestParam Integer syncStatus) {
        return listingService.updateSyncStatus(listingId, syncStatus);
    }
    
    /**
     * 创建分发任务
     */
    @PostMapping("/distribute")
    public DistributeTask createDistributeTask(
            @RequestParam String taskType,
            @RequestParam Integer totalCount,
            @RequestParam(required = false) String createBy) {
        return distributeTaskService.createTask(taskType, totalCount, createBy);
    }
    
    /**
     * 查询任务进度
     */
    @GetMapping("/task/{taskId}")
    public DistributeTask getTaskProgress(@PathVariable String taskId) {
        return distributeTaskService.getByTaskId(taskId);
    }
    
    /**
     * 更新任务进度
     */
    @PutMapping("/task/{taskId}/progress")
    public boolean updateTaskProgress(
            @PathVariable String taskId,
            @RequestParam int successCount,
            @RequestParam int failCount) {
        return distributeTaskService.updateProgress(taskId, successCount, failCount);
    }
}
