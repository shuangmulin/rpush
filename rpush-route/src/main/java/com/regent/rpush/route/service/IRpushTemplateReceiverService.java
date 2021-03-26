package com.regent.rpush.route.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rpush.dto.message.base.BaseMessage;
import com.regent.rpush.route.model.RpushTemplateReceiver;

import java.util.Set;

/**
 * <p>
 * 消息模板-预设接收人表 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
public interface IRpushTemplateReceiverService extends IService<RpushTemplateReceiver> {

    /**
     * 新增或更新接收人
     */
    void updateReceiver(RpushTemplateReceiver receiver);

    /**
     * 解析参数里的所有接收人（即{@link BaseMessage#sendTos} + {@link BaseMessage#groupIds}的并集）
     */
    Set<String> parseReceiver(BaseMessage param);
}
