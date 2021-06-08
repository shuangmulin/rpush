package com.regent.rpush.route.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.common.IdStrAndName;
import com.regent.rpush.dto.enumration.ConfigValueType;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.enumration.SchemeValueType;
import com.regent.rpush.dto.message.config.Config;
import com.regent.rpush.dto.route.config.ConfigFieldVO;
import com.regent.rpush.dto.route.config.ConfigValue;
import com.regent.rpush.dto.route.sheme.*;
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
     * 配置类型映射
     */
    public static Map<Class<?>, ConfigValueType> JAVA_TYPE_CONFIG_MAP = new HashMap<>();

    static {
        JAVA_TYPE_CONFIG_MAP.put(int.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(long.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(short.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(char.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(Integer.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(Long.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(Short.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(Character.class, ConfigValueType.INTEGER);
        JAVA_TYPE_CONFIG_MAP.put(double.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_CONFIG_MAP.put(Double.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_CONFIG_MAP.put(float.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_CONFIG_MAP.put(Float.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_CONFIG_MAP.put(BigDecimal.class, ConfigValueType.DECIMAL);
        JAVA_TYPE_CONFIG_MAP.put(boolean.class, ConfigValueType.BOOLEAN);
        JAVA_TYPE_CONFIG_MAP.put(Boolean.class, ConfigValueType.BOOLEAN);
        JAVA_TYPE_CONFIG_MAP.put(String.class, ConfigValueType.STRING);
        JAVA_TYPE_CONFIG_MAP.put(RpushTemplate.class, ConfigValueType.RPUSH_TEMPLATE);
    }

    /**
     * 方案类型映射
     */
    public static Map<Class<?>, SchemeValueType> JAVA_TYPE_SCHEME_MAP = new HashMap<>();

    static {
        JAVA_TYPE_SCHEME_MAP.put(int.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(long.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(short.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(char.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(Integer.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(Long.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(Short.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(Character.class, SchemeValueType.INTEGER);
        JAVA_TYPE_SCHEME_MAP.put(double.class, SchemeValueType.DECIMAL);
        JAVA_TYPE_SCHEME_MAP.put(Double.class, SchemeValueType.DECIMAL);
        JAVA_TYPE_SCHEME_MAP.put(float.class, SchemeValueType.DECIMAL);
        JAVA_TYPE_SCHEME_MAP.put(Float.class, SchemeValueType.DECIMAL);
        JAVA_TYPE_SCHEME_MAP.put(BigDecimal.class, SchemeValueType.DECIMAL);
        JAVA_TYPE_SCHEME_MAP.put(boolean.class, SchemeValueType.BOOLEAN);
        JAVA_TYPE_SCHEME_MAP.put(Boolean.class, SchemeValueType.BOOLEAN);
        JAVA_TYPE_SCHEME_MAP.put(String.class, SchemeValueType.STRING);
        JAVA_TYPE_SCHEME_MAP.put(List.class, SchemeValueType.MULTI_OBJ_INPUT);
        JAVA_TYPE_SCHEME_MAP.put(Set.class, SchemeValueType.MULTI_OBJ_INPUT);
        JAVA_TYPE_SCHEME_MAP.put(Collection.class, SchemeValueType.MULTI_OBJ_INPUT);
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
    public static Class<? extends Config> getConfigType(MessageHandler<?> messageHandler) {
        return messageHandler.messageType().getPlatform().getConfigType();
    }

    /**
     * 获取消息处理的的配置的所有字段名称
     */
    public static List<ConfigFieldVO> listConfigFieldName(MessagePlatformEnum platform) {
        return SingletonUtil.get("config-field-names-" + platform.name(), () -> {
            Class<? extends Config> configType = platform.getConfigType();
            Field[] fields = ReflectUtil.getFieldsDirectly(configType, false);
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
                String description = "";
                if (annotation != null) {
                    type = annotation.type();
                    name = annotation.value();
                    description = annotation.description();
                    switch (type) {
                        case RPUSH_TEMPLATE:
                            type = JAVA_TYPE_CONFIG_MAP.get(RpushTemplate.class);
                            break;
                        case AUTO:
                            type = JAVA_TYPE_CONFIG_MAP.get(field.getType());
                            break;
                    }
                }
                if (annotation == null) {
                    Class<?> javaType = field.getType();
                    type = JAVA_TYPE_CONFIG_MAP.get(javaType);
                }
                name = StringUtils.isBlank(name) ? field.getName() : name;
                fieldNames.add(ConfigFieldVO.builder()
                        .name(name)
                        .description(description)
                        .type(type)
                        .key(field.getName())
                        .build());
            }
            return fieldNames;
        });
    }

    /**
     * 获取消息处理的的配置的所有字段名称
     */
    public static List<ConfigFieldVO> listConfigFieldName(MessageHandler<?> messageHandler) {
        return listConfigFieldName(messageHandler.messageType().getPlatform());
    }

    /**
     * 把数据库里的配置数据转成具体的配置类
     *
     * @param messageHandler 对应的消息处理器
     * @param configMap      数据库里的配置数据，这个方法可以查{@link IRpushPlatformConfigService#queryConfig(java.lang.String, java.util.List)}
     */
    public static List<Config> convertConfig(MessageHandler<?> messageHandler, Map<Long, Map<String, Object>> configMap) {
        try {
            Class<?> configType = MessageHandlerUtils.getConfigType(messageHandler); // 拿到配置的类型
            return convertConfig(configType, configMap);
        } catch (Exception e) {
            throw new IllegalStateException(messageHandler.getClass().getName() + "配置字段值转换失败，请检查", e);
        }
    }

    /**
     * 把数据库里的配置数据转成具体的配置类
     *
     * @param configType 配置类型
     * @param configMap  数据库里的配置数据，这个方法可以查{@link IRpushPlatformConfigService#queryConfig(java.lang.String, java.util.List)}
     */
    public static List<Config> convertConfig(Class<?> configType, Map<Long, Map<String, Object>> configMap) {
        try {
            List<Config> configs = new ArrayList<>();
            for (Map.Entry<Long, Map<String, Object>> entry : configMap.entrySet()) {
                Long configId = entry.getKey();
                Map<String, Object> valueMap = entry.getValue();

                Config configObj = (Config) configType.newInstance();
                configObj.setConfigId(configId);
                configObj.setDefaultFlag((Boolean) valueMap.get("defaultFlag"));
                configObj.setConfigName((String) valueMap.get("configName"));
                for (String key : valueMap.keySet()) {
                    if (!ReflectUtil.hasField(configType, key) || "defaultFlag".equals(key) || "configName".equals(key)) {
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
            throw new IllegalStateException(configType.getName() + "配置字段值转换失败，请检查", e);
        }
    }

    // ====================================================== 方案相关 ==============================================================

    /**
     * 根据消息类型获取对应方案所有字段
     *
     * @param messageType 消息类型
     * @return 字段列表
     */
    public static List<SchemeFieldVO> listSchemeField(MessageType messageType) {
        return SingletonUtil.get(messageType + SchemeFieldVO.class.getName(), () -> {
            MessageHandler<?> messageHandler = MessageHandlerHolder.get(messageType);
            ParameterizedType genericSuperclass = (ParameterizedType) messageHandler.getClass().getGenericSuperclass();
            Class<?> schemeType = (Class<?>) genericSuperclass.getActualTypeArguments()[0];

            Field[] fields = ReflectUtil.getFieldsDirectly(schemeType, false);
            if (fields == null || fields.length <= 0) {
                return Collections.emptyList();
            }

            List<SchemeFieldVO> fieldNames = new ArrayList<>();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                SchemeValue annotation = field.getAnnotation(SchemeValue.class);
                SchemeValueType type = null;
                String name = "";
                String description = "";
                List<IdStrAndName> idStrAndNames = new ArrayList<>();
                if (annotation != null) {
                    type = annotation.type();
                    name = annotation.value();
                    description = annotation.description();
                    switch (type) {
                        case AUTO:
                            type = JAVA_TYPE_SCHEME_MAP.get(field.getType());
                            break;
                    }

                    SchemeValueOption[] options = annotation.options();
                    for (SchemeValueOption option : options) {
                        idStrAndNames.add(new IdStrAndName(option.key(), option.label()));
                    }
                }
                if (annotation == null) {
                    Class<?> javaType = field.getType();
                    type = JAVA_TYPE_SCHEME_MAP.get(javaType);
                }
                List<MultiObjFieldVO> multiObjFields = new ArrayList<>();
                if (SchemeValueType.MULTI_OBJ_INPUT.equals(type)) {
                    // 多对象输入
                    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    Class<?> multiObjClass = (Class<?>) genericType.getActualTypeArguments()[0];
                    Field[] multiObjFieldArr = ReflectUtil.getFieldsDirectly(multiObjClass, false);
                    for (Field fieldMulti : multiObjFieldArr) {
                        if (Modifier.isFinal(field.getModifiers())) {
                            continue;
                        }
                        MultiObjField fieldAnnotation = fieldMulti.getAnnotation(MultiObjField.class);
                        if (fieldAnnotation != null) {
                            multiObjFields.add(MultiObjFieldVO.builder()
                                    .description(fieldAnnotation.description())
                                    .key(fieldMulti.getName())
                                    .label(fieldAnnotation.value())
                                    .build());
                        } else {
                            multiObjFields.add(MultiObjFieldVO.builder()
                                    .description(fieldMulti.getName())
                                    .key(fieldMulti.getName())
                                    .label(fieldMulti.getName())
                                    .build());
                        }
                    }
                }
                name = StringUtils.isBlank(name) ? field.getName() : name;
                fieldNames.add(SchemeFieldVO.builder()
                        .name(name)
                        .description(description)
                        .type(type)
                        .key(field.getName())
                        .options(idStrAndNames)
                        .multiObjFields(multiObjFields)
                        .build());
            }
            return fieldNames;
        });
    }

}
