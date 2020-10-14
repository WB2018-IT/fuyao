package com.fuyao.filter;

import com.fuyao.auth.utils.JwtUtils;
import com.fuyao.common.utils.CookieUtils;
import com.fuyao.config.FilterProperties;
import com.fuyao.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURL().toString();
        List<String> allowPaths = filterProperties.getAllowPaths();
        for (String path : allowPaths){
            if(StringUtils.contains(url,path)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取token
        String tocken = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        if(StringUtils.isBlank(tocken)){
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }

        //校验
        try {
            // 校验通过什么都不做，即放行
            JwtUtils.getInfoFromToken(tocken, jwtProperties.getPublicKey());
        } catch (Exception e) {
            // 校验出现异常，返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            e.printStackTrace();
        }

        return null;
    }
}
