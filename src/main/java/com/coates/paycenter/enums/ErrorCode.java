package com.coates.paycenter.enums;

/**
 * @ClassName ErrorCode
 * @Description 返回类型code
 * @Author mc
 * @Date 2019/4/26 11:37
 * @Version 1.0
 **/

public enum ErrorCode {
    //返回的code 账户问题222开头
    SUCCESS(200, "成功"),

    NO_PERMISSION(211,"权限不足"),

    SERVER_ERROR(10000,"服务器异常"),

    AUTH_ERROR(10001,"认证失败"),

    PARAMS_ERROR(10002,"参数错误"),

    JSON_PARSE_ERROR(10003,"json解析错误"),

    ILLEAGAL_STRING(15001,"非法字符串"),

    UNKNOW_ERROR(16000,"未知错误"),

    LOGIN_ERROR(10003,"手机号/邮箱不能为空"),

    USER_LOGIN_ERROR(10003,"手机号/邮箱不能为空"),

    ADDRESS_ERROR(404,"您请求的地址不存在"),

    ACCOUNT_EXISTED_ERROR(222,"账号已存在请勿重复添加");



    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName(int key) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode() == key) {
                return errorCode.msg;
            }
        }
        return null;
    }
}

