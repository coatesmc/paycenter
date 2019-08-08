package com.coates.paycenter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName WebConfigurer
 * @Description TODO
 * @Author mc
 * @Date 2019/4/28 10:08
 * @Version 1.0
 **/
@Configuration
@ComponentScan(basePackages = "com.coates.paycenter")
public class WebConfigurer extends WebMvcConfigurationSupport {
    private static final Logger logger = LoggerFactory.getLogger(WebConfigurer.class);
    @Autowired
    private DateConverterConfig dateConverterConfig;
    @Autowired
    private MyHandlerInterceptor myHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //拦截所有url
        registry.addInterceptor(myHandlerInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);

    }

    /**
     * 配置跨域访问
     *
     * @return
     */
    @Bean
    public Filter getCORSFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "*"); //允许哪些url可以跨域请求到本域
                response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS"); //允许的请求方法，一般是GET,POST,PUT,DELETE,OPTIONS
                response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,sign,timestamp"); //允许哪些请求头可以跨域
                filterChain.doFilter(request, response);
            }
        };
    }


    /**
     * 添加时间转换器
     * @param registry
     */
    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverter(dateConverterConfig);
    }

}
