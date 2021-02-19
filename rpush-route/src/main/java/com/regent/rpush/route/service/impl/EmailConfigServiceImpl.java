package com.regent.rpush.route.service.impl;

import com.regent.rpush.route.model.EmailConfig;
import com.regent.rpush.route.mapper.EmailConfigMapper;
import com.regent.rpush.route.service.IEmailConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮件模块配置 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-02-19
 */
@Service
public class EmailConfigServiceImpl extends ServiceImpl<EmailConfigMapper, EmailConfig> implements IEmailConfigService {

}
