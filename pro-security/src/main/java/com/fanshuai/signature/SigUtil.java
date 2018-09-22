package com.fanshuai.signature;

import com.fanshuai.util.CodecUtil;
import org.apache.commons.lang.StringUtils;

import java.security.*;

public class SigUtil {
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

    public static String sign(String text) throws Exception{
        if (StringUtils.isBlank(text)) {
            throw new Exception("sign text empty");
        }

        byte[] bytes = text.getBytes();

        try {
            PrivateKey privateKey = keyPair.getPrivate();
            Signature signature = Signature.getInstance("MD5withRSA");

            signature.initSign(privateKey);
            signature.update(bytes);
            byte[] sign = signature.sign();
            return CodecUtil.bytesToHexString(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(String text, String sign) throws Exception{
        byte[] signBytes = CodecUtil.hexStringToBytes(sign);
        if (null == signBytes) {
            throw new Exception("sign is empty");
        }
        if (StringUtils.isBlank(text)) {
            throw new Exception("sign text is empty");
        }

        byte[] src = text.getBytes();
        try {
            PublicKey publicKey = keyPair.getPublic();
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);

            signature.update(src);
            boolean b = signature.verify(signBytes);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) throws Exception{
        String text = "UI恶化第三方已而我是多恢复也任务的划分 UI热水袋回复489375儿童";

        //数字签名
        String textSig = sign(text);
        //签名验证
        boolean signSuccess = verify(text, textSig);

        System.out.println(textSig);
        System.out.println(signSuccess);
    }
}
