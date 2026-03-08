#!/bin/bash

# 前端目录（脚本所在目录）
FRONTEND_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="$FRONTEND_DIR/frontend.pid"

echo "========================================"
echo "准备停止 日本跨境电商ERP 前端应用..."
echo "========================================"

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")

    if ps -p $PID > /dev/null; then
        echo "正在停止进程 (PID: $PID)..."
        kill $PID

        for i in {1..5}; do
            if ! ps -p $PID > /dev/null; then
                echo "✅ 进程已成功停止。"
                rm "$PID_FILE"
                exit 0
            fi
            sleep 1
        done

        if ps -p $PID > /dev/null; then
            echo "⚠️ 进程未能正常退出，正在强制结束..."
            kill -9 $PID
            echo "✅ 进程已强制结束。"
        fi
    else
        echo "⚠️ 进程 (PID: $PID) 未运行，可能已经停止。"
    fi

    rm "$PID_FILE"
else
    echo "⚠️ 找不到 PID 文件 ($PID_FILE)。应用可能未运行，或未通过 start.sh 启动。"

    echo "尝试查找并停止相关的 Vite 进程..."
    PIDS=$(pgrep -f "vite")

    if [ -n "$PIDS" ]; then
        echo "发现相关的 Vite 进程: $PIDS"
        for pid in $PIDS; do
            kill $pid
            echo "已停止进程: $pid"
        done
        echo "✅ 所有相关进程已停止。"
    else
        echo "✅ 未发现运行中的 Vite 进程。"
    fi
fi
