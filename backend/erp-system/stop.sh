#!/bin/bash
SERVICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="$SERVICE_DIR/erp-system.pid"

if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")
  if ps -p $PID > /dev/null 2>&1; then
    kill $PID
    echo "已停止 erp-system (PID: $PID)"
  fi
  rm -f "$PID_FILE"
else
  pkill -f "erp-system" 2>/dev/null && echo "已停止 erp-system" || echo "erp-system 未运行"
fi
