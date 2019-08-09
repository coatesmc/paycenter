package com.coates.paycenter.service;

import com.alipay.api.AlipayApiException;

import java.util.Map;

/**
 * <p>Title: PaymentgatewayService.java </p>
 * <p>Package com.shenpinkj.service </p>
 * <p>Description: TODO(支付处理)  </p>
 * <p>Company: www.shenpinkj.cn</p>
 *
 * @author 牟超
 * @version 1.0
 * @date 2017年12月5日下午3:44:22
 */
public interface PaymentGatewayService {
    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月5日
     * 方法描述：微信统一下单接口
     */
    String weiXinunifiedOrder(Map<String, String> paramMap);

    /**
     * 创 建 人：牟 超
     * 创建时间：2018年1月4日
     * 方法描述：微信退款申请
     *
     * @param paramMap
     * @return
     */
    String WeChatRefundApplication(Map<String, String> paramMap);

    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月5日
     * 方法描述：订单查询
     */
    void weiXinOrderEnquiry(String out_trade_no);

    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月5日
     * 方法描述：退款查询
     */
    void weiXinRefundEquiry(String out_trade_no);

    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月5日
     * 方法描述：下载对账单
     */
    void weiXinDownloadStatement();

    /**
     * 创 建 人：牟 超
     * 创建时间：2017年12月12日
     * 方法描述：创建支付订单
     *
     * @return
     * @throws AlipayApiException
     */
    String AlipayExtremelyPay(Map<String, String> paramMap) throws AlipayApiException;

    /**
     * 创 建 人：牟 超
     * 创建时间：2018年1月4日
     * 方法描述：获取openId
     *
     * @param code
     * @return
     */
    String getOpenId(String code);

}
