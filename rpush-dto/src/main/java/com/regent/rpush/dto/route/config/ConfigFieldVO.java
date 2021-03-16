package com.regent.rpush.dto.route.config;

import com.regent.rpush.dto.common.IdStrAndName;
import com.regent.rpush.dto.enumration.ConfigValueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
     * 字段描述
     */
    private String description;
    /**
     * 字段类型
     */
    private ConfigValueType type;
    /**
     * 选项（如果是选择型字段）
     */
    private List<IdStrAndName> options = new ArrayList<>();

}
