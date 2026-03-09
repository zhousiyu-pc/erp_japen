package com.erp.jp.job.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.jp.job.entity.JobExecutionLog;
import com.erp.jp.job.mapper.JobExecutionLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务日志服务
 * [by Agent]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobLogService {

    private final JobExecutionLogMapper jobExecutionLogMapper;

    /**
     * 创建任务执行日志
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createLog(String jobName, String jobType) {
        JobExecutionLog log = new JobExecutionLog();
        log.setJobName(jobName);
        log.setJobType(jobType);
        log.setStatus(0); // 执行中
        log.setStartTime(LocalDateTime.now());
        log.setCreateTime(LocalDateTime.now());
        jobExecutionLogMapper.insert(log);
        return log.getId();
    }

    /**
     * 更新任务执行日志（成功）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLogSuccess(Long logId, Integer fetchCount, Integer successCount, Integer failCount) {
        JobExecutionLog log = jobExecutionLogMapper.selectById(logId);
        if (log != null) {
            log.setStatus(1); // 成功
            log.setEndTime(LocalDateTime.now());
            log.setFetchCount(fetchCount);
            log.setSuccessCount(successCount);
            log.setFailCount(failCount);
            jobExecutionLogMapper.updateById(log);
        }
    }

    /**
     * 更新任务执行日志（失败）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLogFailed(Long logId, String errorMessage) {
        JobExecutionLog log = jobExecutionLogMapper.selectById(logId);
        if (log != null) {
            log.setStatus(2); // 失败
            log.setEndTime(LocalDateTime.now());
            log.setErrorMessage(errorMessage);
            jobExecutionLogMapper.updateById(log);
        }
    }

    /**
     * 查询最近的执行日志
     */
    public List<JobExecutionLog> listRecentLogs(String jobType, int limit) {
        return jobExecutionLogMapper.selectList(
                new LambdaQueryWrapper<JobExecutionLog>()
                        .eq(jobType != null, JobExecutionLog::getJobType, jobType)
                        .orderByDesc(JobExecutionLog::getCreateTime)
                        .last("LIMIT " + limit)
        );
    }

    /**
     * 查询失败的任务日志
     */
    public List<JobExecutionLog> listFailedLogs(Integer hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return jobExecutionLogMapper.selectList(
                new LambdaQueryWrapper<JobExecutionLog>()
                        .eq(JobExecutionLog::getStatus, 2)
                        .ge(JobExecutionLog::getCreateTime, startTime)
                        .orderByDesc(JobExecutionLog::getCreateTime)
        );
    }
}
