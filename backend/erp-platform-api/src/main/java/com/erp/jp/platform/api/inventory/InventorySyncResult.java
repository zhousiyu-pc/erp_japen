package com.erp.jp.platform.api.inventory;

import java.util.List;

/**
 * 库存同步结果
 */
public class InventorySyncResult {
    private final boolean success;
    private final int successCount;
    private final int failCount;
    private final List<String> failedSkus;

    public InventorySyncResult(boolean success, int successCount, int failCount, List<String> failedSkus) {
        this.success = success;
        this.successCount = successCount;
        this.failCount = failCount;
        this.failedSkus = failedSkus != null ? failedSkus : List.of();
    }

    public static InventorySyncResult of(boolean success, int successCount, int failCount, List<String> failedSkus) {
        return new InventorySyncResult(success, successCount, failCount, failedSkus);
    }

    public boolean isSuccess() { return success; }
    public int getSuccessCount() { return successCount; }
    public int getFailCount() { return failCount; }
    public List<String> getFailedSkus() { return failedSkus; }
}
