package com.regent.rpush.route.service.impl;

import com.regent.rpush.route.model.RpushServerRegistration;
import com.regent.rpush.route.mapper.RpushServerRegistrationMapper;
import com.regent.rpush.route.service.IRpushServerRegistrationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.route.utils.Qw;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 注册设备表 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-20
 */
@Service
public class RpushServerRegistrationServiceImpl extends ServiceImpl<RpushServerRegistrationMapper, RpushServerRegistration> implements IRpushServerRegistrationService {

    @Override
    public RpushServerRegistration getByLoginName(String clientId, String loginName) {
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(loginName)) {
            return null;
        }
        return baseMapper.selectOne(Qw.newInstance(currentModelClass()).eq("client_id", clientId).eq("login_name", loginName));
    }

    @Override
    public RpushServerRegistration getByRegistrationId(String clientId, String registrationId) {
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(registrationId)) {
            return null;
        }
        return baseMapper.selectOne(Qw.newInstance(currentModelClass()).eq("client_id", clientId).eq("id", registrationId));
    }
}
