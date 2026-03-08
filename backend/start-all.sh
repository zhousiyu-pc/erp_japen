#!/bin/bash
# 一键启动所有后端微服务
# 启动顺序：gateway 需最先启动，其余可并行
# 首次运行或依赖变更后，请先执行: mvn clean install -DskipTests

BACKEND_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$BACKEND_ROOT" || exit 1

mkdir -p "$BACKEND_ROOT/logs"

echo "========================================"
echo "日本跨境电商 ERP - 启动所有后端服务"
echo "========================================"

# 1. 网关（需最先启动）
"$BACKEND_ROOT/erp-gateway/start.sh" || exit 1
sleep 2

# 2. 业务服务（可并行）
"$BACKEND_ROOT/erp-system/start.sh" &
"$BACKEND_ROOT/erp-product/start.sh" &
"$BACKEND_ROOT/erp-store/start.sh" &
wait

echo ""
echo "========================================"
echo "✅ 所有后端服务已启动"
echo "  - 网关:     http://localhost:8080"
echo "  - 系统:     http://localhost:8081"
echo "  - 商品:     http://localhost:8082"
echo "  - 店铺:     http://localhost:8083"
echo "  - API 入口: http://localhost:8080/api"
echo "  - 应用日志: $BACKEND_ROOT/logs/"
echo "========================================"
