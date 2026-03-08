package com.erp.jp.product.service;

import com.erp.jp.product.entity.ProductSpu;
import com.erp.jp.product.entity.ProductSku;
import com.erp.jp.product.entity.ProductLocalized;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Product 服务层测试
 */
@SpringBootTest
public class ProductServiceTest {
    
    @Autowired
    private ProductSpuService spuService;
    
    @Autowired
    private ProductSkuService skuService;
    
    @Autowired
    private ProductLocalizedService localizedService;
    
    /**
     * 测试创建 SPU
     */
    @Test
    public void testCreateSpu() {
        ProductSpu spu = new ProductSpu();
        spu.setTenantId(1L);
        spu.setSpuCode("SPU-TEST-001");
        spu.setProductName("测试商品");
        spu.setBasePrice(new BigDecimal("1000.00"));
        spu.setCurrency("JPY");
        spu.setStatus(0);
        
        ProductSpu saved = spuService.createSpu(spu);
        assertNotNull(saved.getSpuId());
        assertEquals("SPU-TEST-001", saved.getSpuCode());
    }
    
    /**
     * 测试创建 SKU
     */
    @Test
    public void testCreateSku() {
        ProductSku sku = new ProductSku();
        sku.setTenantId(1L);
        sku.setSpuId(1L);
        sku.setSkuCode("SKU-TEST-001");
        sku.setJanCode("4900001234567");
        sku.setCostPrice(new BigDecimal("500.00"));
        sku.setSalePrice(new BigDecimal("1000.00"));
        sku.setWeight(new BigDecimal("200.00"));
        sku.setStatus(1);
        
        ProductSku saved = skuService.createSku(sku);
        assertNotNull(saved.getSkuId());
        assertEquals("4900001234567", saved.getJanCode());
    }
    
    /**
     * 测试保存多语言文案
     */
    @Test
    public void testSaveLocalized() {
        ProductLocalized localized = new ProductLocalized();
        localized.setTenantId(1L);
        localized.setSpuId(1L);
        localized.setLocale("ja_JP");
        localized.setTitle("テスト商品");
        localized.setSubtitle("テスト副标题");
        localized.setBullet1("卖点 1");
        localized.setDescription("商品描述");
        
        ProductLocalized saved = localizedService.saveOrUpdateLocalized(localized);
        assertNotNull(saved.getId());
        assertEquals("ja_JP", saved.getLocale());
    }
    
    /**
     * 测试查询多语言文案
     */
    @Test
    public void testGetLocalized() {
        ProductLocalized localized = localizedService.getBySpuAndLocale(1L, "ja_JP");
        if (localized != null) {
            assertEquals("ja_JP", localized.getLocale());
        }
    }
}
