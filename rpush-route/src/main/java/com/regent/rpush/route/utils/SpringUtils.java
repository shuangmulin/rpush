package com.regent.rpush.route.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 钟宝林
 * @since 2021/2/20/020 15:12
 **/
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context; //应用上下文环境

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
