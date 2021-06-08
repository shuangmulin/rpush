package com.regent.rpush.route.handler.dingtalk.corp;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.config.DingTalkCorpConfig;
import com.regent.rpush.dto.message.dingtalk.corp.ActionCardSingleMessageDTO;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 钉钉工作通知文本类型消息处理器
 *
 * @author 钟宝林
 * @since 2021/4/11/011 15:06
 **/
@Component
public class CorpActionCardSingleMessageHandler extends MessageHandler<ActionCardSingleMessageDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(CorpActionCardSingleMessageHandler.class);

    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @Override
    public MessageType messageType() {
        return MessageType.DING_TALK_COPR_ACTION_CARD_SINGLE;
    }

    @Override
    public void handle(ActionCardSingleMessageDTO param) {
        List<DingTalkCorpConfig> configs = rpushPlatformConfigService.queryConfigOrDefault(param, DingTalkCorpConfig.class, messageType().getPlatform());
        for (DingTalkCorpConfig config : configs) {
            Set<String> receiverUsers = rpushTemplateReceiverGroupService.listReceiverIds(param.getReceiverGroupIds(), param.getClientId()); // 先拿参数里分组的接收人
            if (param.getReceiverIds() != null) {
                receiverUsers.addAll(param.getReceiverIds());
            }

            if (receiverUsers.size() <= 0) {
                LOGGER.warn("请求号：{}，消息配置：{}。没有检测到接收用户", param.getRequestNo(), config.getConfigName());
                return;
            }

            DingTalkClient client = SingletonUtil.get("dinging-" + config.getAppKey() + config.getAppSecret(),
                    (SingletonUtil.Factory<DingTalkClient>) () -> new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2"));
            OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
            request.setAgentId(Long.valueOf(config.getAgentId()));
            request.setUseridList(String.join(",", receiverUsers));
            request.setDeptIdList(param.getDeptIdList());
            request.setToAllUser(param.isToAllUser());

            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
            msg.getActionCard().setTitle(param.getSingleTitle());
            msg.getActionCard().setMarkdown(param.getMarkdown());
            msg.getActionCard().setSingleTitle(param.getSingleTitle());
            msg.getActionCard().setSingleUrl(param.getSingleUrl());
            msg.setMsgtype("action_card");
            request.setMsg(msg);

            RpushMessageHisDetail hisDetail = RpushMessageHisDetail.builder()
                    .platform(messageType().getPlatform().name())
                    .messageType(messageType().name())
                    .configName(config.getConfigName())
                    .receiverId(param.isToAllUser() ? "所有人" : request.getUseridList())
                    .requestNo(param.getRequestNo())
                    .configId(config.getConfigId())
                    .build();
            try {
                OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, AccessTokenUtils.getAccessToken(config.getAppKey(), config.getAppSecret()));
                if (!rsp.isSuccess()) {
                    throw new IllegalStateException(rsp.getBody());
                }
                hisDetail.setSendStatus(RpushMessageHisDetail.SEND_STATUS_SUCCESS);
                hisDetail.setErrorMsg(rsp.getBody());
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
