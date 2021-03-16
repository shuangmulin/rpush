package com.regent.rpush.route.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.route.mapper.RpushTemplateMapper;
import com.regent.rpush.route.model.RpushTemplate;
import com.regent.rpush.route.model.RpushTemplateReceiver;
import com.regent.rpush.route.service.IRpushTemplateReceiverService;
import com.regent.rpush.route.service.IRpushTemplateService;
import com.regent.rpush.route.utils.Qw;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息模板表 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-06
 */
@Service
public class RpushTemplateServiceImpl extends ServiceImpl<RpushTemplateMapper, RpushTemplate> implements IRpushTemplateService {

    @Autowired
    private IRpushTemplateReceiverService rpushTemplateReceiverService;

    @Transactional
    @Override
    public void updateTemplate(RpushTemplate rpushTemplate) {
        String templateName = rpushTemplate.getTemplateName();
        Long id = rpushTemplate.getId();
        String receiverIds = rpushTemplate.getReceiverIds();
        if (StringUtils.isNotBlank(receiverIds)) {
            // 格式校验
            MessagePlatformEnum platform = MessagePlatformEnum.valueOf(rpushTemplate.getPlatform());
            for (String receiverId : receiverIds.split(";")) {
                platform.matcherThrow(receiverId);
            }
        }
        if (StringUtils.isNotBlank(templateName)) {
            // 名称判重
            QueryWrapper<RpushTemplate> groupNameQw = Qw.newInstance(RpushTemplate.class)
                    .eq("platform", rpushTemplate.getPlatform())
                    .eq("template_name", templateName);
            if (id != null) {
                groupNameQw.ne("id", id);
            }
            RpushTemplate existTemplate = getOne(groupNameQw);
            Assert.isTrue(existTemplate == null, "模板名称重复");
        }

        // 入库
        if (id == null) {
            save(rpushTemplate);
        } else {
            updateById(rpushTemplate);
        }
    }

    @Override
    public Set<String> listAllReceiverId(Long rpushTemplateId) {
        RpushTemplate rpushTemplate = getById(rpushTemplateId);
        String receiverIds = rpushTemplate.getReceiverIds();
        Set<String> receiverEmails = new HashSet<>();
        if (StringUtils.isNotBlank(receiverIds)) {
            CollUtil.addAll(receiverEmails, receiverIds.split(";"));
        }
        Long receiverGroupId = rpushTemplate.getReceiverGroupId();
        List<RpushTemplateReceiver> receivers = rpushTemplateReceiverService.list(Qw.newInstance(RpushTemplateReceiver.class).eq("group_id", receiverGroupId));
        if (receivers != null) {
            receiverEmails.addAll(receivers.stream().map(RpushTemplateReceiver::getReceiverId).collect(Collectors.toList()));
        }
        return receiverEmails;
    }
}
