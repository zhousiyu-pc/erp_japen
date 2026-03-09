package com.erp.jp.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.jp.common.result.R;
import com.erp.jp.order.dto.OrderDTO;
import com.erp.jp.order.service.OrderExportService;
import com.erp.jp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 订单管理 API
 * [by Agent]
 */
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderExportService orderExportService;

    /**
     * 订单列表（分页）
     */
    @GetMapping("/orders")
    public R<Page<OrderDTO>> listOrders(
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false, defaultValue = "all") String platformCode,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.listOrders(tenantId, shopId, platformCode, orderStatus, startTime, endTime, page, size);
    }

    /**
     * 订单详情
     */
    @GetMapping("/orders/{id}")
    public R<OrderDTO> getOrderDetail(@PathVariable Long id) {
        return orderService.getOrderDetail(id);
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/orders/{id}/status")
    public R<Void> updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        return orderService.updateOrderStatus(id, status);
    }

    /**
     * 导入乐天订单（模拟）
     */
    @PostMapping("/orders/rakuten/import")
    public R<Integer> importRakutenOrders(@RequestParam Long shopId,
                                           @RequestParam(defaultValue = "10") int count) {
        return orderService.importRakutenOrders(shopId, count);
    }

    /**
     * 导出订单为 Excel
     */
    @GetMapping("/orders/export")
    public ResponseEntity<byte[]> exportOrders(
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false) String platformCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            byte[] excelData = orderExportService.exportToExcel(tenantId, shopId, platformCode, startTime, endTime);

            String fileName = "订单导出_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelData);
        } catch (Exception e) {
            log.error("订单导出失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
