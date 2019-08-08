package com.coates.paycenter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @ClassName LogRecordAspect
 * @Description aop拦截
 * @Author mc
 * @Date 2019/5/7 14:45
 * @Version 1.0
 **/
@Aspect
@Component  //定义一个切面
@Order(0)
public class LogRecordAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);
    public final String string = "execution(public * com.gsg.registrationcenter.controller.*.*(..))";
    private static final String UTF_8 = "utf-8";
    private final ObjectMapper mapper;

    @Autowired
    public LogRecordAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    // 定义切点Pointcut
    @Pointcut(string)
    public void excudeService() {
    }

    //执行切点 之前
    @Before("excudeService()")
    public void exBefore(JoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }

    // 通知（环绕）
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;
        StringBuffer param = new StringBuffer();
        for (Object object : joinPoint.getArgs()) {
            if (
                    object instanceof MultipartFile
                            || object instanceof HttpServletRequest
                            || object instanceof HttpServletResponse) {
                continue;
            }
            param.append(mapper.writeValueAsString(object)).append(",");
        }
        logger.info(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + ":【parameter】: " + param.toString());
        proceed = joinPoint.proceed();
        logger.info("【RETURN】" + proceed);
        return proceed;
    }

    public static <T> Stream<T> streamOf(T[] array) {
        return ArrayUtils.isEmpty(array) ? Stream.empty() : Arrays.asList(array).stream();
    }

}
