package com.regent.rpush.route.service;

import com.regent.rpush.route.model.RpushTemplateReceiverGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 消息模板-接收人分组表 服务类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
public interface IRpushTemplateReceiverGroupService extends IService<RpushTemplateReceiverGroup> {

    /**
     * 更新或保存分组
     */
    void updateGroup(RpushTemplateReceiverGroup group);

    /**
     * 查询分组所有接收人
     *
     * @param receiverGroupIds 接收人分组id
     * @return 接收人id列表
     */
    Set<String> listReceiverIds(List<Long> receiverGroupIds, String clientId);

    void delete(Long id);
}
