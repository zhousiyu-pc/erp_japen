#!/bin/bash
SERVICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_ROOT="$(cd "$SERVICE_DIR/.." && pwd)"
PID_FILE="$SERVICE_DIR/erp-product.pid"
LOG_FILE="$SERVICE_DIR/erp-product.log"

cd "$BACKEND_ROOT" || exit 1

if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")
  if ps -p $PID > /dev/null 2>&1; then
    echo "⚠️ erp-product 已在运行 (PID: $PID)"
    exit 1
  fi
  rm -f "$PID_FILE"
fi

echo "启动 erp-product (端口 8082)..."
nohup mvn -pl erp-product spring-boot:run -q > "$LOG_FILE" 2>&1 &
echo $! > "$PID_FILE"
sleep 5
if ps -p $(cat "$PID_FILE") > /dev/null 2>&1; then
  echo "✅ erp-product 启动成功"
else
  echo "❌ erp-product 启动失败，请查看 $LOG_FILE"
  rm -f "$PID_FILE"
  exit 1
fi
