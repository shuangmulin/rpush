package com.regent.rpush.route.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author 钟宝林
 * @since 2021/5/11/011 20:20
 **/
public class SessionUtils {

    public static final String SUPPER_ADMIN = "super-admin";

    public static String getClientId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            return auth.getName();
        }
        return ((OAuth2Authentication) auth).getOAuth2Request().getClientId();
    }

    public static String getLoginName() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        return String.valueOf(auth.getPrincipal());
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
