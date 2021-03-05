package com.regent.rpush.route.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.dto.message.base.MessagePushDTO;
import com.regent.rpush.dto.message.config.Config;
import com.regent.rpush.dto.message.base.PlatformMessageDTO;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息处理器基类
 *
 * @author 钟宝林
 * @date 2021/2/8 20:25
 **/
public abstract class MessageHandler<T extends BaseMessage<?>> implements EventHandler<MessagePushDTO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private IRpushPlatformConfigService rpushPlatformConfigService;

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(MessagePushDTO event, long sequence, boolean endOfBatch) throws Exception {
        Map<MessagePlatformEnum, PlatformMessageDTO> platformParamMap = event.getPlatformParam();
        if (!platformParamMap.containsKey(platform())) {
            // 不处理
            return;
        }
        MessagePlatformEnum platform = platform();
        PlatformMessageDTO platformMessageDTO = platformParamMap.get(platform);
        if (platformMessageDTO == null) {
            return;
        }
        try {
            // 处理参数
            JSONObject param = platformMessageDTO.getParam();
            ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type actualTypeArgument = superclass.getActualTypeArguments()[0];
            BaseMessage<?> baseMessage = param.toBean(actualTypeArgument);
            baseMessage.setContent(event.getContent());
            baseMessage.setRequestNo(event.getRequestNo());

            // 处理配置
            processPlatformConfig(platformMessageDTO, baseMessage);

            // 最后调用实际消息处理的方法
            handle((T) baseMessage);
        } catch (Exception e) {
            LOGGER.error("消息处理异常", e);
        }
    }

    /**
     * 根据参数传入的配置id，转换成具体的配置数据，提供给下游具体的消息处理器使用
     */
    private void processPlatformConfig(PlatformMessageDTO platformMessageDTO, BaseMessage<?> baseMessage) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<Long> configIds = platformMessageDTO.getConfigIds();
        Map<Long, Map<String, String>> configMap = rpushPlatformConfigService.queryConfig(configIds); // 键为配置id，值为：具体的配置键值

        ParameterizedType messageSupperClass = (ParameterizedType) baseMessage.getClass().getGenericSuperclass();
        Class<?> configType = (Class<?>) messageSupperClass.getActualTypeArguments()[0];

        List<Config> configs = new ArrayList<>();
        for (Map.Entry<Long, Map<String, String>> entry : configMap.entrySet()) {
            Long configId = entry.getKey();
            Map<String, String> valueMap = entry.getValue();

            Config configObj = (Config) configType.newInstance();
            configObj.setConfigId(configId);
            for (String key : valueMap.keySet()) {
                if (!ReflectUtil.hasField(configType, key)) {
                    continue;
                }
                Field declaredField = configType.getDeclaredField(key);
                Class<?> fieldType = declaredField.getType();
                ReflectUtil.setFieldValue(configObj, key, MapUtil.get(valueMap, key, fieldType));
            }
            configs.add(configObj);
        }
        baseMessage.setConfigs(configs);
    }

    /**
     * 所有消息处理器必须实现这个接口，标识自己处理的是哪个平台的消息
     */
    public abstract MessagePlatformEnum platform();

    /**
     * 实现这个接口来处理消息，再正式调用这个方法之前会处理好需要的参数和需要的配置
     */
    public abstract void handle(T param);

}
