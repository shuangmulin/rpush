package com.regent.rpush.dto.route.sheme;

import com.regent.rpush.dto.common.IdStrAndName;
import com.regent.rpush.dto.enumration.SchemeValueType;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 方案字段
 *
 * @author 钟宝林
 * @since 2021/4/5/005 10:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeFieldVO implements Serializable {
    private static final long serialVersionUID = -4546007323930142286L;

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
    private SchemeValueType type;
    /**
     * 选项（如果是选择型字段）
     */
    private List<IdStrAndName> options = new ArrayList<>();
    /**
     * 多对象字段
     */
    private List<MultiObjFieldVO> multiObjFields = new ArrayList<>();

}
