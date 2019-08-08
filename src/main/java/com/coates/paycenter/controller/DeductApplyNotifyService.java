/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * zhike 2016年7月7日 下午2:39:35 创建
 */
package com.coates.paycenter.controller;

import com.coates.paycenter.asy.AsynchronousThreadThingsProcessing;
import com.github.wxpay.sdk.WXPayUtil;
import com.shenpinkj.common.timer.AsynchronousThreadThingsProcessing;
import com.shenpinkj.common.utils.DataValidation;
import com.shenpinkj.service.IdataUnifiedProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author zhike@yiji.com
 * @data   2016年7月7日
 */
@Controller
@RequestMapping("/inform")
public class DeductApplyNotifyService {

	static AsynchronousThreadThingsProcessing attp = null;
	
	@RequestMapping("notifyUrl")
	@ResponseBody
	public String notifyUrl(HttpServletResponse response, HttpServletRequest request){
		Map<String, String[]> map = request.getParameterMap();
		//Map<String, String> maps = DataValidation.toMapStirng(map);
		
		System.out.println("异步通知："+map.toString());
		try{
			//支付处理
			if ("commandPayTradeCreatePay".equals(maps.get("service"))) {
				// 开启异步事物
				attp = new AsynchronousThreadThingsProcessing("2",maps.toString());
				attp.start();
				iprocessingservice.saveDataUnified(maps);
			}
			
			if(maps.get("itemTradeStatus")!=null){
				
				if("EXECUTE_SUCCESS".equals(maps.get("resultCode"))&& "qftBatchTransfer".equals(maps.get("service"))){
					// 开启异步事物
					attp = new AsynchronousThreadThingsProcessing("1",maps.toString());
					attp.start();
				}
				iprocessingservice.saveDataUnified(maps);
			}
		
		}catch (Exception e) {
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
        System.out.println("----接收到的数据如下：---");
        System.out.println(xmlString);
        System.out.println("-------------------");
        Map<String, String> map = new HashMap<String, String>();

        map = WXPayUtil.xmlToMap(xmlString);

        if (DataValidation.checkSign(xmlString) == true) {
        	// 开启异步事物
			attp = new AsynchronousThreadThingsProcessing("wxzf",map.toString());
			attp.start();
        	
        	iprocessingservice.saveDataUnified(map);
        	
        	return DataValidation.returnXML(map.get("result_code"));
        } else {
            return DataValidation.returnXML("FAIL");
        }
	}
}

    