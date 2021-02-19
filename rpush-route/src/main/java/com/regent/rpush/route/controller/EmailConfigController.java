package com.regent.rpush.route.controller;


import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.route.model.EmailConfig;
import com.regent.rpush.route.service.IEmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 邮件模块配置 前端控制器
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-19
 */
@RestController
@RequestMapping("/email-config")
public class EmailConfigController {

    @Autowired
    private IEmailConfigService emailConfigService;

    @GetMapping
    public ApiResult<List<EmailConfig>> list() {
        List<EmailConfig> list = emailConfigService.list();
        return ApiResult.of(list);
    }

}
