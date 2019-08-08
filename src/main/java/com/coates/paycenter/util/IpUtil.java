package com.coates.paycenter.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/9/19.
 */
public class IpUtil {


    /**
     *
     * @Title: getRemortIP
     * @Description: 获取IP
     * @param request
     * @return
     */
    public static String getRemortIP(HttpServletRequest request){
        String ip = null;
        if(request.getHeader("x-forwarded-for") == null ){
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader("x-forwarded-for");
        }
        if(ip.indexOf(",")!=-1){//如果存在，号，需要分隔
            String[] split = ip.split(",");
            ip = split[0];
        }
        return  ip;

    }
}
