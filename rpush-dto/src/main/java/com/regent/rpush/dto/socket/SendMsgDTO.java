package com.regent.rpush.dto.socket;

import com.regent.rpush.dto.BaseParam;
import lombok.*;

import java.util.List;

/**
 * 消息发送DTO
 *
 * @author 钟宝林
 * @date 2021/2/9 10:53
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMsgDTO extends BaseParam {
    private static final long serialVersionUID = 737722535877172748L;

    /**
     * 用户id
     */
    private List<Long> userId;
    /**
     * 消息内容
     */
    private String content;

}
