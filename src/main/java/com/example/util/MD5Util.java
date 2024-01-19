//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Util {
    public MD5Util() {
    }

    public static String stringToMD5(String str, String encode) {
        try {
            byte[] strTemp = str.getBytes(encode);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            return toHexString(mdTemp.digest());
        } catch (Exception var4) {
            return null;
        }
    }

    public static String MD5Code(String mContent, String encoding) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] strContent = mContent.getBytes(encoding);
            MessageDigest mDM = MessageDigest.getInstance("MD5");
            mDM.update(strContent);
            byte[] md = mDM.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte mData = md[i];
                str[k++] = hexDigits[mData >>> 4 & 15];
                str[k++] = hexDigits[mData & 15];
            }

            return new String(str);
        } catch (Exception var11) {
            var11.getMessage();
            return null;
        }
    }

    public static String streamToMD5(InputStream inputStream) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            boolean var3 = false;

            int numRead;
            while((numRead = inputStream.read(buffer)) > 0) {
                mdTemp.update(buffer, 0, numRead);
            }

            return toHexString(mdTemp.digest());
        } catch (Exception var4) {
            return null;
        }
    }

    public static String fileNameToMD5(String fileName) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(fileName);
            String var4 = streamToMD5(inputStream);
            return var4;
        } catch (Exception var12) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var11) {
                }
            }

        }

        return null;
    }

    private static String toHexString(byte[] md) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int j = md.length;
        char[] str = new char[j * 2];

        for(int i = 0; i < j; ++i) {
            byte byte0 = md[i];
            str[2 * i] = hexDigits[byte0 >>> 4 & 15];
            str[i * 2 + 1] = hexDigits[byte0 & 15];
        }

        return new String(str);
    }
}
