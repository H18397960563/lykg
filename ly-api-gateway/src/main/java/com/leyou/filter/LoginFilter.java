package com.leyou.filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProps;

    @Autowired
    private FilterProperties filterProps;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {

        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    /**
     * 返回false过滤器不生效，返回true，生效
     *
     * 对于某些请求，应该放行，白名单
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //获取当前请求的上下文对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        ///api/auth/accredit
        String requestURI = request.getRequestURI();

        //获取白名单集合
        List<String> allowPaths = filterProps.getAllowPaths();

        //迭代白名单数组，如果请求的uri以白名单中某一项开头，则过滤器不生效
        for (String allowPath : allowPaths) {
            if (requestURI.startsWith(allowPath)){
                return false;
            }
        }

        return true;
    }


    /**
     * 没有酒驾，一切正常，放行，
     * 拦截要处理，拦截就是不响应
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        //获取当前请求的上下文对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        try {
            //获取token值，看能不能正常获取
            String token = CookieUtils.getCookieValue(request, jwtProps.getCookieName());

            JwtUtils.getInfoFromToken(token,jwtProps.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
            //不能解析，说明没有登录，回状态码401
            currentContext.setResponseStatusCode(401);
            //说没有登录不响应
            currentContext.setSendZuulResponse(false);
        }

        return null;
    }
}
