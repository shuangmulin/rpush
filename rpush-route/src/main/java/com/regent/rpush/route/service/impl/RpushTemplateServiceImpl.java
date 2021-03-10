package com.regent.rpush.route.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rpush.route.mapper.RpushTemplateMapper;
import com.regent.rpush.route.model.RpushTemplate;
import com.regent.rpush.route.service.IRpushTemplateService;
import com.regent.rpush.route.utils.Qw;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public void updateTemplate(RpushTemplate rpushTemplate) {
        String templateName = rpushTemplate.getTemplateName();
        Long id = rpushTemplate.getId();
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
}
