package com.coates.paycenter.enums;

/**
 * @ClassName PaymentStatus
 * @Description 支付状态
 * @Author mc
 * @Date 2019/8/8 18:06
 * @Version 1.0
 **/
public enum PaymentStatus {
    PAYMENT_CALLBACK(1, "支付结果回调");
    private int code;
    private String msg;

    PaymentStatus(int code, String msg) {
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
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.getCode() == key) {
                return paymentStatus.msg;
            }
        }
        return null;
    }
}
