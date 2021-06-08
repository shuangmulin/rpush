package com.regent.rpush.route.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.EmailMessageDTO;
import com.regent.rpush.dto.message.config.EmailConfig;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushMessageHisService;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 邮件消息处理器
 *
 * @author 钟宝林
 * @date 2021/2/8 20:55
 **/
@Component
public class EmailMessageHandler extends MessageHandler<EmailMessageDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailMessageHandler.class);

    @Autowired
    private IRpushMessageHisService rpushMessageHisService;
    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @Override
    public MessageType messageType() {
        return MessageType.EMAIL;
    }

    @Override
    public void handle(EmailMessageDTO param) {
        List<EmailConfig> configs = rpushPlatformConfigService.queryConfigOrDefault(param, EmailConfig.class, messageType().getPlatform());
        String content = param.getContent();
        String title = param.getTitle();
        for (EmailConfig config : configs) {
            Set<String> receiverEmails = new HashSet<>();
            if (CollUtil.isNotEmpty(param.getReceiverIds())) {
                receiverEmails.addAll(param.getReceiverIds());
            }
            if (CollUtil.isNotEmpty(param.getReceiverGroupIds())) {
                receiverEmails.addAll(rpushTemplateReceiverGroupService.listReceiverIds(param.getReceiverGroupIds(), param.getClientId()));
            }
            if (receiverEmails.size() <= 0) {
                LOGGER.warn("请求号：{}，消息配置：{}。没有检测到接收邮箱", param.getRequestNo(), config.getConfigName());
                return;
            }
            MailAccount account = new MailAccount();
            account.setHost(config.getHost());
            account.setPort(config.getPort());
            account.setAuth(true);
            account.setFrom(config.getFrom());
            account.setUser(config.getUser());
            account.setPass(config.getPassword());
            for (String receiverEmail : receiverEmails) {
                RpushMessageHisDetail hisDetail = RpushMessageHisDetail.builder()
                        .platform(messageType().getPlatform().name())
                        .messageType(messageType().name())
                        .configName(config.getConfigName())
                        .receiverId(receiverEmail)
                        .requestNo(param.getRequestNo())
                        .configId(config.getConfigId())
                        .build();
                try {
                    MailUtil.send(account, receiverEmail, title, content, false);
                    hisDetail.setSendStatus(RpushMessageHisDetail.SEND_STATUS_SUCCESS);
                } catch (Exception e) {
                    String eMessage = ExceptionUtil.getMessage(e);
                    eMessage = StringUtils.isBlank(eMessage) ? "未知错误" : eMessage;
                    hisDetail.setSendStatus(RpushMessageHisDetail.SEND_STATUS_FAIL);
                    hisDetail.setErrorMsg(eMessage);
                }
                rpushMessageHisService.logDetail(param.getClientId(), hisDetail);
            }
        }
    }
}
