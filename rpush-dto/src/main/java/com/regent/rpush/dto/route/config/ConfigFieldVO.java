package com.regent.rpush.dto.route.config;

import com.regent.rpush.dto.enumration.ConfigValueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 配置的字段
 *
 * @author 钟宝林
 * @since 2021/3/6/006 16:07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigFieldVO implements Serializable {
    private static final long serialVersionUID = -1203229844327500120L;

    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段key
     */
    private String key;
    /**
     * 字段类型
     */
    private ConfigValueType type;

}
