package com.regent.rpush.route.handler;

import cn.hutool.json.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.BaseMessage;
import com.regent.rpush.dto.message.MessagePushDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 消息处理器基类
 *
 * @author 钟宝林
 * @date 2021/2/8 20:25
 **/
public abstract class MessageHandler<T extends BaseMessage> implements EventHandler<MessagePushDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(MessagePushDTO event, long sequence, boolean endOfBatch) throws Exception {
        Map<MessagePlatformEnum, JSONObject> platformParamMap = event.getPlatformParam();
        if (!platformParamMap.containsKey(platform())) {
            // 不处理
            return;
        }
        JSONObject param = platformParamMap.get(platform());
        if (param == null) {
            return;
        }
        // 处理参数
        ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type actualTypeArgument = superclass.getActualTypeArguments()[0];
        BaseMessage baseMessage = param.toBean(actualTypeArgument);
        baseMessage.setContent(event.getContent());
        baseMessage.setRequestNo(event.getRequestNo());
        try {
            handle((T) baseMessage);
        } catch (Exception e) {
            LOGGER.error("消息处理异常", e);
        }
    }

    public abstract MessagePlatformEnum platform();

    public abstract void handle(T param);

}
