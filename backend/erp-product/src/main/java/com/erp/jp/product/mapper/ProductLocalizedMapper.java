package com.erp.jp.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.jp.product.entity.ProductLocalized;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品多语言 Mapper
 */
@Mapper
public interface ProductLocalizedMapper extends BaseMapper<ProductLocalized> {
}
