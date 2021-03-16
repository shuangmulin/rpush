package com.regent.rpush.route.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.base.MessagePushDTO;
import com.regent.rpush.dto.message.base.PlatformMessageDTO;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.utils.MessageHandlerHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings({"rawtypes", "unchecked", "MismatchedQueryAndUpdateOfCollection"})
@RestController
@RequestMapping("/message/push")
public class MessagePushController {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 队列
     */
    private volatile Disruptor<MessagePushDTO> disruptor;

    @PostMapping
    public ApiResult<String> push(@RequestBody String param) {
        MessagePushDTO messagePushDTO = new MessagePushDTO();
        JSONObject json = new JSONObject(param);
        messagePushDTO.setContent(json.getStr("content"));
        messagePushDTO.setTitle(json.getStr("title"));
        messagePushDTO.setRequestNo(json.getStr("requestNo"));
        JSONObject platformParam = json.getJSONObject("platformParam");
        MessagePlatformEnum[] values = MessagePlatformEnum.values();
        for (MessagePlatformEnum value : values) {
            JSONObject jsonObject = platformParam.getJSONObject(value.name());
            if (jsonObject == null) {
                continue;
            }
            JSONArray configIds = jsonObject.getJSONArray("configIds");
            JSONArray sendTos = jsonObject.getJSONArray("sendTos");
            JSONArray groupIds = jsonObject.getJSONArray("groupIds");
            PlatformMessageDTO platformMessageDTO = PlatformMessageDTO.builder()
                    .configIds(configIds == null ? Collections.EMPTY_LIST : configIds.toList(Long.TYPE))
                    .sendTos(sendTos == null ? Collections.EMPTY_LIST : sendTos.toList(String.class))
                    .groupIds(groupIds == null ? Collections.EMPTY_LIST : groupIds.toList(Long.TYPE))
                    .param(jsonObject.getJSONObject("param"))
                    .build();
            messagePushDTO.getPlatformParam().put(value, platformMessageDTO);
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
            Executor executor = Executors.newCachedThreadPool();
            int bufferSize = 1024;
            disruptor = new Disruptor<>(MessagePushDTO::new, bufferSize, executor);
            disruptor.handleEventsWith(MessageHandlerHolder.values().toArray(new MessageHandler[0]));
            disruptor.start();
            return disruptor;
        }
    }

}
