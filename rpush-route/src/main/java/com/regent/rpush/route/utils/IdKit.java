package com.regent.rpush.route.utils;

import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang.StringUtils;

/**
 * id生成工具
 *
 * @author 钟宝林
 * @since 2021/2/20/020 15:02
 **/
public class IdKit {

    public static long getId() {
        String workerIdStr = SpringUtils.getContext().getEnvironment().getProperty("id.workerId");
        long workerId = StringUtils.isBlank(workerIdStr) ? 1 : Long.parseLong(workerIdStr);
        String datacenterIdStr = SpringUtils.getContext().getEnvironment().getProperty("id.datacenterId");
        long datacenterId = StringUtils.isBlank(datacenterIdStr) ? 1 : Long.parseLong(datacenterIdStr);
        return IdUtil.getSnowflake(workerId, datacenterId).nextId();
    }

}
