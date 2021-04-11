package com.regent.rpush.dto.message.config;

import com.regent.rpush.dto.route.config.ConfigValue;
import lombok.*;

/**
 * 企业微信-群机器人配置
 *
 * @author 钟宝林
 * @since 2021/3/16/016 10:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatWorkRobotConfig extends Config {
    private static final long serialVersionUID = -9206902816158196669L;

    @ConfigValue(value = "群机器人的webhook")
    private String webhook;

}
