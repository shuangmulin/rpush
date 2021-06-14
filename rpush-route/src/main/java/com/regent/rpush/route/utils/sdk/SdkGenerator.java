package com.regent.rpush.route.utils.sdk;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.regent.rpush.dto.enumration.MessageType;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.utils.MessageHandlerHolder;
import com.regent.rpush.route.utils.MessageHandlerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * sdk生成器
 *
 * @author 钟宝林
 * @since 2021/6/8/008 13:30
 **/
@Component
public class SdkGenerator {

    @Value("${sdk.directory}")
    private String sdkDirectory;

    public String generate() {
        List<Map<String, String>> sdkDTOInfoList = new ArrayList<>();
        Collection<MessageHandler<?>> messageHandlers = MessageHandlerHolder.values();
        for (MessageHandler<?> messageHandler : messageHandlers) {
            MessageType messageType = messageHandler.messageType();
            Class<?> actualTypeArgument = MessageHandlerUtils.getParamType(messageHandler);

            Map<String, String> sdkMap = new HashMap<>();
            sdkMap.put("className", actualTypeArgument.getName());
            sdkMap.put("classSimpleName", actualTypeArgument.getSimpleName());
            sdkMap.put("description", messageType.getPlatform().getName() + messageType.getName());
            sdkMap.put("messageType", messageType.name());
            sdkDTOInfoList.add(sdkMap);
        }

        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("sdk/RpushMessage.ftl");
        String sdk = template.render(Dict.create().set("sdks", sdkDTOInfoList));

        String rpushSender = sdkDirectory + File.separator + "RpushMessage.java";
        FileUtil.del(rpushSender);
        File file = FileUtil.file(rpushSender);
        FileUtil.writeBytes(sdk.getBytes(StandardCharsets.UTF_8), file);
        return sdk;
    }

}
