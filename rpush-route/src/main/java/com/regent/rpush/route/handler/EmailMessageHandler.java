package com.regent.rpush.route.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.EmailMessageDTO;
import com.regent.rpush.dto.message.config.EmailConfig;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 邮件消息处理器
 *
 * @author 钟宝林
 * @date 2021/2/8 20:55
 **/
@Component
public class EmailMessageHandler extends MessageHandler<EmailMessageDTO> {

    @Override
    public MessagePlatformEnum platform() {
        return MessagePlatformEnum.EMAIL;
    }

    @Override
    public void handle(EmailMessageDTO param) {
        List<EmailConfig> configs = param.getConfigs();
        for (EmailConfig config : configs) {
            MailAccount account = new MailAccount();
            account.setHost(config.getHost());
            account.setPort(config.getPort());
            account.setAuth(true);
            account.setFrom(config.getFrom());
            account.setUser(config.getUser());
            account.setPass(config.getPassword());
            MailUtil.send(account, CollUtil.newArrayList(param.getSendTo()), "测试", "测试邮件？", false);
        }
    }
}
