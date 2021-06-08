package com.regent.rpush.route.handler.dingtalk.corp;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.config.DingTalkCorpConfig;
import com.regent.rpush.dto.message.dingtalk.corp.OaMessageDTO;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 钉钉工作通知文本类型消息处理器
 *
 * @author 钟宝林
 * @since 2021/4/11/011 15:06
 **/
@Component
public class CorpOaMessageHandler extends MessageHandler<OaMessageDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(CorpOaMessageHandler.class);

    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @Override
    public MessageType messageType() {
        return MessageType.DING_TALK_COPR_OA;
    }

    @Override
    public void handle(OaMessageDTO param) {
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
            msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
            msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
            msg.getOa().setMessageUrl(param.getMessageUrl());
            msg.getOa().setPcMessageUrl(param.getPcMessageUrl());
            msg.getOa().getHead().setText(param.getHeadText());
            msg.getOa().getHead().setBgcolor(param.getHeadBgColor());
            msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
            msg.getOa().getBody().setContent(param.getContent());
            msg.getOa().getBody().setTitle(param.getBodyTitle());
            msg.getOa().getBody().setAuthor(param.getAuthor());
            msg.getOa().getBody().setFileCount(param.getFileCount());
            msg.getOa().getBody().setImage(param.getImage());
            OapiMessageCorpconversationAsyncsendV2Request.Rich rich = new OapiMessageCorpconversationAsyncsendV2Request.Rich();
            rich.setNum(param.getRichNum());
            rich.setUnit(param.getRichUnit());
            msg.getOa().getBody().setRich(rich);

            List<OaMessageDTO.BodyForm> bodyForms = param.getBodyForms();
            List<OapiMessageCorpconversationAsyncsendV2Request.Form> forms = new ArrayList<>();
            for (OaMessageDTO.BodyForm bodyForm : bodyForms) {
                OapiMessageCorpconversationAsyncsendV2Request.Form form = new OapiMessageCorpconversationAsyncsendV2Request.Form();
                form.setKey(bodyForm.getKey());
                form.setValue(bodyForm.getValue());
                forms.add(form);
            }
            msg.getOa().getBody().setForm(forms);

            msg.getOa().setStatusBar(new OapiMessageCorpconversationAsyncsendV2Request.StatusBar());
            msg.getOa().getStatusBar().setStatusBg(param.getStatusBarStatusBg());
            msg.getOa().getStatusBar().setStatusValue(param.getStatusBarStatusValue());
            msg.setMsgtype("oa");

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
