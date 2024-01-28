package com.example.util;

import java.util.Collection;
import java.util.Random;

public class Strings {

	public static boolean isEmpty(String str)
	{
		return str==null||"".equals(str);
	}
	public static boolean isEmpty(@SuppressWarnings("rawtypes") Collection c)
	{
		return (c == null) || (c.isEmpty());
	}
	/**
	 * 判断字符串为空或空值
	 * @param str
	 * 待判断字符串
	 * @return
	 * 返回是否为空
	 */
	public static boolean isBlank(String str){
		if(str==null|| "".equals(str) || "null".equalsIgnoreCase(str) || "\"null\"".equalsIgnoreCase(str)){
			return true;
		}
		return false;
	}

	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}

	public static String escapeUnsupportAscii(String value)
	{
		if (isEmpty(value)) {
			return value;
		}
		char[] chars = value.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] >= '\b') &&
					(chars[i] != '\013') && (
							(chars[i] < '\016') || (chars[i] > '\037'))) {
				chars[(pos++)] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	/**
	 * 格式化函数
	 * eg.
	 * ("%04d", 99) -> 0099
	 * ("%.2f", 99.999) -> 99.99
	 * @param format
	 * @param args
	 * @return
	 */
	public static String format(String format, Object... args){
		return String.format(format, args);
	}

	public static String randomHexString(int len){
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<len;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return result.toString().toUpperCase();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
