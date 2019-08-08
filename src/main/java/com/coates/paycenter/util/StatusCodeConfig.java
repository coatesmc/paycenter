package com.coates.paycenter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @ClassName StatusCodeConfig
 * @Description TODO
 * @Author mc
 * @Date 2019/7/31 10:04
 * @Version 1.0
 **/
public class StatusCodeConfig {
    static final Logger logger = LoggerFactory.getLogger(StatusCodeConfig.class);
    private static StatusCodeConfig instance;
    private static Properties properties = new Properties();

    private StatusCodeConfig() {
        this.initDefault();
        this.initExtend();
    }

    private static synchronized void initInstance() {
        if (instance == null) {
            instance = new StatusCodeConfig();
        }

    }

    private void load(InputStream input) {
        if (input != null) {
            BufferedReader br = null;

            try {
                br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                properties.load(br);
            } catch (IOException var16) {
                var16.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }
                }

                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }

        }
    }

    private void load(String filePath) {
        try {
            InputStream is = StatusCodeConfig.class.getClass().getResourceAsStream(filePath);
            if (is == null) {
                logger.error("状态码配置文件" + filePath + "加载失败，请确认文件是否存在");
                return;
            }

            this.load(is);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private void initDefault() {
        properties.put("200", "success");
        properties.put("500", "系统繁忙,请您稍后再试");
    }

    private void initExtend() {
        this.load(this.getFullBySystem("/conf/status-code.properties"));
    }

    private String getFullBySystem(String filePath) {
        if (File.separatorChar == '/') {
            filePath = "/BOOT-INF/classes" + filePath;
        }

        return filePath;
    }

    public static String getValue(String key) {
        if (key != null && !"".equals(key)) {
            if (instance == null) {
                initInstance();
            }

            String value = properties.getProperty(key);
            return value;
        } else {
            return "";
        }
    }
}
