/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * zhike 2016年7月7日 下午2:39:35 创建
 */
package com.coates.paycenter.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.coates.paycenter.asy.AsynchronousThreadThingsProcessing;
import com.coates.paycenter.configuration.AlipayConfig;
import com.coates.paycenter.configuration.WeiXinConfig;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhike@yiji.com
 * @data 2016年7月7日
 */
@Controller
@Slf4j
@RequestMapping("/inform")
public class DeductApplyNotifyService {

    static AsynchronousThreadThingsProcessing attp = null;

    @RequestMapping("notifyUrl")
    @ResponseBody
    public String notifyUrl(HttpServletResponse response, HttpServletRequest request) throws AlipayApiException {
        Map<String, String[]> map = request.getParameterMap();
        //偷一个懒就没有抽象出来
        Map<String, String> maps =toMapStirng(map);
        log.info("异步通知：[{}]", map.toString());
        boolean signVerified = AlipaySignature.rsaCheckV1(maps, AlipayConfig.alipay_public_key, AlipayConfig.charset,
                AlipayConfig.sign_type);

        try {

            //调用SDK验证签名
            if (signVerified) {
                // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure

            } else {
                // TODO 验签失败则记录异常日志，并在response中返回failure.
            }

            //支付处理
            if ("commandPayTradeCreatePay".equals(maps.get("service"))) {
                // 开启异步事物
                //attp = new AsynchronousThreadThingsProcessing("2", maps.toString());
                // attp.start();
                // iprocessingservice.saveDataUnified(maps);
            }

            if (maps.get("itemTradeStatus") != null) {

                if ("EXECUTE_SUCCESS".equals(maps.get("resultCode")) && "qftBatchTransfer".equals(maps.get("service"))) {
                    // 开启异步事物
//                    attp = new AsynchronousThreadThingsProcessing("1", maps.toString());
//                    attp.start();
                }
                //iprocessingservice.saveDataUnified(maps);
            }

        } catch (Exception e) {
            return "success";
        }

        return "success";
    }

    @RequestMapping(value = "payNotifyUrl", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String payNotifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader reader = null;
        reader = request.getReader();
        String line = "";
        String xmlString = null;
        StringBuffer inputString = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        xmlString = inputString.toString();
        request.getReader().close();
        log.info("----接收到的数据如下:----");
        log.info(xmlString);
        log.info("---------end----------");

        Map<String, String> map = new HashMap<String, String>();

        map = WXPayUtil.xmlToMap(xmlString);

        if (checkSign(xmlString) == true) {
         /*   // 开启异步事物
            attp = new AsynchronousThreadThingsProcessing("wxzf", map.toString());
            attp.start();

            iprocessingservice.saveDataUnified(map);*/

            return returnXML(map.get("result_code"));
        } else {
            return returnXML("FAIL");
        }
    }


    /**
     *
     * 创 建 人：牟 超
     * 创建时间：2017年12月11日
     * 方法描述：Map数组转mapString
     * @param resultsMap
     * @return
     */
    public static Map<String, String> toMapStirng(Map<String, String[]> resultsMap){
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : resultsMap.entrySet()){
            map.put(entry.getKey(), entry.getValue()[0]);
        }
        return map;
    }

    /**
     * 检查参数是否合法
     * @param xmlString
     * @return
     * @throws Exception
     */
    public static boolean checkSign(String xmlString) throws Exception {

        Map<String, String> map = null;

        try {

            map = WXPayUtil.xmlToMap(xmlString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String signFromAPIResponse = map.get("sign").toString();

        if (signFromAPIResponse == "" || signFromAPIResponse == null) {

            System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");

            return false;
        }
        System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);

        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名

        map.put("sign", "");

        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        WeiXinConfig config =new WeiXinConfig();

        String signForAPIResponse;

        signForAPIResponse = WXPayUtil.generateSignature(map, config.getKey());


        if (!signForAPIResponse.equals(signFromAPIResponse)) {

            //签名验不过，表示这个API返回的数据有可能已经被篡改了

            System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! signForAPIResponse生成的签名为" + signForAPIResponse);

            return false;

        }

        System.out.println("恭喜，API返回的数据签名验证通过!!!");

        return true;

    }

    /**
     * 设置返回参数
     * @param return_code
     * @return
     */
    public static String returnXML(String return_code) {

        return "<xml><return_code><![CDATA["

                + return_code

                + "]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}

    