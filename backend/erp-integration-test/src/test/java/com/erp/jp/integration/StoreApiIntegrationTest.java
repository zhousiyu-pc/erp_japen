package com.erp.jp.integration;

import com.erp.jp.store.StoreApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 店铺 API 集成测试 - 文档：店铺与渠道管理
 */
@SpringBootTest(
        classes = StoreApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class StoreApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("获取店铺列表 - 空列表应返回200")
    void listStores_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/store/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("接入新店铺 - 应成功创建")
    void createStore_shouldSucceed() throws Exception {
        String body = """
                {"storeName":"乐天测试店","platformCode":"RAKUTEN","sellerId":"SELLER001","isSandbox":true}
                """;
        mockMvc.perform(post("/api/v1/store/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNumber());
    }

    @Test
    @DisplayName("获取仓库列表 - 应返回种子数据")
    void listWarehouses_shouldReturnSeeded() throws Exception {
        mockMvc.perform(get("/api/v1/store/warehouses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].warehouseName").value("FBA (NRT1)"));
    }

    @Test
    @DisplayName("获取分组列表 - 应返回数组")
    void listGroups_shouldReturnArray() throws Exception {
        mockMvc.perform(get("/api/v1/store/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }
}
