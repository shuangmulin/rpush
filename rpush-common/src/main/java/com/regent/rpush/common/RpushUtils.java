package com.regent.rpush.common;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * rpush一些工具
 *
 * @author 钟宝林
 * @since 2021/8/15/015 15:34
 **/
public final class RpushUtils {
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

    /**
     * 判断是否是手机号
     *
     * @param tel 手机号
     * @return boolean true:是  false:否
     */
    public static boolean isMobile(String tel) {
        if (StringUtils.isEmpty(tel)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, tel);
    }

    /**
     * 获取列表里的手机号
     */
    public static List<String> getMobile(Collection<String> list) {
        List<String> result = new ArrayList<>(list.size());
        for (String str : list) {
            if (isMobile(str)) {
                result.add(str);
            }
        }
        return result;
    }

    /**
     * 获取列表里不是手机号的字符串列表
     */
    public static List<String> getNotMobile(Collection<String> list) {
        List<String> result = new ArrayList<>(list.size());
        for (String str : list) {
            if (!isMobile(str)) {
                result.add(str);
            }
        }
        return result;
    }

}
