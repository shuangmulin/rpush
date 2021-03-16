package com.regent.rpush.route.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rpush.route.model.RpushTemplate;

import java.util.Set;

/**
 * <p>
 * 消息模板表 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
public interface IRpushTemplateService extends IService<RpushTemplate> {

    /**
     * 新增或更新模板
     */
    void updateTemplate(RpushTemplate rpushTemplate);

    /**
     * 查询模板关联的所有receiverId
     *
     * @param rpushTemplateId 模板ID
     */
    Set<String> listAllReceiverId(Long rpushTemplateId);
}
