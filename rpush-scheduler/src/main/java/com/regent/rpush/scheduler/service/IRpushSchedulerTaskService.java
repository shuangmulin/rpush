package com.regent.rpush.scheduler.service;

import com.regent.rpush.scheduler.model.RpushSchedulerTask;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;

/**
 * <p>
 * 调度任务表 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-05-02
 */
public interface IRpushSchedulerTaskService extends IService<RpushSchedulerTask> {

    /**
     * 添加一个任务
     *
     * @param task 任务
     */
    void addJob(RpushSchedulerTask task);

    /**
     * 启动一个任务
     *
     * @param taskId 任务id
     */
    void enableTask(Long taskId);

    /**
     * 停用一个任务
     *
     * @param taskId 任务id
     */
    void disableTask(Long taskId) throws SchedulerException;

    /**
     * 更新一个任务
     *
     * @param task 任务信息
     */
    void updateTask(RpushSchedulerTask task) throws SchedulerException;

    /**
     * 启用或停用一个任务
     *
     * @param taskId          任务id
     * @param disableOrEnable true 表示停用，false 表示启用
     */
    void disableOrEnable(Long taskId, boolean disableOrEnable) throws SchedulerException;

    void delete(Long taskId);
}
