package com.regent.rpush.route.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.dto.common.IdAndName;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.dto.route.sheme.SchemeDTO;
import com.regent.rpush.route.mapper.RpushMessageSchemeMapper;
import com.regent.rpush.route.model.RpushMessageScheme;
import com.regent.rpush.route.service.IRpushMessageSchemeService;
import com.regent.rpush.route.utils.Qw;
import com.regent.rpush.route.config.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 消息发送方案 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-04-05
 */
@Service
public class RpushMessageSchemeServiceImpl extends ServiceImpl<RpushMessageSchemeMapper, RpushMessageScheme> implements IRpushMessageSchemeService {

    @Autowired
    private RpushMessageSchemeMapper rpushMessageSchemeMapper;

    @Transactional
    @Override
    public RpushMessageScheme saveOrUpdate(SchemeDTO scheme) {
        String clientId = SessionUtils.getClientId();

        RpushMessageScheme messageScheme = new RpushMessageScheme();
        messageScheme.setId(scheme.getId());
        messageScheme.setMessageType(scheme.getMessageType().name());
        messageScheme.setName(scheme.getName());
        messageScheme.setPlatform(scheme.getMessageType().getPlatform().name());
        messageScheme.setParam(scheme.getParam());
        messageScheme.setClientId(clientId);

        // 名称查重
        QueryWrapper<RpushMessageScheme> wrapper = Qw.newInstance(RpushMessageScheme.class)
                .eq("client_id", clientId)
                .eq("name", scheme.getName())
                .eq("message_type", scheme.getMessageType());
        if (scheme.getId() != null) {
            wrapper.ne("id", scheme.getId());
        }
        RpushMessageScheme exists = getOne(wrapper);
        if (exists != null) {
            throw new IllegalArgumentException("方案名称重复");
        }

        if (messageScheme.getId() != null) {
            updateById(messageScheme);
        } else {
            save(messageScheme);
        }
        return messageScheme;
    }

    @Override
    public List<IdAndName> listScheme(MessageType messageType) {
        String clientId = SessionUtils.getClientId();
        return rpushMessageSchemeMapper.listScheme(clientId, messageType);
    }

    @Override
    public void delete(String schemeId) {
        rpushMessageSchemeMapper.delete(Qw.newInstance(RpushMessageScheme.class).eq("id", schemeId).eq("client_id", SessionUtils.getClientId()));
    }
}
