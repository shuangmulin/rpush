package com.regent.rpush.dto.message;

import com.regent.rpush.dto.BaseParam;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.config.PlatformMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息推送参数
 *
 * @author 钟宝林
 * @date 2021/2/8 18:30
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePushDTO extends BaseParam {
    private static final long serialVersionUID = 2732930320545780215L;

    /**
     * 消息内容
     */
    private String content;
    /**
     * 配置id，可以不传，传了会根据对应的配置去发消息
     */
    private List<Long> configIds;
    /**
     * 消息参数，键为需要发送的平台，值为对应平台需要的参数（不同平台可能会需要不同的参数，所以这里不表达具体类型，由不同的实现决定具体结构）
     */
    private Map<MessagePlatformEnum, PlatformMessageDTO> platformParam = new LinkedHashMap<>();

}
