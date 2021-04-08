package com.regent.rpush.dto.route.sheme;

import java.lang.annotation.*;

/**
 * 多对象输入字段
 *
 * @author 钟宝林
 * @since 2021/4/8/008 12:17
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiObjField {

    /**
     * 字段名称
     */
    String value() default "";

    /**
     * 字段描述
     */
    String description() default "";

}
