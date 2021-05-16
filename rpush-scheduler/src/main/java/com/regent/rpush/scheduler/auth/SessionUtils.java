package com.regent.rpush.scheduler.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 钟宝林
 * @since 2021/5/11/011 20:20
 **/
public class SessionUtils {

    public static String getClientId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

}
