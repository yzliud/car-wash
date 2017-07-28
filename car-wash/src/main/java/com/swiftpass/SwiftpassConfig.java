package com.swiftpass;

import com.jfinal.kit.PropKit;


/**
 * <一句话功能简述>
 * <功能详细描述>配置信息
 * 
 * @author  Administrator
 * @version  [版本号, 2014-8-29]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SwiftpassConfig {
    
    /**
     * 威富通交易密钥
     */
    public static String key = PropKit.use("wft_config.properties").get("key");
    
    /**
     * 威富通商户号
     */
    public static String mch_id = PropKit.use("wft_config.properties").get("mch_id");
    
    /**
     * 威富通请求url
     */
    public static String req_url = PropKit.use("wft_config.properties").get("req_url");
    
    public static String version = PropKit.use("wft_config.properties").get("version");
    
    public static String service = PropKit.use("wft_config.properties").get("service");

    public static String charset = PropKit.use("wft_config.properties").get("charset");

    public static String sign_type = PropKit.use("wft_config.properties").get("sign_type");

    public static String is_raw = PropKit.use("wft_config.properties").get("is_raw");
    
    public static String notify_url = PropKit.use("wft_config.properties").get("notify_url");
    
    public static String callback_url = PropKit.use("wft_config.properties").get("callback_url");
    
    public static String sub_appid = PropKit.use("wft_config.properties").get("sub_appid");
    
    public static String body = PropKit.use("wft_config.properties").get("body");
    
    public static String mch_create_ip = PropKit.use("wft_config.properties").get("mch_create_ip");
    
}
