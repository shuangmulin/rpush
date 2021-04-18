package com.regent.rpush.route.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger LOG = LoggerFactory.getLogger(MyUserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LOG.error("登陆用户输入的用户名：{}", s);

        //根据用户名查找用户信息

        //密码进行bcrypt加密
        String pwd = "123456";
        //String cryptPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
        String cryptPwd = passwordEncoder.encode(pwd);

        LOG.error("加密后的密码为: {}", cryptPwd);

        List<GrantedAuthority> admin = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");

        return new User("s", cryptPwd, admin); //账号 密码 权限
    }
}
