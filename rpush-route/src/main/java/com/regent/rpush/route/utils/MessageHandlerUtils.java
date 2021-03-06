package com.regent.rpush.route.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.enumration.ConfigValueType;
import com.regent.rpush.dto.message.config.Config;
import com.regent.rpush.dto.route.config.ConfigFieldVO;
import com.regent.rpush.dto.route.config.ConfigValue;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.model.RpushTemplate;
import com.regent.rpush.route.service.IRpushPlatformConfigService;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.*;

/**
 * 消息处理器相关工具
 *
 * @author 钟宝林
 * @since 2021/3/6/006 15:51
 **/
public final class MessageHandlerUtils {

    /**
     * 类型映射
     */
    public static Map<Class<?>, ConfigValueType> JAVA_TYPE_MAP = new HashMap<>();

    static {
        JAVA_TYPE_MAP.put(int.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(long.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(short.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(char.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(Integer.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(Long.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(Short.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(Character.class, ConfigValueType.INTEGER);
        JAVA_TYPE_MAP.put(double.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_MAP.put(Double.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_MAP.put(float.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_MAP.put(Float.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_MAP.put(BigDecimal.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_MAP.put(boolean.class, ConfigValueType.BOOLEAN);
        JAVA_TYPE_MAP.put(Boolean.class, ConfigValueType.BOOLEAN);
        JAVA_TYPE_MAP.put(String.class, ConfigValueType.STRING);
        JAVA_TYPE_MAP.put(RpushTemplate.class, ConfigValueType.RPUSH_TEMPLATE);
    }

    /**
     * 获取消息处理器的参数类型
     */
    public static Class<?> getParamType(MessageHandler<?> messageHandler) {
        // 缓存单例，避免每次都执行反射去参数类型
        return SingletonUtil.get("param-type-" + messageHandler.getClass().getName(), () -> {
            ParameterizedType superclass = (ParameterizedType) messageHandler.getClass().getGenericSuperclass();
            return (Class<?>) superclass.getActualTypeArguments()[0];
        });
    }

    /**
     * 获取消息处理器的配置类型
     */
    public static Class<?> getConfigType(MessageHandler<?> messageHandler) {
        return SingletonUtil.get("config-type-" + messageHandler.getClass().getName(), () -> {
            Class<?> paramType = getParamType(messageHandler);
            ParameterizedType messageSupperClass = (ParameterizedType) paramType.getGenericSuperclass();
            return (Class<?>) messageSupperClass.getActualTypeArguments()[0];
        });
    }

    /**
     * 获取消息处理的的配置的所有字段名称
     */
    public static List<ConfigFieldVO> listConfigFieldName(MessageHandler<?> messageHandler) {
        return SingletonUtil.get("config-field-names-" + messageHandler.getClass().getName(), () -> {
            Class<?> configType = getConfigType(messageHandler);
            Field[] fields = ReflectUtil.getFieldsDirectly(configType, false); // 只有打了ConfigValue注解的字段才有效
            if (fields == null || fields.length <= 0) {
                return Collections.emptyList();
            }


            List<ConfigFieldVO> fieldNames = new ArrayList<>();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                ConfigValueType type = null;
                String name = "";
                if (annotation != null) {
                    type = annotation.type();
                    name = annotation.value();
                }
                Class<?> javaType = field.getType();
                if (type != null) {
                    switch (type) {
                        case RPUSH_TEMPLATE:
                            javaType = RpushTemplate.class;
                    }
                }
                name = StringUtils.isBlank(name) ? field.getName() : name;
                fieldNames.add(ConfigFieldVO.builder()
                        .name(name)
                        .type(JAVA_TYPE_MAP.get(javaType))
                        .key(field.getName())
                        .build());
            }
            return fieldNames;
        });
    }

    /**
     * 把数据库里的配置数据转成具体的配置类
     *
     * @param messageHandler 对应的消息处理器
     * @param configMap      数据库里的配置数据，这个方法可以查{@link IRpushPlatformConfigService#queryConfig(java.util.List)}
     */
    public static List<Config> convertConfig(MessageHandler<?> messageHandler, Map<Long, Map<String, String>> configMap) {
        try {
            Class<?> configType = MessageHandlerUtils.getConfigType(messageHandler); // 拿到配置的类型
            List<Config> configs = new ArrayList<>();
            for (Map.Entry<Long, Map<String, String>> entry : configMap.entrySet()) {
                Long configId = entry.getKey();
                Map<String, String> valueMap = entry.getValue();

                Config configObj = (Config) configType.newInstance();
                configObj.setConfigId(configId);
                configObj.setDefaultFlag(Boolean.parseBoolean(valueMap.get("defaultFlag")));
                for (String key : valueMap.keySet()) {
                    if (!ReflectUtil.hasField(configType, key) || "defaultFlag".equals(key)) {
                        continue;
                    }
                    Field declaredField = configType.getDeclaredField(key);
                    Class<?> fieldType = declaredField.getType();
                    ReflectUtil.setFieldValue(configObj, key, MapUtil.get(valueMap, key, fieldType));
                }
                configs.add(configObj);
            }
            return configs;
        } catch (Exception e) {
            throw new IllegalStateException("配置字段值转换失败，请检查", e);
        }
    }

}
