package com.le.component.geetest;

/**
 * GeetestWeb配置文件
 * 
 *
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String geetest_id = "64af3dd36b4ae9bfbc4ec6496e2847a7";
	private static final String geetest_key = "20b37e5d375c944f05bdd74713e264b4";

	public static final String getGeetest_id() {
		return geetest_id;
	}

	public static final String getGeetest_key() {
		return geetest_key;
	}

}
