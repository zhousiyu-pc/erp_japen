package com.erp.jp.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.jp.order.dto.OrderDTO;
import com.erp.jp.order.entity.OrderMaster;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单导出服务
 * [by Agent]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderExportService {

    private final OrderService orderService;

    /**
     * 导出订单为 Excel
     */
    public byte[] exportToExcel(Long tenantId, Long shopId, String platformCode,
                                 LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // 查询订单数据
        Page<OrderDTO> orders = orderService.listOrders(tenantId, shopId, platformCode,
                null, startTime, endTime, 1, 10000);

        // 创建 Excel 工作簿
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("订单列表");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"订单号", "平台订单号", "店铺 ID", "平台", "状态", "类型",
                    "下单时间", "买家姓名", "收货人", "收货地址", "总金额", "币种"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // 填充数据
            int rowNum = 1;
            for (OrderDTO order : orders.getRecords()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getOrderNo());
                row.createCell(1).setCellValue(order.getPlatformOrderId());
                row.createCell(2).setCellValue(order.getShopId() != null ? order.getShopId().toString() : "");
                row.createCell(3).setCellValue(order.getPlatformCode());
                row.createCell(4).setCellValue(getStatusText(order.getOrderStatus()));
                row.createCell(5).setCellValue(order.getOrderType() == 1 ? "B2C" : "B2B");
                row.createCell(6).setCellValue(formatDateTime(order.getOrderTime()));
                row.createCell(7).setCellValue(order.getBuyerName());
                row.createCell(8).setCellValue(order.getShippingName());
                row.createCell(9).setCellValue(order.getShippingAddress());
                row.createCell(10).setCellValue(order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0);
                row.createCell(11).setCellValue(order.getCurrency());
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        return style;
    }

    private String getStatusText(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 10: return "待审核";
            case 20: return "已审核";
            case 30: return "已发货";
            case 40: return "已完成";
            case 0: return "已取消";
            case 50: return "售后中";
            default: return "未知";
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
