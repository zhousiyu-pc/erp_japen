package com.erp.jp.product.controller;

import com.erp.jp.product.dto.ProductSpuCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Product 控制器测试
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 测试创建 SPU
     */
    @Test
    public void testCreateSpu() throws Exception {
        ProductSpuCreateRequest request = new ProductSpuCreateRequest();
        request.setSpuCode("SPU-API-TEST-001");
        request.setProductName("API 测试商品");
        request.setBasePrice(new BigDecimal("2000.00"));
        request.setCurrency("JPY");
        
        mockMvc.perform(post("/api/v1/product/spu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.spuCode").value("SPU-API-TEST-001"));
    }
    
    /**
     * 测试查询 SPU 列表
     */
    @Test
    public void testPageSpu() throws Exception {
        mockMvc.perform(get("/api/v1/product/spu/page")
                .param("current", "1")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.records").isArray());
    }
    
    /**
     * 测试验证失败
     */
    @Test
    public void testCreateSpuValidationFail() throws Exception {
        ProductSpuCreateRequest request = new ProductSpuCreateRequest();
        // 不填必填字段
        
        mockMvc.perform(post("/api/v1/product/spu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is4xxClientError());
    }
}
