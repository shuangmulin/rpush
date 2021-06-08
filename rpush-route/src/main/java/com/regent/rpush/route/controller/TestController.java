package com.regent.rpush.route.controller;

import cn.hutool.core.io.FileUtil;
import com.regent.rpush.route.utils.sdk.SdkGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author 钟宝林
 * @since 2021/6/8/008 14:24
 **/
@RequestMapping("/test")
@RestController
public class TestController {

    @Resource
    private SdkGenerator sdkGenerator;
    @Value("${sdk.directory}")
    private String sdkDirectory;

    @GetMapping("/sdk")
    public String sdk() {
        String generate = sdkGenerator.generate();
        String rpushSender = sdkDirectory + File.separator + "RpushSender.java";
        FileUtil.del(rpushSender);
        File file = FileUtil.file(rpushSender);
        FileUtil.writeBytes(generate.getBytes(StandardCharsets.UTF_8), file);
        return generate;
    }
}
