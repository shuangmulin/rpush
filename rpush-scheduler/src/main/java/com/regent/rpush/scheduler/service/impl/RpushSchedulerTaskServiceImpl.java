package com.regent.rpush.scheduler.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.scheduler.mapper.RpushSchedulerTaskMapper;
import com.regent.rpush.scheduler.model.RpushSchedulerTask;
import com.regent.rpush.scheduler.service.IRpushSchedulerTaskService;
import com.regent.rpush.scheduler.utils.Qw;
import com.regent.rpush.scheduler.auth.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 调度任务表 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-05-02
 */
@Service
public class RpushSchedulerTaskServiceImpl extends ServiceImpl<RpushSchedulerTaskMapper, RpushSchedulerTask> implements IRpushSchedulerTaskService, CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpushSchedulerTaskServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(String... arg0) {
        // 项目启动，即启动所有已启用的任务
        List<RpushSchedulerTask> jobList = baseMapper.selectList(null);
        for (RpushSchedulerTask task : jobList) {
            startTask(task);

        }
    }

    public void startTask(RpushSchedulerTask task) {
        if (task.getEnableFlag() != null && task.getEnableFlag()) {
            // 启动任务
            enableTask(task.getId());
        }
    }

    @Transactional
    @Override
    public void addJob(RpushSchedulerTask task) {
        if (StringUtils.isBlank(task.getBeanClass())) {
            task.setBeanClass("com.regent.rpush.scheduler.job.MessagePushJob");
        }
        task.setClientId(SessionUtils.getClientId());
        save(task); // 入库

        startTask(task); // 启动任务
    }

    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public void enableTask(Long taskId) {
        RpushSchedulerTask task = getById(taskId);
        if (task == null) {
            return;
        }
        Date startAt = task.getStartAt() == null ? DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND) : task.getStartAt();
        Date endAt = task.getEndAt();
        if (endAt != null) {
            Date now = new Date();
            if (now.after(endAt)) {
                // 已经结束的任务不启动
                return;
            }
            if (startAt.after(endAt)) {
                // 启动时间大于结束时间，异常数据不管
                return;
            }
        }

        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(task.getBeanClass()).newInstance()
                    .getClass());
            JobBuilder jobBuilder = JobBuilder.newJob(jobClass).withIdentity(task.getJobName(), task.getJobGroup());// 任务名称和组构成任务key
            jobBuilder.usingJobData("clientId", task.getClientId());
            String param = task.getParam();
            if (StringUtils.isNotBlank(param)) {
                JSONObject paramJson = new JSONObject(param);
                paramJson.put("clientId", task.getClientId());
                jobBuilder.usingJobData("param", paramJson.toString());
            }
            JobDetail jobDetail = jobBuilder.build();
            // 定义调度触发规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup())// 触发器key
                    .startAt(startAt)
                    .endAt(endAt)
                    .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression())).build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }

            // 更新启用状态
            task.setEnableFlag(true);
            updateById(task);
        } catch (Exception e) {
            LOGGER.error("添加任务调度失败", e);
            throw new IllegalStateException("添加调度任务失败", e);
        }
    }

    @Transactional
    @Override
    public void disableTask(Long taskId) throws SchedulerException {
        RpushSchedulerTask task = getOne(Qw.newInstance(RpushSchedulerTask.class).eq("id", taskId).eq("client_id", SessionUtils.getClientId()));
        if (task == null) {
            return;
        }
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.deleteJob(jobKey);

        // 更新停用状态
        task.setEnableFlag(false);
        updateById(task);
    }

    @Transactional
    @Override
    public void disableOrEnable(Long taskId, boolean disableOrEnable) throws SchedulerException {
        RpushSchedulerTask task = getOne(Qw.newInstance(RpushSchedulerTask.class).eq("id", taskId).eq("client_id", SessionUtils.getClientId()));
        if (disableOrEnable) {
            // 停用
            disableTask(taskId);
        } else {
            // 启用
            boolean checkExists = scheduler.checkExists(JobKey.jobKey(task.getJobName(), task.getJobGroup()));
            if (task.getEnableFlag() != null && task.getEnableFlag() && checkExists) {
                // 已经是启用状态
                return;
            }
            enableTask(taskId);
        }
    }

    @Transactional
    @Override
    public void updateTask(RpushSchedulerTask task) throws SchedulerException {
        RpushSchedulerTask oldTask = getOne(Qw.newInstance(RpushSchedulerTask.class).eq("id", task.getId()).eq("client_id", SessionUtils.getClientId()));
        if (oldTask == null) {
            return;
        }
        String oldJobName = oldTask.getJobName();
        String oldJobGroup = oldTask.getJobGroup();

        JobKey oldJobKey = JobKey.jobKey(oldJobName, oldJobGroup);
        scheduler.deleteJob(oldJobKey);

        removeById(task.getId()); // 删掉原来的
        addJob(task); // 重建
    }

    @Override
    public void delete(Long taskId) {
        remove(Qw.newInstance(RpushSchedulerTask.class).eq("id", taskId).eq("client_id", SessionUtils.getClientId()));
    }
}
