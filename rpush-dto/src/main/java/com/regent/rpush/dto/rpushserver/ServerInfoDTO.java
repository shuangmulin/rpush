package com.regent.rpush.dto.rpushserver;

import lombok.*;

import java.io.Serializable;

/**
 * 服务器信息DTO
 *
 * @author 钟宝林
 * @since 2021/2/25/025 11:34
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerInfoDTO implements Serializable {
    private static final long serialVersionUID = 9222803955924359317L;

    private String host;
    private int socketPort;
    private int webSocketPort;
    private int httpPort;

}
