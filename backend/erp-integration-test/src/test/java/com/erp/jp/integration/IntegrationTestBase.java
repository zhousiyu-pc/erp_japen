package com.erp.jp.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * 集成测试基类 - 提供 Testcontainers MySQL
 */
public abstract class IntegrationTestBase {

    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("erp_jp_crossborder")
            .withUsername("root")
            .withPassword("test")
            .withInitScript("init-test.sql")
            .withUrlParam("useUnicode", "true")
            .withUrlParam("characterEncoding", "utf8")
            .withUrlParam("serverTimezone", "Asia/Tokyo");

    static {
        MYSQL.start();
    }

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }
}
