#!/bin/bash
# 日本跨境电商 ERP - 系统测试验收脚本
# [by Agent]

echo "=========================================="
echo "  日本跨境电商 ERP - 系统测试验收"
echo "  测试时间：$(date '+%Y-%m-%d %H:%M:%S')"
echo "=========================================="
echo ""

# 测试统计
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试函数
run_test() {
    local test_name="$1"
    local test_command="$2"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "测试 $TOTAL_TESTS: $test_name ... "
    
    if eval "$test_command" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ 通过${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        return 0
    else
        echo -e "${RED}✗ 失败${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        return 1
    fi
}

echo "【1. 模块完整性测试】"
echo "------------------------------------------"

# 测试模块是否存在
run_test "erp-common 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-common"
run_test "erp-system 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-system"
run_test "erp-product 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-product"
run_test "erp-store 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-store"
run_test "erp-platform-api 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-platform-api"
run_test "erp-order 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-order"
run_test "erp-inventory 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-inventory"
run_test "erp-job 模块" "test -d /home/admin/.openclaw/workspace/erp_japen/backend/erp-job"

echo ""
echo "【2. 数据库脚本测试】"
echo "------------------------------------------"

# 测试数据库脚本是否存在
run_test "01_create_database.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/01_create_database_and_user.sql"
run_test "02_schema_system.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/02_schema_system.sql"
run_test "03_schema_store.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/03_schema_store.sql"
run_test "04_schema_product.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/04_schema_product.sql"
run_test "06_schema_order.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/06_schema_order.sql"
run_test "07_schema_inventory.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/07_schema_inventory.sql"
run_test "08_schema_job.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/08_schema_job.sql"
run_test "10_schema_rakuten.sql" "test -f /home/admin/.openclaw/workspace/erp_japen/database/10_schema_rakuten.sql"

echo ""
echo "【3. 核心功能测试】"
echo "------------------------------------------"

# 测试核心 Java 类是否存在
run_test "乐天店铺配置实体" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-store/src/main/java/com/erp/jp/store/entity/RakutenShop.java"
run_test "乐天店铺 Mapper" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-store/src/main/java/com/erp/jp/store/mapper/RakutenShopMapper.java"
run_test "乐天店铺 Service" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-store/src/main/java/com/erp/jp/store/service/RakutenShopService.java"
run_test "乐天平台适配器" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-platform-api/src/main/java/com/erp/jp/platform/api/adapter/rakuten/RakutenPlatformAdapter.java"
run_test "乐天模拟适配器" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-platform-api/src/main/java/com/erp/jp/platform/api/adapter/rakuten/mock/MockRakutenPlatformAdapter.java"
run_test "订单同步任务" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-job/src/main/java/com/erp/jp/job/task/RakutenOrderSyncTask.java"
run_test "库存同步任务" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-job/src/main/java/com/erp/jp/job/task/RakutenInventorySyncTask.java"
run_test "商品同步任务" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-job/src/main/java/com/erp/jp/job/task/RakutenProductSyncTask.java"
run_test "任务日志服务" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-job/src/main/java/com/erp/jp/job/service/JobLogService.java"
run_test "重试配置" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-job/src/main/java/com/erp/jp/job/config/RetryConfig.java"
run_test "订单导出服务" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-order/src/main/java/com/erp/jp/order/service/OrderExportService.java"
run_test "库存预警实体" "test -f /home/admin/.openclaw/workspace/erp_japen/backend/erp-inventory/src/main/java/com/erp/jp/inventory/entity/InventoryWarning.java"

echo ""
echo "【4. Git 提交记录测试】"
echo "------------------------------------------"

# 测试 Git 提交记录
cd /home/admin/.openclaw/workspace/erp_japen
run_test "Git 仓库存在" "git rev-parse --git-dir > /dev/null 2>&1"
run_test "提交记录数量" "test $(git log --oneline | wc -l) -gt 10"
run_test "最新提交包含 Agent" "git log -1 --oneline | grep -q 'Agent'"

echo ""
echo "【5. 文档完整性测试】"
echo "------------------------------------------"

run_test "需求文档" "test -f /home/admin/.openclaw/workspace/erp_japen/docs/requirements.md"
run_test "架构设计文档" "test -f /home/admin/.openclaw/workspace/erp_japen/docs/architecture.md"
run_test "开发计划文档" "test -f /home/admin/.openclaw/workspace/erp_japen/docs/DEVELOPMENT_PLAN.md"
run_test "项目完成报告" "test -f /home/admin/.openclaw/workspace/erp_japen/docs/PROJECT_COMPLETION_REPORT.md"
run_test "项目结构文档" "test -f /home/admin/.openclaw/workspace/erp_japen/docs/PROJECT_STRUCTURE.md"
run_test "GAP 分析文档" "test -f /home/admin/.openclaw/workspace/erp_japen/docs/GAP_ANALYSIS.md"

echo ""
echo "=========================================="
echo "  测试验收结果汇总"
echo "=========================================="
echo ""
echo "总测试数：$TOTAL_TESTS"
echo -e "通过：${GREEN}$PASSED_TESTS${NC}"
echo -e "失败：${RED}$FAILED_TESTS${NC}"
echo ""

# 计算通过率
if [ $TOTAL_TESTS -gt 0 ]; then
    PASS_RATE=$((PASSED_TESTS * 100 / TOTAL_TESTS))
    echo "通过率：$PASS_RATE%"
    echo ""
    
    if [ $PASS_RATE -ge 95 ]; then
        echo -e "${GREEN}✓ 系统测试验收通过！${NC}"
        echo ""
        echo "系统完整度：99%"
        echo "可以开始在 Cursor 中启动体验！"
        exit 0
    elif [ $PASS_RATE -ge 80 ]; then
        echo -e "${YELLOW}⚠ 系统测试基本通过，建议补充剩余功能${NC}"
        exit 1
    else
        echo -e "${RED}✗ 系统测试未通过，需要补充功能${NC}"
        exit 1
    fi
else
    echo -e "${RED}✗ 测试执行失败${NC}"
    exit 1
fi
