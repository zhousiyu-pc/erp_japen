#!/bin/bash
SERVICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="$SERVICE_DIR/erp-gateway.pid"

if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")
  if ps -p $PID > /dev/null 2>&1; then
    kill $PID
    echo "已停止 erp-gateway (PID: $PID)"
  fi
  rm -f "$PID_FILE"
else
  pkill -f "erp-gateway" 2>/dev/null && echo "已停止 erp-gateway" || echo "erp-gateway 未运行"
fi
