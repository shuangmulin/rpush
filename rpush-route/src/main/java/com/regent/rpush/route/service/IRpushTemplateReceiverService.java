package com.regent.rpush.route.service;

import com.regent.rpush.route.model.RpushTemplateReceiver;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
