package com.regent.rpush.route.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rpush.dto.common.IdAndName;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.route.sheme.SchemeDTO;
import com.regent.rpush.route.model.RpushMessageScheme;

import java.util.List;

/**
 * <p>
 * 消息发送方案 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-04-05
 */
public interface IRpushMessageSchemeService extends IService<RpushMessageScheme> {

    RpushMessageScheme saveOrUpdate(SchemeDTO scheme);

    List<IdAndName> listScheme(MessageType messageType);

    void delete(String schemeId);
}
