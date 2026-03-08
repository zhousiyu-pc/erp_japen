package com.erp.jp.platform.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 平台适配器注册表 - 自动收集所有 PlatformAdapter 实现
 * 业务层通过 getAdapter(platformCode) 获取，实现可插拔
 */
@Component
public class PlatformRegistry {

    private final Map<PlatformCode, PlatformAdapter> adapterMap;

    @Autowired(required = false)
    public PlatformRegistry(List<PlatformAdapter> adapters) {
        this.adapterMap = adapters == null ? Map.of() : adapters.stream()
                .collect(Collectors.toMap(PlatformAdapter::getPlatformCode, a -> a));
    }

    public Optional<PlatformAdapter> getAdapter(PlatformCode code) {
        return Optional.ofNullable(adapterMap.get(code));
    }

    public PlatformAdapter requireAdapter(PlatformCode code) {
        return getAdapter(code)
                .orElseThrow(() -> new IllegalArgumentException("Platform not supported: " + code.name()));
    }

    public PlatformAdapter getAdapter(String platformCode) {
        return getAdapter(PlatformCode.of(platformCode)).orElse(null);
    }

    public List<PlatformCode> getSupportedPlatforms() {
        return adapterMap.keySet().stream().toList();
    }
}
