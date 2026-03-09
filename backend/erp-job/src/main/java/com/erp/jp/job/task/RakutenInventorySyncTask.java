package com.erp.jp.job.task;

import com.erp.jp.inventory.service.InventoryService;
import com.erp.jp.platform.api.PlatformAdapter;
import com.erp.jp.platform.api.PlatformCode;
import com.erp.jp.platform.api.PlatformRegistry;
import com.erp.jp.platform.api.inventory.InventorySyncRequest;
import com.erp.jp.platform.api.inventory.InventorySyncResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 乐天库存同步任务
 * [by Agent]
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "erp.job.inventory-sync.enabled", havingValue = "true", matchIfMissing = true)
public class RakutenInventorySyncTask {

    private final PlatformRegistry platformRegistry;
    private final InventoryService inventoryService;

    /**
     * 每 30 分钟同步一次库存到乐天
     * 模拟模式：直接返回成功
     * 真实模式：调用乐天 API 更新库存
     */
    @Scheduled(fixedRateString = "${erp.job.inventory-sync.rate:1800000}")
    public void syncInventory() {
        log.info("====== 开始执行乐天库存同步任务 ======");

        try {
            // 获取乐天平台适配器
            PlatformAdapter adapter = platformRegistry.getAdapter(PlatformCode.RAKUTEN);
            if (adapter == null) {
                log.warn("乐天平台适配器未找到，跳过同步");
                return;
            }

            // 模拟店铺 ID 和库存数据（实际应从数据库查询）
            Long shopId = 1L;

            // 构建库存同步请求
            InventorySyncRequest request = new InventorySyncRequest();
            request.setSkuId(1L);
            request.setPlatformSkuId("mock_sku_001");
            request.setQuantity(100); // 同步库存数量为 100

            List<InventorySyncRequest> requests = List.of(request);

            // 调用平台 API 同步库存
            InventorySyncResult result = adapter.syncInventory(shopId, requests);

            if (result.isSuccess()) {
                log.info("====== 乐天库存同步完成：成功{}单，失败{}单 ======",
                        result.getSuccessCount(), result.getFailCount());
            } else {
                log.error("乐天库存同步失败：{}", result.getMessage());
            }

        } catch (Exception e) {
            log.error("乐天库存同步任务执行失败", e);
        }
    }
}
