package com.regent.rpush.route.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.route.mapper.OauthClientDetailsMapper;
import com.regent.rpush.route.model.OauthClientDetails;
import com.regent.rpush.route.service.IOauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-05-16
 */
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements IOauthClientDetailsService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public void initSuperAdmin(String superAdminUsername, String superAdminPass) {
        baseMapper.initSuperAdmin(superAdminUsername, passwordEncoder.encode(superAdminPass));
    }
}
