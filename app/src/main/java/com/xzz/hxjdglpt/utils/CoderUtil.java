package com.xzz.hxjdglpt.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CoderUtil
{
	private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		
	
	public static char[] hexEncode(byte[] bytes)
	{
		int length = bytes.length;
		char[] hex = new char[length << 1];
		int i = 0;
		for (int j = 0; i < length; i++)
		{
			hex[(j++)] = HEX_CHARS[((0xF0 & bytes[i]) >>> 4)];
			hex[(j++)] = HEX_CHARS[(0xF & bytes[i])];
		}
		return hex;
	}
  
	public static String md5Encrypt(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		return md5Encrypt(str, null);
	}
	
	public static String md5Encrypt(String str, String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest digest = MessageDigest.getInstance("MD5");
		if (!StringUtil.toString(charset).equals("")) 
		{
			digest.update(str.getBytes(charset));
		}
		else 
		{
			digest.update(str.getBytes("utf-8"));
		}
	
		byte[] bytes = digest.digest();
		return new String(hexEncode(bytes));
	}
	
}