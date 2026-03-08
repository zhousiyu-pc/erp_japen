#!/bin/bash
# 一键执行所有数据库脚本
# 用法: ./run_all.sh [mysql_root_password]

set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

MYSQL_CMD="mysql -u root"
if [ -n "$1" ]; then
  MYSQL_CMD="mysql -u root -p$1"
fi

echo "=========================================="
echo "执行日本跨境电商ERP数据库初始化..."
echo "=========================================="

for f in 01_create_database_and_user.sql 02_schema_system.sql 03_schema_store.sql 04_schema_product.sql 05_seed_demo.sql; do
  if [ -f "$f" ]; then
    echo "执行: $f"
    $MYSQL_CMD < "$f" 2>/dev/null || $MYSQL_CMD -e "source $f"
  fi
done

echo "=========================================="
echo "数据库初始化完成！"
echo "=========================================="
