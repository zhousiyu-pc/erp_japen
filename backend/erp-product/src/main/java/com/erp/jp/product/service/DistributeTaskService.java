package com.erp.jp.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.product.entity.DistributeTask;
import com.erp.jp.product.mapper.DistributeTaskMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 分发任务服务
 */
@Service
public class DistributeTaskService extends ServiceImpl<DistributeTaskMapper, DistributeTask> {
    
    /**
     * 创建分发任务
     */
    public DistributeTask createTask(String taskType, Integer totalCount, String createBy) {
        DistributeTask task = new DistributeTask();
        task.setTaskId(UUID.randomUUID().toString().replace("-", ""));
        task.setTaskType(taskType);
        task.setTotalCount(totalCount);
        task.setSuccessCount(0);
        task.setFailCount(0);
        task.setStatus(0); // 待执行
        task.setCreateBy(createBy);
        task.setCreateTime(LocalDateTime.now());
        save(task);
        return task;
    }
    
    /**
     * 更新任务进度
     */
    public boolean updateProgress(String taskId, int successCount, int failCount) {
        DistributeTask task = lambdaQuery()
            .eq(DistributeTask::getTaskId, taskId)
            .one();
        
        if (task == null) {
            return false;
        }
        
        task.setSuccessCount(successCount);
        task.setFailCount(failCount);
        
        // 判断是否完成
        if (successCount + failCount >= task.getTotalCount()) {
            task.setStatus(failCount > 0 ? 3 : 2); // 3-失败 2-完成
        } else {
            task.setStatus(1); // 执行中
        }
        
        return updateById(task);
    }
    
    /**
     * 根据任务 ID 查询
     */
    public DistributeTask getByTaskId(String taskId) {
        return lambdaQuery()
            .eq(DistributeTask::getTaskId, taskId)
            .one();
    }
}
