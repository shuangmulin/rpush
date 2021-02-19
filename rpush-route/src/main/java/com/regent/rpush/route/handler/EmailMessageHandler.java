package com.regent.rpush.route.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.EmailMessageDTO;
import com.regent.rpush.route.model.EmailConfig;
import com.regent.rpush.route.service.IEmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 邮件消息处理器
 *
 * @author 钟宝林
 * @date 2021/2/8 20:55
 **/
@Component
public class EmailMessageHandler extends MessageHandler<EmailMessageDTO> {

    @Autowired
    private IEmailConfigService emailConfigService;

    @Override
    public MessagePlatformEnum platform() {
        return MessagePlatformEnum.EMAIL;
    }

    @Override
    public void handle(EmailMessageDTO param) {
        QueryWrapper<EmailConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_default", true);
        EmailConfig emailConfig = emailConfigService.getOne(queryWrapper);

        MailAccount account = new MailAccount();
        account.setHost(emailConfig.getHost());
        account.setPort(emailConfig.getPort());
        account.setAuth(true);
        account.setFrom(emailConfig.getFrom());
        account.setUser(emailConfig.getUser());
        account.setPass(emailConfig.getPassword());
        MailUtil.send(account, CollUtil.newArrayList(param.getSendTo()), "测试", "邮件来自baolin测试", false);
    }
}
