package com.regent.rpush.route.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 钟宝林
 * @since 2021/5/11/011 20:20
 **/
public class SessionUtils {

    public static final String SUPPER_ADMIN = "super-admin";

    public static String getClientId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        return auth.getName();
    }

    public static boolean isSupperAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (StringUtils.equals(authority.getAuthority(), SUPPER_ADMIN)) {
                return true;
            }
        }
        return false;
    }
}
