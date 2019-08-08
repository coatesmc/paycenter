package com.coates.paycenter.config;

/**
 * @ClassName MyRuntimeException
 * @Description  自定义异常类
 * @Author mc
 * @Date 2019/4/30 10:04
 * @Version 1.0
 **/
public class MyRuntimeException extends RuntimeException {

    private Integer code;
    private String message;
    private static final long serialVersionUID = -6925278824391495117L;

    public MyRuntimeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
