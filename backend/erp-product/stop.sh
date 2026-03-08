#!/bin/bash
SERVICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="$SERVICE_DIR/erp-product.pid"

if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")
  if ps -p $PID > /dev/null 2>&1; then
    kill $PID
    echo "已停止 erp-product (PID: $PID)"
  fi
  rm -f "$PID_FILE"
else
  pkill -f "erp-product" 2>/dev/null && echo "已停止 erp-product" || echo "erp-product 未运行"
fi
