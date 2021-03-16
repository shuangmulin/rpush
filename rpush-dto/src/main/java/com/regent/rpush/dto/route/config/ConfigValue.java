package com.regent.rpush.dto.route.config;

import com.regent.rpush.dto.enumration.ConfigValueType;

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

    /**
     * 字段名称
     */
    String value() default "";

    /**
     * 字段描述
     */
    String description() default "";

    /**
     * 默认取对应的java类型
     */
    ConfigValueType type() default ConfigValueType.AUTO;

}
