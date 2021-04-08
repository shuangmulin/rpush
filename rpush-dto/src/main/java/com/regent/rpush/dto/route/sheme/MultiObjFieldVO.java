package com.regent.rpush.dto.route.sheme;

import lombok.*;

/**
 * 多对象输入
 *
 * @author 钟宝林
 * @since 2021/4/8/008 12:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultiObjFieldVO {

    private String key;
    private String label;
    private String description;

}
