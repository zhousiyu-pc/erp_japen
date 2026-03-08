#!/bin/bash
SERVICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_ROOT="$(cd "$SERVICE_DIR/.." && pwd)"
PID_FILE="$SERVICE_DIR/erp-gateway.pid"
LOG_FILE="$SERVICE_DIR/erp-gateway.log"

cd "$BACKEND_ROOT" || exit 1

if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")
  if ps -p $PID > /dev/null 2>&1; then
    echo "⚠️ erp-gateway 已在运行 (PID: $PID)"
    exit 1
  fi
  rm -f "$PID_FILE"
fi

echo "启动 erp-gateway (端口 8080)..."
nohup mvn -pl erp-gateway spring-boot:run -q > "$LOG_FILE" 2>&1 &
echo $! > "$PID_FILE"
sleep 3
if ps -p $(cat "$PID_FILE") > /dev/null 2>&1; then
  echo "✅ erp-gateway 启动成功"
else
  echo "❌ erp-gateway 启动失败，请查看 $LOG_FILE"
  rm -f "$PID_FILE"
  exit 1
fi
