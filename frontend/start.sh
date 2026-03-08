#!/bin/bash

# 前端目录（脚本所在目录）
FRONTEND_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="$FRONTEND_DIR/frontend.pid"
LOG_FILE="$FRONTEND_DIR/frontend.log"

echo "========================================"
echo "准备启动 日本跨境电商ERP 前端应用..."
echo "========================================"

# 检查是否已经在运行
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p $PID > /dev/null; then
        echo "⚠️ 应用已经在运行中 (PID: $PID)"
        echo "如需重启，请先运行 ./stop.sh"
        exit 1
    else
        echo "清理过期的 PID 文件..."
        rm "$PID_FILE"
    fi
fi

# 启动前端开发服务器
cd "$FRONTEND_DIR" || exit 1

# 若使用 nvm，优先使用 Node 18+
if [ -s "$HOME/.nvm/nvm.sh" ]; then
  . "$HOME/.nvm/nvm.sh"
  nvm use 18 2>/dev/null || nvm use 20 2>/dev/null || true
fi

echo "正在后台启动 Vite 开发服务器..."
nohup npm run dev > "$LOG_FILE" 2>&1 &

PID=$!
echo $PID > "$PID_FILE"

sleep 2

if ps -p $PID > /dev/null; then
    echo "✅ 启动成功！(进程 ID: $PID)"
    echo "📝 运行日志已保存至: $LOG_FILE"
    echo "🌐 请在浏览器中访问: http://localhost:3001"
    echo "💡 如需停止应用，请运行: ./stop.sh"
else
    echo "❌ 启动失败，请检查日志文件: $LOG_FILE"
    rm "$PID_FILE"
    exit 1
fi
