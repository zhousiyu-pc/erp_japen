package com.erp.jp.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.jp.store.entity.StoreMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMasterMapper extends BaseMapper<StoreMaster> {

    List<StoreMaster> selectStoreListWithAuth(@Param("tenantId") Long tenantId, @Param("platformCode") String platformCode);
}
