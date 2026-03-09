package com.erp.jp.job.task;

import com.erp.jp.platform.api.PlatformAdapter;
import com.erp.jp.platform.api.PlatformCode;
import com.erp.jp.platform.api.PlatformRegistry;
import com.erp.jp.platform.api.dto.PageResult;
import com.erp.jp.platform.api.product.PlatformProduct;
import com.erp.jp.platform.api.product.ProductQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 乐天商品同步任务
 * [by Agent]
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "erp.job.product-sync.enabled", havingValue = "true", matchIfMissing = false)
public class RakutenProductSyncTask {

    private final PlatformRegistry platformRegistry;

    /**
     * 每小时同步一次商品数据
     */
    @Scheduled(fixedRateString = "${erp.job.product-sync.rate:3600000}")
    public void syncProducts() {
        log.info("====== 开始执行乐天商品同步任务 ======");

        try {
            PlatformAdapter adapter = platformRegistry.getAdapter(PlatformCode.RAKUTEN);
            if (adapter == null) {
                log.warn("乐天平台适配器未找到，跳过同步");
                return;
            }

            Long shopId = 1L;
            ProductQuery query = new ProductQuery();
            query.setPage(1);
            query.setPageSize(100);

            PageResult<PlatformProduct> result = adapter.listProducts(shopId, query);

            if (result.getData() == null || result.getData().isEmpty()) {
                log.info("未同步到新商品");
                return;
            }

            log.info("====== 乐天商品同步完成：共{}单 ======", result.getData().size());

        } catch (Exception e) {
            log.error("乐天商品同步任务执行失败", e);
        }
    }
}
