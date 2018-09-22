package com.fanshuai.base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {
    private static BASE64Encoder encoder = new BASE64Encoder();
    private static BASE64Decoder decoder = new BASE64Decoder();

    public static String getBase64(String content) {
        byte[] data = content.getBytes();

        return encoder.encode(data);
    }

    public static String getFromBase64(String base64) {
        try {
            byte[] bytes = decoder.decodeBuffer(base64);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String text = "牛二手房入耳式对方";

        String base64 = getBase64(text);
        String originalText = getFromBase64(base64);

        System.out.println(base64);
        System.out.println(originalText);
    }

}
