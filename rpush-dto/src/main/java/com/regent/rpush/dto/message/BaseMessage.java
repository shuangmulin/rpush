package com.regent.rpush.dto.message;

import com.regent.rpush.dto.BaseParam;
import lombok.*;

/**
 * 消息基类
 *
 * @author 钟宝林
 * @date 2021/2/8 21:02
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage extends BaseParam {
    private static final long serialVersionUID = 437493036483567460L;

    private String content;

}
