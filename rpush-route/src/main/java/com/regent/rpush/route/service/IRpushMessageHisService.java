package com.regent.rpush.route.service;

import com.regent.rpush.route.model.RpushMessageHis;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rpush.route.model.RpushMessageHisDetail;

/**
 * <p>
 * 消息历史记录表 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-16
 */
public interface IRpushMessageHisService extends IService<RpushMessageHis> {

    /**
     * 记录消息历史记录
     */
    void log(String clientId, RpushMessageHis rpushMessageHis);

    /**
     * 记录消息历史记录详情
     */
    void logDetail(String clientId, RpushMessageHisDetail hisDetail);
}
