package com.fanshuai.rsa;

import com.fanshuai.util.CodecUtil;

import javax.crypto.Cipher;
import java.security.*;

public class Rsa {
    private static KeyPair keyPair = null;
    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //私钥加密
    public static String encode(String text) {
        PublicKey key = keyPair.getPublic();

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(text.getBytes());
            return CodecUtil.bytesToHexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //公钥解密
    public static String decode(String encodedText) {
        PrivateKey key = keyPair.getPrivate();

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encodedBytes = CodecUtil.hexStringToBytes(encodedText);
            if (null != encodedBytes) {
                byte[] bytes = cipher.doFinal(encodedBytes);
                return new String(bytes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception{
        String text = "UI额卫生防护已荣事达合肥";

        String encoded = encode(text);
        String decoded = decode(encoded);

        System.out.println(encoded);
        System.out.println(decoded);

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        keyPair = generator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        String alg = privateKey.getAlgorithm();
        String format = privateKey.getFormat();
        String key = CodecUtil.bytesToHexString(privateKey.getEncoded());

        PublicKey publicKey = keyPair.getPublic();
        String alg2 = publicKey.getAlgorithm();
        String format2 = publicKey.getFormat();
        String key2 = CodecUtil.bytesToHexString(publicKey.getEncoded());
    }
}
