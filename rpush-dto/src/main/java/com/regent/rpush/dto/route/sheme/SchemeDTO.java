package com.regent.rpush.dto.route.sheme;

import com.regent.rpush.dto.enumration.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 方案
 *
 * @author 钟宝林
 * @since 2021/4/5/005 20:18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeDTO implements Serializable {
    private static final long serialVersionUID = 8147356780332132525L;

    private Long id;
    /**
     * 消息类型
     */
    @NotNull(message = "消息类型必填")
    private MessageType messageType;
    /**
     * 方案名称
     */
    @NotBlank(message = "名称必填")
    private String name;
    /**
     * 参数
     */
    @NotBlank(message = "参数必填")
    private String param;

}
