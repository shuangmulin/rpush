package com.regent.rpush.dto.message;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 普通消息结构
 *
 * @author 钟宝林
 * @since 2021/2/28/028 21:13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormalMessageDTO {
    private static final long serialVersionUID = -8824134072189443062L;

    /**
     * 发送方registrationId
     */
    @NotNull
    private Long fromTo;
    /**
     * 接收方registrationId
     */
    @NotNull
    private Long sendTo;

    private String content;

}
