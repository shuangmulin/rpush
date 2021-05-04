package com.regent.rpush.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CorsFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE; // 可以在请求被路由之前调用  pre
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1; // filter执行顺序，通过数字指定 ,优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public Object run() {
        // 获取request对象
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type");
        response.setHeader("Access-Control-Expose-Headers", "X-forwared-port, X-forwarded-host");
        response.setHeader("Vary", "Origin,Access-Control-Request-Method,Access-Control-Request-Headers");

        // 跨域请求一共会进行两次请求 先发送options 是否可以请求
        // 调试可发现一共拦截两次 第一次请求为options，第二次为正常的请求 eg：get请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            ctx.setSendZuulResponse(false); //验证请求不进行路由
            ctx.setResponseStatusCode(HttpStatus.OK.value());//返回验证成功的状态码
            ctx.set("isSuccess", true);
            return null;
        }

        ctx.setSendZuulResponse(true); //对请求进行路由
        ctx.setResponseStatusCode(HttpStatus.OK.value());
        ctx.set("isSuccess", true);

        return null;
    }

}
