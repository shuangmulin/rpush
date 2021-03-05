package com.regent.rpush.dto.message.config;

import java.lang.annotation.*;

/**
 * 标记字段为配置值
 *
 * @author 钟宝林
 * @since 2021/3/5/005 9:54
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigValue {

    ConfigValueType type() default ConfigValueType.DEFAULT;

}
