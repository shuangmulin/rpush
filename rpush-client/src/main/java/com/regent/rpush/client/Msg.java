package com.regent.rpush.client;

import com.regent.rpush.common.Constants;
import lombok.*;

/**
 * 消息
 *
 * @author 钟宝林
 * @since 2021/7/10/010 21:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Msg {

    /**
     * 从哪来的
     */
    private long fromTo;
    /**
     * 发给谁的
     */
    private long sendTo;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型{@link Constants.MessageType}
     */
    private int type;

}
