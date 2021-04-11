package com.regent.rpush.dto.route.sheme;

import java.lang.annotation.*;

/**
 * @author 钟宝林
 * @since 2021/4/11/011 15:45
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SchemeValueOption {

    String key();

    String label();

}
