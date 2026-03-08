package com.erp.jp.integration;

import com.erp.jp.system.SystemApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 组织结构 API 集成测试 - 文档：账号与组织管理-组织结构配置 TC01~TC06
 */
@SpringBootTest(
        classes = SystemApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class OrgApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("TC01: 获取组织树 - 应返回种子数据树结构")
    void getOrgTree_shouldReturnTree() throws Exception {
        mockMvc.perform(get("/api/v1/system/org/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].label").value("总经办"))
                .andExpect(jsonPath("$.data[0].orgCode").value("HQ"))
                .andExpect(jsonPath("$.data[0].children[0].label").value("运营中心"))
                .andExpect(jsonPath("$.data[0].children[0].children[0].label").value("日本 Amazon 一部"));
    }

    @Test
    @DisplayName("TC02: 新增根组织 - 应成功创建")
    void createRootOrg_shouldSucceed() throws Exception {
        String body = """
                {"orgNameJa":"本社","orgNameEn":"HQ_TC02","orgCode":"HQ_TC02","sortOrder":0,"status":0}
                """;
        mockMvc.perform(post("/api/v1/system/org/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNumber());

        mockMvc.perform(get("/api/v1/system/org/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].label", org.hamcrest.Matchers.hasItems("本社", "总经办")));
    }

    @Test
    @DisplayName("TC03: 新增子组织 - 应在父节点下创建")
    void createChildOrg_shouldSucceed() throws Exception {
        String body = """
                {"orgNameJa":"楽天事業部","orgNameEn":"RAK","orgCode":"RAK_TC03","parentOrgId":1,"sortOrder":1,"status":0}
                """;
        mockMvc.perform(post("/api/v1/system/org/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/v1/system/org/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].children[*].label", org.hamcrest.Matchers.hasItem("楽天事業部")));
    }

    @Test
    @DisplayName("TC04: 组织编码唯一性校验 - 重复编码应失败")
    void createOrg_duplicateCode_shouldFail() throws Exception {
        String body = """
                {"orgNameJa":"重复测试","orgCode":"HQ","sortOrder":0,"status":0}
                """;
        mockMvc.perform(post("/api/v1/system/org/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("编码")));
    }

    @Test
    @DisplayName("TC05: 删除含有子组织的节点 - 应禁止删除")
    void deleteOrg_withChildren_shouldFail() throws Exception {
        mockMvc.perform(delete("/api/v1/system/org/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("子组织")));
    }

    @Test
    @DisplayName("TC06: 删除叶子节点 - 应成功")
    void deleteLeafOrg_shouldSucceed() throws Exception {
        // 先创建一个叶子节点
        String createBody = """
                {"orgNameJa":"待删叶子","orgCode":"DEL_LEAF","parentOrgId":5,"sortOrder":99,"status":0}
                """;
        MvcResult createRes = mockMvc.perform(post("/api/v1/system/org/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        JsonNode node = objectMapper.readTree(createRes.getResponse().getContentAsString());
        long id = node.get("data").asLong();

        mockMvc.perform(delete("/api/v1/system/org/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("更新组织节点 - 应成功")
    void updateOrg_shouldSucceed() throws Exception {
        String body = """
                {"orgNameJa":"财务部-已更新","orgCode":"FIN","status":0}
                """;
        mockMvc.perform(put("/api/v1/system/org/update/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/v1/system/org/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].children[*].label", org.hamcrest.Matchers.hasItem("财务部-已更新")));
    }
}
