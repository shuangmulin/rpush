package com.regent.rpush.route.handler.wechatofficialaccount;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.config.WechatOfficialAccountConfig;
import com.regent.rpush.dto.message.wechatofficialaccount.TextMessageDTO;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 微信公众号文本消息handler
 *
 * @author 钟宝林
 * @since 2021/3/16/016 10:45
 **/
@Component
public class MpTextMessageHandler extends MessageHandler<TextMessageDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MpTextMessageHandler.class);

    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @Override
    public MessageType messageType() {
        return MessageType.WECHAT_OFFICIAL_ACCOUNT_TEXT;
    }

    @Override
    public void handle(TextMessageDTO param) {
        List<WechatOfficialAccountConfig> configs = rpushPlatformConfigService.queryConfigOrDefault(param, WechatOfficialAccountConfig.class, messageType().getPlatform());
        for (WechatOfficialAccountConfig config : configs) {
            Set<String> receiverUsers = rpushTemplateReceiverGroupService.listReceiverIds(param.getReceiverGroupIds(), param.getClientId()); // 先拿参数里分组的接收人
            if (param.getReceiverIds() != null) {
                receiverUsers.addAll(param.getReceiverIds());
            }

            if (receiverUsers.size() <= 0) {
                LOGGER.warn("请求号：{}，消息配置：{}。没有检测到接收用户", param.getRequestNo(), config.getConfigName());
                return;
            }

            WxMpService wxService = SingletonUtil.get(config.getAppId() + config.getSecret(), () -> {
                WxMpDefaultConfigImpl mpConfig = new WxMpDefaultConfigImpl();
                mpConfig.setAppId(config.getAppId());
                mpConfig.setSecret(config.getSecret());
                WxMpService wxService1 = new WxMpServiceImpl();
                wxService1.setWxMpConfigStorage(mpConfig);
                return wxService1;
            });

            for (String receiverUser : receiverUsers) {
                WxMpKefuMessage message = WxMpKefuMessage.TEXT().toUser(receiverUser).content(param.getContent()).build();

                RpushMessageHisDetail hisDetail = RpushMessageHisDetail.builder()
                        .platform(messageType().getPlatform().name())
                        .messageType(messageType().name())
                        .configName(config.getConfigName())
                        .receiverId(receiverUser)
                        .requestNo(param.getRequestNo())
                        .configId(config.getConfigId())
                        .build();
                try {
                    wxService.getKefuService().sendKefuMessage(message);
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
