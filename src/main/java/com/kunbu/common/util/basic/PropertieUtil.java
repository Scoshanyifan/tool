package com.kunbu.common.util.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @project: spring-practice
 * @author: kunbu
 * @create: 2019-08-28 13:29
 **/
@Component  //加上bean注释后，会在启动时加载，否则需要等到使用时第一次加载
public class PropertieUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertieUtil.class);

    // todo
    public static final String PROPERTIES_CONFIG = "prop.properties";

    private static Map<String, String> kvMap = new HashMap<>();

    static {
        InputStreamReader reader = null;
        try {
            InputStream in = PropertieUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_CONFIG);
            if (in != null) {
                reader = new InputStreamReader(in);

                Properties prop = new Properties();
                prop.load(reader);

                for (String key : prop.stringPropertyNames()) {
                    kvMap.put(key, prop.getProperty(key, key));
                }
                logger.info(">>> 配置文件加载完成：{}", kvMap);
            }
        } catch (Exception e) {
            logger.error(">>> PropertiesUtil load properties error.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(">>> PropertiesUtil close resource error.", e);
                }
            }
        }
    }

    public static String getValue(String key) {
        if (kvMap.containsKey(key)) {
            return kvMap.get(key);
        } else {
            return "";
        }
    }

}
