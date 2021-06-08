package com.regent.rpush.route.utils.sdk;

import lombok.*;

/**
 * @author 钟宝林
 * @since 2021/6/8/008 13:50
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SdkDTOInfo {

    /**
     * 类全名
     */
    private String className;
    /**
     * 类简称
     */
    private String classSimpleName;
    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 描述
     */
    private String description;

}
