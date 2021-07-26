package com.regent.rpush.route.handler;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.message.base.MessagePushDTO;
import com.regent.rpush.dto.message.base.TypeMessageDTO;
import com.regent.rpush.route.service.IRpushMessageHisService;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 消息处理器基类
 *
 * @author 钟宝林
 * @date 2021/2/8 20:25
 **/
public abstract class MessageHandler<T extends BaseMessage> implements EventHandler<MessagePushDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    protected IRpushPlatformConfigService rpushPlatformConfigService;
    @Autowired
    protected IRpushMessageHisService rpushMessageHisService;

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(MessagePushDTO event, long sequence, boolean endOfBatch) throws Exception {
        Map<MessageType, TypeMessageDTO> messageParamMap = event.getMessageParam();
        if (!messageParamMap.containsKey(messageType())) {
            // 不处理
            return;
        }
        MessageType messageType = messageType();
        TypeMessageDTO typeMessageDTO = messageParamMap.get(messageType);
        if (typeMessageDTO == null) {
            return;
        }
        try {
            // 处理参数
            JSONObject param = typeMessageDTO.getParam();
            Class<?> actualTypeArgument = MessageHandlerUtils.getParamType(this);
            BaseMessage baseMessage = param == null ? (BaseMessage) ReflectUtil.newInstance(actualTypeArgument) : (BaseMessage) param.toBean(actualTypeArgument);
            baseMessage.setRequestNo(event.getRequestNo());
            baseMessage.setClientId(event.getClientId());
            baseMessage.setConfigIds(typeMessageDTO.getConfigIds());

            // 最后调用实际消息处理的方法
            handle((T) baseMessage);
        } catch (Exception e) {
            LOGGER.error("消息处理异常", e);
        }
    }

    /**
     * 所有消息处理器必须实现这个接口，标识自己处理的是哪个消息类型
     */
    public abstract MessageType messageType();

    /**
     * 实现这个接口来处理消息，再正式调用这个方法之前会处理好需要的参数和需要的配置
     */
    public abstract void handle(T param);

}
