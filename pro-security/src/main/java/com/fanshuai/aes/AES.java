package com.fanshuai.aes;

import com.fanshuai.util.CodecUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class AES {
    private static Key key = null;
    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(new SecureRandom());
            key = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encode(String text) {
        byte[] bytes = text.getBytes();

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encodeBytes = cipher.doFinal(bytes);
            return CodecUtil.bytesToHexString(encodeBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(String encodeText) {
        byte[] encodeBytes = CodecUtil.hexStringToBytes(encodeText);
        if (null == encodeBytes) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] bytes = cipher.doFinal(encodeBytes);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String text = "介绍费已大商股份因为俺师傅于死地  苏菲487天为";

        String encoded = encode(text);
        String decoded = decode(encoded);

        System.out.println(encoded);
        System.out.println(decoded);
    }
}
