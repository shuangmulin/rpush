package com.regent.rpush.dto.message.base;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 钟宝林
 * @since 2021/3/4/004 20:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformMessageDTO implements Serializable {
    private static final long serialVersionUID = -7899679819734022466L;

    /**
     * 选择的配置id列表
     */
    private List<Long> configIds;
    /**
     * 具体的参数
     */
    private JSONObject param;

}
