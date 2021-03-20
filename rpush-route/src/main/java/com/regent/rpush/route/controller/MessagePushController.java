package com.regent.rpush.route.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.regent.rpush.dto.ApiResult;
import com.regent.rpush.dto.enumration.MessagePlatformEnum;
import com.regent.rpush.dto.message.base.MessagePushDTO;
import com.regent.rpush.dto.message.base.PlatformMessageDTO;
import com.regent.rpush.route.handler.MessageHandler;
import com.regent.rpush.route.model.RpushMessageHis;
import com.regent.rpush.route.service.IRpushMessageHisService;
import com.regent.rpush.route.utils.MessageHandlerHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mybatis-plus.global-config.workerId}")
    private int workerId;
    @Value("${mybatis-plus.global-config.datacenterId}")
    private int datacenterId;

    /**
     * 队列
     */
    private volatile Disruptor<MessagePushDTO> disruptor;

    @Autowired
    private IRpushMessageHisService rpushMessageHisService;

    @PostMapping
    public ApiResult<String> push(@RequestBody String param) {
        MessagePushDTO messagePushDTO = new MessagePushDTO();
        JSONObject json = new JSONObject(param);
        String requestNo = json.getStr("requestNo");
        if (StringUtils.isBlank(requestNo)) {
            // 如果客户端投递消息时没有传唯一请求号，这里自动生成一个
            requestNo = new Snowflake(workerId, datacenterId).nextIdStr();
        }
        messagePushDTO.setRequestNo(requestNo);
        messagePushDTO.setContent(json.getStr("content"));
        messagePushDTO.setTitle(json.getStr("title"));
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

        rpushMessageHisService.log(RpushMessageHis.builder().requestNo(requestNo).param(param).build()); // 记录消息历史记录

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
        return ApiResult.of(requestNo);
    }

    private Disruptor<MessagePushDTO> getDisruptor() {
        if (disruptor != null) {
            return disruptor;
        }
        synchronized (MessagePushController.class) {
            if (disruptor != null) {
                return disruptor;
            }
            // 启动消息队列
            Executor executor = Executors.newCachedThreadPool();
            int bufferSize = 1024;
            disruptor = new Disruptor<>(MessagePushDTO::new, bufferSize, executor);
            disruptor.handleEventsWith(MessageHandlerHolder.values().toArray(new MessageHandler[0]));
            disruptor.start();
            return disruptor;
        }
    }

}
