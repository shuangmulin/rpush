package com.regent.rpush.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 钟宝林
 * @since 2021/3/5/005 17:47
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyAndName implements Serializable {
    private static final long serialVersionUID = -8247431937045453249L;

    private String key;
    private String name;

}
