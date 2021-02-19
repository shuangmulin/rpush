package com.regent.rpush.route.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.MessagePushDTO;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.server.api.MessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings({"rawtypes", "unchecked", "MismatchedQueryAndUpdateOfCollection"})
@RestController
@RequestMapping("/message/push")
public class MessagePushController {

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private MessagePushService messagePushService;

    /**
     * 队列
     */
    private volatile Disruptor<MessagePushDTO> disruptor;

    @GetMapping("/choose")
    public Object chooseUrl() {
        ServiceInstance instance = loadBalancer.choose("RPUSH-SERVER");
//        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//        System.out.println(interceptors);
        return instance;
    }

    @PostMapping
    public ApiResult<String> push(@RequestBody String param) {
        MessagePushDTO messagePushDTO = new MessagePushDTO();
        JSONObject json = new JSONObject(param);
        messagePushDTO.setContent(json.getStr("content"));
        messagePushDTO.setRequestNo(json.getStr("requestNo"));
        JSONObject platformParam = json.getJSONObject("platformParam");
        MessagePlatformEnum[] values = MessagePlatformEnum.values();
        for (MessagePlatformEnum value : values) {
            messagePushDTO.getPlatformParam().put(value, platformParam.getJSONObject(value.name()));
        }
        // 往队列里扔
        RingBuffer<MessagePushDTO> ringBuffer = getDisruptor().getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            MessagePushDTO event = ringBuffer.get(sequence);
            BeanUtil.copyProperties(messagePushDTO, event);
        } finally {
            // 发布事件
            ringBuffer.publish(sequence);
        }
        return ApiResult.of("消息投递成功");
    }

    private Disruptor<MessagePushDTO> getDisruptor() {
        if (disruptor != null) {
            return disruptor;
        }
        synchronized (MessagePushController.class) {
            if (disruptor != null) {
                return disruptor;
            }
            // 项目启动同时启动消息处理队列
            Map<MessagePlatformEnum, MessageHandler> resultMap = new LinkedHashMap<>();
            Map<String, MessageHandler> springMap = applicationContext.getBeansOfType(MessageHandler.class);
            for (MessageHandler messageHandler : springMap.values()) {
                MessagePlatformEnum platform = messageHandler.platform();
                if (resultMap.containsKey(platform)) {
                    // 一种消息平台只接受一个消息处理器（如果接受多个会有处理器执行的顺序问题，会变复杂，暂时不处理这种情况）
                    throw new IllegalStateException("存在重复消息处理器");
                }
                resultMap.put(platform, messageHandler);
            }

            Executor executor = Executors.newCachedThreadPool();
            int bufferSize = 1024;
            disruptor = new Disruptor<>(MessagePushDTO::new, bufferSize, executor);
            disruptor.handleEventsWith(springMap.values().toArray(new MessageHandler[0]));
            disruptor.start();
            return disruptor;
        }
    }

}
