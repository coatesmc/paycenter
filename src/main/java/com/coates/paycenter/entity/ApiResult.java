package com.coates.paycenter.entity;

import com.alibaba.fastjson.JSONArray;

import com.coates.paycenter.enums.ErrorCode;
import com.coates.paycenter.util.StatusCodeConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName ApiResult
 * @Description 接口统一返回json
 * @Author mc
 * @Date 2019/4/26 11:34
 * @Version 1.0
 **/
@Data
public class ApiResult {
    private int code;
    private String message;
    private long timestamp;

    private Object data = new HashMap<>();

    public ApiResult(int code, String message) {
        this.setTimestamp(System.currentTimeMillis());
        this.code = code;
        this.message = message;
    }

    /**
     * 请求成功
     *
     * @return
     */
    public static ApiResult successInstance() {
        return new ApiResult(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMsg());
    }

    public static ApiResult instance(int code) {
        return new ApiResult(code, StatusCodeConfig.getValue(String.valueOf(code))) ;
    }


    /**
     * 权限不足
     *
     * @return
     */
    public static ApiResult InsufficientPermissions() {
        return new ApiResult(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
    }

    /**
     * 服务器异常
     *
     * @return
     */
    public static ApiResult ServerErrorInstance() {
        return new ApiResult(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMsg());
    }

    /**
     * 认证失败
     *
     * @return
     */
    public static ApiResult AuthFailureInstance() {
        return new ApiResult(ErrorCode.AUTH_ERROR.getCode(), ErrorCode.AUTH_ERROR.getMsg());
    }

    /**
     * 参数错误
     *
     * @return
     */
    public static ApiResult ParamsInstance() {
        return new ApiResult(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }

    /**
     * json解析错误
     *
     * @return
     */
    public static ApiResult JsonParseInstance() {
        return new ApiResult(ErrorCode.JSON_PARSE_ERROR.getCode(), ErrorCode.JSON_PARSE_ERROR.getMsg());
    }

    /**
     * 非法字符串
     *
     * @return
     */
    public static ApiResult IlleagalStringInstance() {
        return new ApiResult(ErrorCode.ILLEAGAL_STRING.getCode(), ErrorCode.ILLEAGAL_STRING.getMsg());
    }

    /**
     * 未知错误
     *
     * @return
     */
    public static ApiResult UnknowInstance() {
        return new ApiResult(ErrorCode.UNKNOW_ERROR.getCode(), ErrorCode.UNKNOW_ERROR.getMsg());
    }


    /**
     * 地址错误
     *
     * @return
     */
    public static ApiResult AddressInstance() {
        return new ApiResult(ErrorCode.ADDRESS_ERROR.getCode(), ErrorCode.ADDRESS_ERROR.getMsg());
    }

    /**
     * 自定义错误
     * @param code
     * @param altMessage
     * @return
     */
    public static ApiResult CustomErrorInstance(int code, String altMessage) {
        return new ApiResult(code, altMessage);
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }



    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public ApiResult setData(Object data) {
        this.data = data;
        return this;
    }


    public ApiResult createEmptyData() {
        return this.setData(new HashMap());
    }

    public ApiResult createEmptyList() {
        return this.setData(new ArrayList());
    }

    @Override
    public String toString() {
        return JSONArray.toJSONString(this);
    }
}
