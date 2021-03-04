package com.regent.rpush.dto.message;

import com.regent.rpush.dto.message.config.SocketConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Socket消息发送DTO
 *
 * @author 钟宝林
 * @since 2021/2/28/028 21:28
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketMessageDTO extends BaseMessage<SocketConfig> {
    private static final long serialVersionUID = -3289428483627765265L;

    @NotNull
    @ApiModelProperty(value = "接收方registrationId列表，支持批量发送")
    private List<Long> sendTos;

    @NotNull
    @ApiModelProperty(value = "接收方registrationId")
    private Long fromTo;

}
