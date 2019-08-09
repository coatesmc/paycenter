package com.coates.paycenter.entity;

/**
 * 
 * <p>Title: WXPayInfo.java </p>
 * <p>Package com.shenpinkj.entity.vo </p>
 * <p>Description: TODO(微信支付app支付实体类)  </p>
 * <p>Company: www.shenpinkj.cn</p> 
 * @author 牟超
 * @date	2018年1月4日上午11:36:10
 * @version 1.0
 */
public class WXPayInfo {
	public String nonce_str;
	public String appid;
	public String sign;
	public String timeStamp;
	public String prepay_id;
	@Override
	public String toString() {
		return "WXPayInfo [nonce_str=" + nonce_str + ", appid=" + appid + ", sign=" + sign + ", timeStamp=" + timeStamp
				+ ", prepay_id=" + prepay_id + "]";
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getPrepay_id() {
		return prepay_id;
	}
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	
	

	
}
