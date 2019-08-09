package com.coates.paycenter.util;

import com.coates.paycenter.configuration.WeiXinConfig;

import com.coates.paycenter.entity.WXPayInfo;
import com.coates.paycenter.util.wxpay.WXPayUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
*
* <p>Title: MapSort.java </p>
* <p>Package com.shenpinkj.jm </p>
* <p>Description: 请求参数加密  </p>
* <p>Company: www.shenpinkj.cn/</p> 
* @author 牟超
* @date	2017年10月21日上午9:27:43
* @version 1.0
*/
public class MapSort {
	
	private volatile static MapSort mapSort;

	private MapSort() {
	}

	public static MapSort getSingleton() {
		if (mapSort == null) {
			synchronized (MapSort.class) {
				if (mapSort == null) {
					mapSort = new MapSort();
				}
			}
		}
		return mapSort;
	}
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年11月2日
	 * 方法描述：统一处理签名
	 * @param map
	 * @return
	 */
	public String mapHandlingMethod(Map<String, String> map){
		//排序
		Map<String, String> resultmap= this.sortMapByKey(map);
		
		//转字符串
		String resultRSA = this.buildMap(resultmap);
		
		//加密处理
		String result = RSACoder.encrypting(resultRSA);
		
		return result;
	}
	
	/**
	 * 
	 * 创  建  人：牟 超
	 * 创建时间：2017年10月21日
	 * 方法描述：Map转字符串工具类
	 * @param resultmap
	 * @return
	 */
	public String MapTurnTheString(Map<String, String> resultmap) {
		String mapString = "";
		for (Map.Entry<String, String> entry : resultmap.entrySet()) {
			mapString = mapString + entry.getKey() + entry.getValue();
		}

		return mapString;
	}
	
	public static void main(String[] args) {
		String str = "shenpinkj-pay";
		//weixinApi:687d2b4b8fb47367d02e8068d872a196 
		System.out.println(MD5Util.MD5Encode(str));
	}
	

	/**
	 * 
	 * 创  建  人：牟 超
	 * 创建时间：2017年10月21日
	 * 方法描述：使用 Map按key进行排序 
	 * @param map 排序参数
	 * @return
	 */
	public Map<String, String> sortMapByKey(Map<String, String> map) {

		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = new TreeMap<String, String>(
				new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2018年1月4日
	 * 方法描述：Map 转key=value&key=value
	 * @param map
	 * @return
	 */
	public String buildMap(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                sb.append(key + "=");
                if (StringUtils.isEmpty(map.get(key))) {
                    sb.append("&");
                } else {
                    String value = map.get(key);
                    try {
                        value = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    sb.append(value + "&");
                }
            }
        }
        return sb.toString();
    }
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2018年1月5日
	 * 方法描述：Map 转key=value&key=value
	 * @param params
	 * @return
	 */
	public String createLinkStringByGet(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			try {
				value = URLEncoder.encode(value, "UTF-8");

				if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
					prestr = prestr + key + "=" + value;
				} else {
					prestr = prestr + key + "=" + value + "&";
				}
			} catch (UnsupportedEncodingException e) {
			}
		}
		return prestr;
	}
	
	
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2018年1月4日
	 * 方法描述：微信公众号数据处理
	 * @param info
	 * @return
	 */
	public WXPayInfo paySign(WXPayInfo info){
		WeiXinConfig weiXinConfig = new WeiXinConfig();
		Map<String, String> map = new HashMap<String, String>();
		info.setTimeStamp(Dates.getDateUtils().timeStamp());
		map.put("appId", info.getAppid());
		map.put("timeStamp", info.getTimeStamp());
		map.put("nonceStr", info.getNonce_str());
		map.put("signType", "MD5");
		map.put("package", "prepay_id="+info.prepay_id);
		try {
			String xml = WXPayUtil.generateSignedXml(map, weiXinConfig.getKey());
			Map<String,String> maps = WXPayUtil.xmlToMap(xml);
			info.setSign(maps.get("sign"));
			
			System.out.println(info);
			
			return info;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
	
}
