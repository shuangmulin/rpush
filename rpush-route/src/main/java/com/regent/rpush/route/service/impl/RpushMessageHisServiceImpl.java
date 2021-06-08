package com.regent.rpush.route.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.route.mapper.RpushMessageHisMapper;
import com.regent.rpush.route.model.RpushMessageHis;
import com.regent.rpush.route.model.RpushMessageHisDetail;
import com.regent.rpush.route.service.IRpushMessageHisDetailService;
import com.regent.rpush.route.service.IRpushMessageHisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 * 消息历史记录表 服务实现类
 * </p>
 *
 * @author 钟宝林
 * @since 2021-03-16
 */
@Service
public class RpushMessageHisServiceImpl extends ServiceImpl<RpushMessageHisMapper, RpushMessageHis> implements IRpushMessageHisService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpushMessageHisServiceImpl.class);

    @Autowired
    private IRpushMessageHisDetailService rpushMessageHisDetailService;

    @Override
    public void log(String clientId, RpushMessageHis rpushMessageHis) {
        rpushMessageHis.setClientId(clientId);

        // 往队列里扔
        RingBuffer<RpushMessageHis> ringBuffer = getDisruptorHis().getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            RpushMessageHis event = ringBuffer.get(sequence);
            BeanUtil.copyProperties(rpushMessageHis, event);
        } finally {
            // 发布事件
            ringBuffer.publish(sequence);
        }
    }

    @Override
    public void logDetail(String clientId, RpushMessageHisDetail hisDetail) {
        hisDetail.setClientId(clientId);

        // 往队列里扔
        RingBuffer<RpushMessageHisDetail> ringBuffer = getDisruptorHisDetail().getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            RpushMessageHisDetail event = ringBuffer.get(sequence);
            BeanUtil.copyProperties(hisDetail, event);
        } finally {
            // 发布事件
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 主表队列
     */
    private Disruptor<RpushMessageHis> getDisruptorHis() {
        return SingletonUtil.get("disruptor_" + RpushMessageHis.class.getName(), () -> {
            // 启动消息队列
            Executor executor = Executors.newCachedThreadPool();
            int bufferSize = 1024;
            Disruptor<RpushMessageHis> disruptor = new Disruptor<>(RpushMessageHis::new, bufferSize, executor);
            disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
                try {
                   save(event);
                } catch (Exception e) {
                    LOGGER.error("日志记录异常", e);
                }
            }); // 直接入库保存
            disruptor.start();
            return disruptor;
        });
    }

    /**
     * 详情队列
     */
    private Disruptor<RpushMessageHisDetail> getDisruptorHisDetail() {
        return SingletonUtil.get("disruptor_" + RpushMessageHisDetail.class.getName(), () -> {
            // 启动消息队列
            Executor executor = Executors.newCachedThreadPool();
            int bufferSize = 1024;
            Disruptor<RpushMessageHisDetail> disruptor = new Disruptor<>(RpushMessageHisDetail::new, bufferSize, executor);
            disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
                try {
                    rpushMessageHisDetailService.save(event);
                } catch (Exception e) {
                    LOGGER.error("日志记录异常", e);
                }
            }); // 直接入库保存
            disruptor.start();
            return disruptor;
        });
    }
}
