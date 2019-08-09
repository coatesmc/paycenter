package com.coates.paycenter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.coates.paycenter.configuration.AlipayConfig;
import com.coates.paycenter.configuration.WeiXinConfig;
import com.coates.paycenter.service.PaymentGatewayService;
import com.coates.paycenter.util.Dates;
import com.coates.paycenter.util.HttpUtil;
import com.coates.paycenter.util.MapSort;
import com.coates.paycenter.util.wxpay.WXPayReport;
import com.coates.paycenter.util.wxpay.WXPayUtil;
import com.github.wxpay.sdk.WXPay;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("paymentgatewayService")
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

	private WeiXinConfig config = new WeiXinConfig();

	@Override
	public String weiXinunifiedOrder(Map<String, String> param) {
		String res = "";
		Map<String, String> map=new HashMap<String, String>();
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
			//微信支付传的金额为分
			Long fee = (long) (Double.valueOf(param.get("totalMoney").toString()) * 100.00);
			//商品简单描述，该字段须严格按照规范传递，具体请见https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=4_2
			paramMap.put("body", param.get("subject").toString());
			//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
			paramMap.put("out_trade_no", param.get("serialNumber"));
			//订单总金额，单位为分，详见支付金额
			paramMap.put("total_fee", fee + "");

			//wap_url 地址记得改回自己的地址
			if (param.get("openid") == null||"".equals(param.get("openid"))) {
				//必须传正确的用户端IP,支持ipv4、ipv6格式，获取方式详见获取用户ip指引
				paramMap.put("spbill_create_ip", param.get("ip"));
				//H5支付的交易类型为MWEB，扫码支付为JSAPI 这里是根据手机的类别进行判断的，当用户为H5浏览是系统自动调用h5支付
				/**
				 * 微信支付还提供以下支付方式
				 * JSAPI--JSAPI支付
				 * APP--APP支付
				 * NATIVE--Native支付
				 * MWEB--H5支付
				 * MICROPAY--付款码支付
				 *
				 */
				paramMap.put("trade_type", "MWEB");
				//该字段用于上报支付的场景信息,针对H5支付有以下三种场景,请根据对应场景上报,H5支付不建议在APP端使用，针对场景1，2请接入APP支付，不然可能会出现兼容性问题
				//1，IOS移动应用{"h5_info": //h5支付固定传"h5_info" {"type": "",  //场景类型"app_name": "",  //应用名"bundle_id": ""  //bundle_id}
				//2，安卓移动应用{"h5_info": //h5支付固定传"h5_info"{"type": "",  //场景类型"app_name": "",  //应用名"package_name": ""  //包名}}
				//3，WAP网站应用{"h5_info": //h5支付固定传"h5_info"{"type": "",  //场景类型"wap_url": "",//WAP网站URL地址"wap_name": ""  //WAP 网站名}}
				paramMap.put("scene_info",
						"{\"h5_info\": {'type':'Wap','wap_url': 'http://www.coates.com/','wap_name':'支付系统'}}");
			} else {
				paramMap.put("spbill_create_ip", param.get("ip"));
				//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
				paramMap.put("openid", param.get("openid"));
				paramMap.put("trade_type", "JSAPI");
			}

			String xml = fillRequestData(paramMap);

			res = WXPayReport.reportSync(xml);

			map = WXPayUtil.xmlToMap(res);

			System.out.println(map);

			if (param.get("openid") == null||"".equals(param.get("openid"))) {
				return map.get("mweb_url");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map.toString();
	}

	@Override
	public String WeChatRefundApplication(Map<String, String> paramMap) {
		Map<String, String> map = new HashMap<String, String>();
		String result =null;
		Long trade_no = (long) (Double.valueOf(paramMap.get("totalMoney").toString()) * 100.00);
		Long refund_no = (long) (Double.valueOf(paramMap.get("refundMoney").toString()) * 100.00);
		//微信生成的订单号，在支付通知中有返回 和 out_trade_no 二选一
		//map.put("transaction_id",null);
		//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
		map.put("out_trade_no", paramMap.get("serialNumber"));
		//商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
		map.put("out_refund_no", paramMap.get("serialNumber"));
		//订单总金额，单位为分，只能为整数，详见支付金额
		map.put("total_fee", trade_no + "");
		//退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
		map.put("refund_fee", refund_no + "");
		//若商户传入，会在下发给用户的退款消息中体现退款原因
		//注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
		map.put("refund_desc", paramMap.get("desc"));

		try {
			String xml = fillRequestData(map);
			result = WXPayReport.httpsRequest(WeiXinConfig.REFUND_URL, xml, config.certLocalPath);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result ;
	}

	@Override
	public void weiXinOrderEnquiry(String out_trade_no) {
		WeiXinConfig config = new WeiXinConfig();
		WXPay wxpay = new WXPay(config);

		Map<String, String> data = new HashMap<String, String>();
		//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 详见商户订单号
		data.put("out_trade_no", out_trade_no);
		try {
			Map<String, String> resp = wxpay.orderQuery(data);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退款查询需要订单信息
	 * @param out_trade_no
	 */
	@Override
	public void weiXinRefundEquiry(String out_trade_no) {
		WXPay wxpay = new WXPay(config);
		Map<String, String> data = new HashMap<String, String>();
		data.put("out_trade_no", out_trade_no);
		try {
			Map<String, String> resp = wxpay.refundQuery(data);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void weiXinDownloadStatement() {
		Map<String, String> param = new HashMap<String, String>();
		WeiXinConfig config = new WeiXinConfig();
		WXPay wxpay = new WXPay(config);

		param.put("appid", config.getAppID());
		param.put("mch_id", config.getMchID());
		param.put("nonce_str", WXPayUtil.generateNonceStr());
		//下载对账单的日期，格式：20140603
		param.put("bill_date", Dates.getDateUtils().getBillDate(-1));
		//ALL（默认值），返回当日所有订单信息（不含充值退款订单）
		//SUCCESS，返回当日成功支付的订单（不含充值退款订单）
		//REFUND，返回当日退款订单（不含充值退款订单）
		//RECHARGE_REFUND，返回当日充值退款订单
		param.put("bill_type", "ALL");
		try {
			Map<String, String> resp = wxpay.downloadBill(param);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String AlipayExtremelyPay(Map<String, String> paramMap) throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key);
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数
		// 此次只是参数展示，未进行字符串转义，实际情况下请转义
		JSONObject pay = new JSONObject();

		pay.put("out_trade_no","");
		pay.put("product_code","");
		pay.put("total_amount","");
		pay.put("subject","");
		pay.put("body","");
		pay.put("timeout_express","");

		request.setBizContent(pay.toJSONString());

		AlipayTradePagePayResponse response = alipayClient.execute(request);
		// 调用成功，则处理业务逻辑
		if (response.isSuccess()) {
			// .....
		}

		return null;
	}

	@Override
	public String getOpenId(String code) {
		String openid = "false";
		WeiXinConfig config = new WeiXinConfig();
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", config.getAppID());
		map.put("secret", config.getSecret());
		map.put("code", code);
		map.put("grant_type", "authorization_code");
		String param = MapSort.getSingleton().createLinkStringByGet(map);
		String results = HttpUtil.sendGet(url, param);
		JSONObject json = JSONObject.parseObject(results);
		if (json.get("errcode").toString() == null || json.get("errcode").toString() == "") {
			openid = json.get("openid").toString();
		}
		System.out.println(openid);
		return openid;
	}


	/**
	 *
	 * 创 建 人：牟 超 创建时间：2018年1月5日 方法描述：微信公共数据封装 加密
	 *
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	private String fillRequestData(Map<String, String> reqData) throws Exception {
		reqData.put("appid", config.getAppID());
		reqData.put("mch_id", config.getMchID());
		reqData.put("nonce_str", WXPayUtil.generateNonceStr());
		//填写自己的回调地址 DeductApplyNotifyService
		reqData.put("notify_url", "");

		String xml = WXPayUtil.generateSignedXml(reqData, config.getKey());

		return xml;
	}

}
