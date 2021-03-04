package com.regent.rpush.dto.message.config;

import lombok.*;

/**
 * @author 钟宝林
 * @since 2021/3/4/004 20:25
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailConfig extends Config {
    private static final long serialVersionUID = 3833630267273040696L;

    private String host;
    private int port;
    private String from;
    private String user;
    private String password;

}
