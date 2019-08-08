package com.coates.paycenter.config;

import com.coates.paycenter.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @ClassName MyHandlerInterceptor
 * @Description  拦截器
 * @Author mc
 * @Date 2019/4/29 16:23
 * @Version 1.0
 **/
@Component
public class MyHandlerInterceptor  implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(MyHandlerInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if ("/root/ping".equals(request.getServletPath())) {
            response.getWriter().print("200");
            return false;
        }

        StringBuffer sb = new StringBuffer("接口请求:");
        String requestUrl = request.getServletPath();
        sb.append(requestUrl);

        sb.append(";方法:" + request.getMethod() + " 开始调用。。。。。。 ");
        StringBuffer sb1 = new StringBuffer("请求参数:");
        Enumeration paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];

                if (paramValue.length() != 0 && !"token".equals(paramName)) {
                    sb1.append(paramName).append("=").append(paramValue).append(" ");
                }
            }
        }
        logger.info(sb.toString());
        logger.info(sb1.toString());

        // 打印Header参数
        StringBuilder headerLog = new StringBuilder("请求Header:");
        Enumeration<String> headerEnum = request.getHeaderNames();
        if( headerEnum != null ) {
            short tag = 0;
            while( headerEnum.hasMoreElements() ) {
                if( tag++ > 0 ) {
                    headerLog.append(",");
                }
                String headerName = headerEnum.nextElement();
                headerLog.append(headerName).append("=").append(request.getHeader(headerName));
            }
        }
        logger.info(headerLog.toString());


        long reqTime = System.currentTimeMillis();

        request.setAttribute("reqTime", reqTime);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StringBuffer sb = new StringBuffer("调用完成:");
        sb.append(request.getServletPath());
        sb.append(";方法:" + request.getMethod());

        long reqTime = System.currentTimeMillis();
        long respTime = System.currentTimeMillis();
        if (!StringUtils.isEmpty(request.getAttribute("reqTime"))) {
            reqTime = Long.parseLong(request.getAttribute("reqTime").toString());
        }
        String ip = IpUtil.getRemortIP(request);
        sb.append(";调用时间:=" + (respTime - reqTime) + "=。。。。。" + "IP：" + ip);
        logger.info(sb.toString());
    }
}
