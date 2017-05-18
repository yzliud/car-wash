package com.samehope.common;

public class Consts {
	
	public static final String RANDOM_CODE_KEY = "1";
	
	public static final String COOKIE_LOGINED_USER = "user";

	public static final String CHARTSET_UTF8 = "UTF-8";

	public static final String ROUTER_CONTENT = "/c";
	public static final String ROUTER_TAXONOMY = "/t";
	public static final String ROUTER_USER = "/user";
	public static final String ROUTER_USER_CENTER = ROUTER_USER + "/center";
	public static final String ROUTER_USER_LOGIN = ROUTER_USER + "/login";

	public static final int ERROR_CODE_NOT_VALIDATE_CAPTHCHE = 1;
	public static final int ERROR_CODE_USERNAME_EMPTY = 2;
	public static final int ERROR_CODE_USERNAME_EXIST = 3;
	public static final int ERROR_CODE_EMAIL_EMPTY = 4;
	public static final int ERROR_CODE_EMAIL_EXIST = 5;
	public static final int ERROR_CODE_PHONE_EMPTY = 6;
	public static final int ERROR_CODE_PHONE_EXIST = 7;
	public static final int ERROR_CODE_PASSWORD_EMPTY = 8;

	public static final String ATTR_PAGE_NUMBER = "_page_number";
	public static final String ATTR_USER = "USER";
	public static final String ATTR_GLOBAL_WEB_NAME = "WEB_NAME";
	public static final String ATTR_GLOBAL_WEB_TITLE = "WEB_TITLE";
	public static final String ATTR_GLOBAL_WEB_SUBTITLE = "WEB_SUBTITLE";
	public static final String ATTR_GLOBAL_META_KEYWORDS = "META_KEYWORDS";
	public static final String ATTR_GLOBAL_META_DESCRIPTION = "META_DESCRIPTION";

	public static final String SESSION_WECHAT_USER = "_wechat_user";
	
	public static final String SESSION_SYS_USERNAME = "sys_username";
	public static final String SESSION_T_USERNAME = "t_username";
	
	public static final String TAXONOMY_TEMPLATE_PREFIX = "for$";
	
	public static final String REDIS_PLUGIN_NAME = "main_plugin";
	public static final String REDIS_SESSION = "se";
	public static final int REDIS_SESSION_OUT_SECONDS = 1800;
	public static final String REDIS_ACTION = "ac";
	public static final int REDIS_ACTION_OUT_SECONDS = 1800;
	
	public static final int SYS_PAGE_SIZE = 10;

}
