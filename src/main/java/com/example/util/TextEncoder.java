package com.example.util;

import java.math.BigInteger;

public class TextEncoder {
	/**
	 * 加密
	 * @param str
	 * 待加密数据
	 * @return
	 * 返回加密后的字符串
	 */
	public static String encode(String str){
		if(str==null||"".equals(str)){
			return "";
		}
		if(str.startsWith("/1.0/")){
			return str;
		}
		String src="100";
		for(int i=0;i<str.length();i++){
			Integer val=(int) (str.substring(i,i+1).getBytes())[0];
			src+=(val<10?("00"+val):val<100?("0"+val):val);
		}
		BigInteger srcBitInt=new BigInteger(src,10);
		return "/1.0/"+srcBitInt.toString(16);
	}

	/**
	 * 解密
	 * @param str
	 * 待解密数据
	 * @return
	 * 返回解密后的字符串
	 */
	public static String decode(String str){
		if(str==null||"".equals(str)){
			return "";
		}
		if(str.startsWith("/1.0/")){
			str=str.substring(5);
		} else {
			return "";
		}
		BigInteger srcBitInt=new BigInteger(str,16);
		String desc=srcBitInt.toString(10);
		desc=desc.substring(3);
		if(desc.length()%3>0){
			return "";
		}
		String val="";
		for (int i = 0; i < desc.length(); i+=3){
			val+=((char)Integer.parseInt(desc.substring(i,i+3)));
		}
		return val;
	}

	public static void main(String[] args) {
		String username="abcd=1234";
		System.out.println("原始字符串:"+username);
		//username= com.sccysoft.utils.TextEncoder.encode(username);
		System.out.println("加密后的字符串:"+username);
		//username= com.sccysoft.utils.TextEncoder.decode(username);
		System.out.println("解密后的字符串:"+username);
	}
}
