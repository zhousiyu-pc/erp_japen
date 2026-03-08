#!/bin/bash
SERVICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="$SERVICE_DIR/erp-store.pid"

if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")
  if ps -p $PID > /dev/null 2>&1; then
    kill $PID
    echo "已停止 erp-store (PID: $PID)"
  fi
  rm -f "$PID_FILE"
else
  pkill -f "erp-store" 2>/dev/null && echo "已停止 erp-store" || echo "erp-store 未运行"
fi
