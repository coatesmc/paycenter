package com.coates.paycenter.configuration;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 
 * <p>Title: WeiXinConfig.java </p>
 * <p>Package com.shenpinkj.common.config </p>
 * <p>Description: TODO(微信支付关键信息)  </p>
 * <p>Company: www.shenpinkj.cn</p> 
 * @author 牟超
 * @date	2017年12月7日下午6:55:27
 * @version 1.0
 */
public class WeiXinConfig implements WXPayConfig {
	
	//统一下单接口
	public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//订单查询
	public static final String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	//关闭订单
	public static final String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	//撤销订单
	public static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
	//申请退款
	public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	//查询退款
	public static final String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	//下载对账单
	public static final String DOWNLOADBILLY_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	//交易保障
	public static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
	//转换短链接
	public static final String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	//授权码查询openId接口
	public static final String AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";
	//刷卡支付
	public static final String MICROPAY_URL =  "https://api.mch.weixin.qq.com/pay/micropay";
	//企业付款
	public static final String TRANSFERS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	//查询企业付款
	public static final String GETTRANSFERINFO_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";
	
	

	private byte[] certData;

	  public  String certLocalPath = "/WEB-INF/cert/apiclient_cert.p12";
   


	@Override
	public String getAppID() {

		return "wx62be7645a173f725";
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return "687d2b4b8fb47367d02e8068d872a196";
	}

	@Override
	public String getMchID() {
		
		return "1483739632";
	}
	
	@Override
	public InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}
	
	public String getSecret(){
		return "537730dab097190beeae50c920e34915";
	}


	@Override
	public int getHttpConnectTimeoutMs() {
		// TODO Auto-generated method stub
		return 8000;
	}
	
	@Override
	public int getHttpReadTimeoutMs() {
		// TODO Auto-generated method stub
		return 10000;
	}

}
