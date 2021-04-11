package com.regent.rpush.dto.enumration;

/**
 * 方案值类型
 *
 * @author 钟宝林
 * @since 2021/4/5/005 10:29
 **/
public enum SchemeValueType {

    AUTO,
    INTEGER,
    DECIMAL,
    STRING,
    BOOLEAN,
    TEXTAREA,

    // ===========以下为特殊处理的类型============
    /**
     * 接收人分组
     */
    RECEIVER_GROUP,
    /**
     * 接收人
     */
    RECEIVER,
    /**
     * 多对象输入
     */
    MULTI_OBJ_INPUT,
    /**
     * 多选
     */
    SELECT
    ;

}
