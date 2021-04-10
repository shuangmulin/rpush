package com.regent.rpush.dto.message.config;

import com.regent.rpush.dto.route.config.ConfigValue;
import lombok.*;

/**
 * 微信公众号配置
 *
 * @author 钟宝林
 * @since 2021/3/16/016 10:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatOfficialAccountConfig extends Config {
    private static final long serialVersionUID = -9206902816158196669L;

    private String appId;
    private String secret;

}
