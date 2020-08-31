package com.cym.utils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class AuthTest {

	public static void main(String[] args) {
		String key = makeKey();
		
		int code = getCode(key);
		
		testKey(key,code); 
	}

	public static void testKey(String key, int code) {
		// 用户登录时使用
		// 根据用户密钥和用户输入的密码，验证是否一致。（近3个密码都有效：前一个，当前，下一个）
		GoogleAuthenticator gAuth = new GoogleAuthenticator();
		boolean isCodeValid = gAuth.authorize(key, code);
		System.out.println(isCodeValid);
	}

	public static String makeKey() {
		// 用户注册时使用
		// 获取一个新的密钥，默认16位，该密钥与用户绑定
		GoogleAuthenticator gAuth = new GoogleAuthenticator();
		final GoogleAuthenticatorKey key = gAuth.createCredentials();
		String key1 = key.getKey();
		System.out.println(key1);
		return key1;

	}

	public static int getCode(String key) {
		// 根据密钥，获取最新密码（后台用不到，用来开发 谷歌身份验证器 客户端）
		GoogleAuthenticator gAuth = new GoogleAuthenticator();
		int code = gAuth.getTotpPassword(key);
		System.out.println(code);
		return code;
	}
}