package com.coates.paycenter.config;


import com.coates.paycenter.entity.ApiResult;
import com.coates.paycenter.util.StatusCodeConfig;
import com.coates.paycenter.util.ViolationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @ClassName GlobalExceptionHandler
 * @Description  全局异常拦截处理
 * @Author mc
 * @Date 2019/4/30 10:04
 * @Version 1.0
 **/
@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {
    /**
     * 异常处理
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody
    ApiResult jsonErrorHandler(Exception e, HttpServletRequest request) throws Exception {
        e.printStackTrace();
        if (e instanceof BindException) {
            BindException be = (BindException) e;
            if (be.getErrorCount() > 0) {
                String ererMsg = be.getAllErrors().get(0).getDefaultMessage();
                return ApiResult.ParamsInstance().setData(ererMsg);
            }
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> es = cve.getConstraintViolations();
            List<String> list = new ArrayList<>(es.size());
            es.forEach(cv -> {
                String msgTpl = cv.getMessageTemplate();
                String msgValue = StatusCodeConfig.getValue(msgTpl);
                if (StringUtils.isBlank(msgValue)) {
                    msgValue = ViolationUtils.getFieldName(cv.getPropertyPath()) + cv.getMessage();
                }
                list.add(msgValue);
            });
            return ApiResult.instance(100102).setData(list);
        } else if (e instanceof NoHandlerFoundException) {
            return ApiResult.AddressInstance();
        } else if (e instanceof MyRuntimeException) {
            MyRuntimeException ex = (MyRuntimeException) e;
            return ApiResult.CustomErrorInstance(ex.getCode(), ex.getMessage());
        }
        return ApiResult.UnknowInstance();
    }

    @ExceptionHandler(value = MyRuntimeException.class)
    public @ResponseBody
    ApiResult handlerMyRuntimeException(MyRuntimeException ex) {
        return ApiResult.CustomErrorInstance(ex.getCode(), ex.getMessage());
    }

}
