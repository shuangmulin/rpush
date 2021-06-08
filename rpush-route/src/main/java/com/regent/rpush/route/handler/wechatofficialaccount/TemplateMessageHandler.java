package com.regent.rpush.route.handler.wechatofficialaccount;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.config.WechatOfficialAccountConfig;
import com.regent.rpush.dto.message.wechatofficialaccount.TemplateMessageDTO;
import com.regent.rpush.dto.message.wechatofficialaccount.WechatTemplateData;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
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
public class TemplateMessageHandler extends MessageHandler<TemplateMessageDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(TemplateMessageHandler.class);

    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @Override
    public MessageType messageType() {
        return MessageType.WECHAT_OFFICIAL_ACCOUNT_TEMPLATE;
    }

    @Override
    public void handle(TemplateMessageDTO param) {
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
                // 拼模板
                WxMpTemplateMessage wxMessageTemplate = new WxMpTemplateMessage();
                wxMessageTemplate.setTemplateId(param.getWechatTemplateId());
                wxMessageTemplate.setToUser(receiverUser);

                wxMessageTemplate.setUrl(param.getUrl());

                // 小程序
                String miniAppId = param.getMiniAppId();
                if (StringUtils.isNotBlank(miniAppId)) {
                    WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram(miniAppId, param.getMiniPagePath(), false);
                    wxMessageTemplate.setMiniProgram(miniProgram);
                }

                // 模板变量
                List<WechatTemplateData> templateDataList = param.getTemplateDataList();
                for (WechatTemplateData wechatTemplateData : templateDataList) {
                    String color = wechatTemplateData.getColor();
                    color = StringUtils.isNoneBlank(color) && !color.contains("#") ? "#" + color : color; // 校正颜色
                    wxMessageTemplate.addData(new WxMpTemplateData(wechatTemplateData.getName(), wechatTemplateData.getValue(), color));
                }

                RpushMessageHisDetail hisDetail = RpushMessageHisDetail.builder()
                        .platform(messageType().getPlatform().name())
                        .messageType(messageType().name())
                        .configName(config.getConfigName())
                        .receiverId(receiverUser)
                        .requestNo(param.getRequestNo())
                        .configId(config.getConfigId())
                        .build();
                try {
                    wxService.getTemplateMsgService().sendTemplateMsg(wxMessageTemplate);
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
