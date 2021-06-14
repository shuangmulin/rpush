package com.regent.rpush.sdk;

import com.regent.rpush.dto.enumration.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类
 *
 * @author 钟宝林
 * @since 2021/6/14/014 10:49
 **/
public class RpushMessage {

    /**
    * 消息参数和消息类型的映射关系，键为消息参数类型，值为对应的消息类型
    */
    static final Map<Class<?>, MessageType> MESSAGE_TYPE_PARAM_MAP = new HashMap<>();
    <#list sdks as sdk>

    /**
     * ${sdk.description}
     */
    public static ${sdk.className}.${sdk.classSimpleName}Builder<?, ?> ${sdk.messageType}() {
        return ${sdk.className}.builder();
    }
    static {
        MESSAGE_TYPE_PARAM_MAP.put(${sdk.className}.class, MessageType.${sdk.messageType});
    }
    </#list>

}
