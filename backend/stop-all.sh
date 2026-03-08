#!/bin/bash
# 一键停止所有后端微服务

BACKEND_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "========================================"
echo "日本跨境电商 ERP - 停止所有后端服务"
echo "========================================"

"$BACKEND_ROOT/erp-store/stop.sh"
"$BACKEND_ROOT/erp-product/stop.sh"
"$BACKEND_ROOT/erp-system/stop.sh"
"$BACKEND_ROOT/erp-gateway/stop.sh"

echo ""
echo "✅ 所有后端服务已停止"
