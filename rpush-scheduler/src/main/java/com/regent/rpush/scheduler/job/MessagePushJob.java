package com.regent.rpush.scheduler.job;

import com.regent.rpush.api.route.MessageRoutePushService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息推送任务
 *
 * @author 钟宝林
 * @since 2021/5/2/002 17:50
 **/
@Component
public class MessagePushJob implements Job {

    @Autowired
    private MessageRoutePushService messageRoutePushService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String param = jobExecutionContext.getMergedJobDataMap().getString("param");
        if (StringUtils.isBlank(param)) {
            // 没有参数就不用执行了
            return;
        }
        messageRoutePushService.push(param);
    }
}
