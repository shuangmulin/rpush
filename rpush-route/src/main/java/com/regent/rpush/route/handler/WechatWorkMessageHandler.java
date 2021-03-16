package com.regent.rpush.route.handler;

import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.WechatWorkMessageDTO;
import com.regent.rpush.dto.message.config.WechatWorkConfig;
import com.regent.rpush.route.model.RpushTemplate;
import com.regent.rpush.route.service.IRpushTemplateReceiverService;
import com.regent.rpush.route.service.IRpushTemplateService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 企业微信handler
 *
 * @author 钟宝林
 * @since 2021/3/16/016 10:45
 **/
@Component
public class WechatWorkMessageHandler extends MessageHandler<WechatWorkMessageDTO> {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(WechatWorkMessageHandler.class);

    @Autowired
    private IRpushTemplateService rpushTemplateService;
    @Autowired
    private IRpushTemplateReceiverService rpushTemplateReceiverService;

    @Override
    public MessagePlatformEnum platform() {
        return MessagePlatformEnum.WECHAT_WORK;
    }

    @Override
    public void handle(WechatWorkMessageDTO param) {
        List<WechatWorkConfig> configs = param.getConfigs();
        String content = param.getContent();
        for (WechatWorkConfig config : configs) {
            Set<String> receiverUsers = rpushTemplateReceiverService.parseReceiver(param); // 先拿参数里的接收人

            // 再看下有没有模板
            Long templateId = config.getTemplateId();
            RpushTemplate rpushTemplate = rpushTemplateService.getById(templateId);
            if (rpushTemplate != null) {
                content = StringUtils.isBlank(content) ? rpushTemplate.getContent() : content; // 本次投递有传则优先取传入的，否则默认取模板的
                receiverUsers = rpushTemplateService.listAllReceiverId(rpushTemplate.getId()); // 拿模板设置的所有邮箱
            }
            if (receiverUsers.size() <= 0) {
                LOGGER.warn("请求号：{}，消息配置：{}。没有检测到接收用户", param.getRequestNo(), param.getConfigs());
                return;
            }

            WxCpDefaultConfigImpl cpConfig = SingletonUtil.get(config.getCorpId() + config.getSecret() + config.getAgentId(), () -> {
                WxCpDefaultConfigImpl cpConfig1 = new WxCpDefaultConfigImpl();
                cpConfig1.setCorpId(config.getCorpId());
                cpConfig1.setCorpSecret(config.getSecret());
                cpConfig1.setAgentId(config.getAgentId());
                return cpConfig1;
            });

            WxCpServiceImpl wxCpService = new WxCpServiceImpl();
            wxCpService.setWxCpConfigStorage(cpConfig);

            for (String receiverUser : receiverUsers) {
                WxCpMessage message = WxCpMessage.TEXT().agentId(config.getAgentId()).toUser(receiverUser).content(content).build();
                try {
                    wxCpService.getMessageService().send(message);
                } catch (WxErrorException e) {
                    LOGGER.error("发送失败[{}]，异常:[{}]", message.toJson(), e.getMessage());
                }
            }
        }
    }
}