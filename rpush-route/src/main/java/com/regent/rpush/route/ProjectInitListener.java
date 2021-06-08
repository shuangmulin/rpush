package com.regent.rpush.route;

import com.regent.rpush.route.service.IOauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动初始化
 *
 * @author 钟宝林
 * @since 2021/5/16/016 15:29
 **/
@Component
public class ProjectInitListener implements CommandLineRunner {

    @Autowired
    private IOauthClientDetailsService oauthClientDetailsService;

    @Value("${auth.super-admin.password}")
    private String superAdminPass;

    @Value(("${auth.super-admin.username}"))
    private String superAdminUsername;

    @Override
    public void run(String... args) throws Exception {
        // 初始化超级管理员
        oauthClientDetailsService.initSuperAdmin(superAdminUsername, superAdminPass);
    }
}
