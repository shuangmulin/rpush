package com.regent.rpush.route.service.impl;

import com.regent.rpush.route.config.SessionUtils;
import com.regent.rpush.route.model.RpushServerRegistration;
import com.regent.rpush.route.service.IRpushServerRegistrationService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RpushUserDetailService implements UserDetailsService {

    @Resource
    private IRpushServerRegistrationService rpushServerRegistrationService;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        String clientId = SessionUtils.getClientId();
        RpushServerRegistration registration = rpushServerRegistrationService.getByLoginName(clientId, loginName);
        if (registration == null) {
            registration = rpushServerRegistrationService.getByRegistrationId(clientId, loginName);
        }
        if (registration == null) {
            return null;
        }

        List<GrantedAuthority> admin = AuthorityUtils.commaSeparatedStringToAuthorityList("push_message"); // 普通用户暂时只有消息推送权限
        return new User(loginName, registration.getPassword(), admin); // 账号 密码 权限
    }
}
