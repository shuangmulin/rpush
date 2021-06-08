package com.regent.rpush.route.handler.wechatwork.robot;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.config.WechatWorkRobotConfig;
import com.regent.rpush.dto.message.wechatwork.robot.ImageMessageDTO;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushTemplateReceiverGroupService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 企业微信-图片消息
 *
 * @author 钟宝林
 * @since 2021/4/8/008 21:36
 **/
@Component
public class ImageMessageHandler extends MessageHandler<ImageMessageDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImageMessageHandler.class);

    @Autowired
    private IRpushTemplateReceiverGroupService rpushTemplateReceiverGroupService;

    @Override
    public MessageType messageType() {
        return MessageType.WECHAT_WORK_ROBOT_IMAGE;
    }

    @Override
    public void handle(ImageMessageDTO param) {
        List<WechatWorkRobotConfig> configs = rpushPlatformConfigService.queryConfigOrDefault(param, WechatWorkRobotConfig.class, messageType().getPlatform());
        for (WechatWorkRobotConfig config : configs) {
            Set<String> receiverUsers = rpushTemplateReceiverGroupService.listReceiverIds(param.getReceiverGroupIds(), param.getClientId()); // 先拿参数里分组的接收人
            if (param.getReceiverIds() != null) {
                receiverUsers.addAll(param.getReceiverIds());
            }

            if (receiverUsers.size() <= 0) {
                LOGGER.warn("请求号：{}，消息配置：{}。没有检测到接收用户", param.getRequestNo(), config.getConfigName());
                return;
            }

            RpushMessageHisDetail hisDetail = RpushMessageHisDetail.builder()
                    .platform(messageType().getPlatform().name())
                    .messageType(messageType().name())
                    .configName(config.getConfigName())
                    .receiverId(String.join(",", receiverUsers))
                    .requestNo(param.getRequestNo())
                    .configId(config.getConfigId())
                    .build();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("base64", param.getBase64());
                jsonObject.put("md5", param.getMd5());
                List<String> mentionedList = new ArrayList<>();
                List<String> mentionedMobileList = new ArrayList<>();
                for (String receiverUser : receiverUsers) {
                    receiverUser = receiverUser.equals("all") ? "@all" : receiverUser; // 补一下at全部人的@符号
                    boolean isMobile = ReUtil.isMatch("^[1][3,4,5,6,7,8,9][0-9]{9}$", receiverUser);
                    if (isMobile) {
                        mentionedMobileList.add(receiverUser);
                    } else {
                        mentionedList.add(receiverUser);
                    }
                }
                jsonObject.put("mentioned_list", mentionedList);
                jsonObject.put("mentioned_mobile_list", mentionedMobileList);
                JSONObject messageParam = new JSONObject();
                messageParam.put("msgtype", "image");
                messageParam.put("image", jsonObject);
                String post = HttpUtil.post(config.getWebhook(), messageParam.toString());
                @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") JSONObject result = new JSONObject(post);
                if (result.getInt("errcode") != 0) {
                    throw new WxErrorException(post);
                }
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
