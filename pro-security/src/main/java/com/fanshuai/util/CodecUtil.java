package com.fanshuai.util;

import org.apache.commons.lang.StringUtils;

public class CodecUtil {
    public static String bytesToHexString(byte[] data) {
        char[] charArray = "0123456789ABCDEF".toCharArray();

        if (null == data || data.length == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            builder.append(charArray[(b & 0xF0) >> 4]);
            builder.append(charArray[b & 0x0F]);
        }

        return builder.toString();
    }

    public static byte[] hexStringToBytes(String hex) {
        if (StringUtils.isBlank(hex)) {
            return null;
        }

        byte[] bytes = new byte[hex.length()/2];
        for (int i = 0; i < bytes.length; i++) {
            int pos = i * 2;
            byte high4 = (byte) (getByte(hex.charAt(pos)) << 4);
            byte low4 = (byte)getByte(hex.charAt(pos + 1));
            bytes[i] = (byte)(high4 | low4);
        }

        return bytes;
    }

    public static int getByte(char ch) {
        int b = "0123456789ABCDEF".indexOf(ch);
        return b;
    }
}
