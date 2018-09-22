package com.fanshuai.messagedigest;

import com.fanshuai.util.CodecUtil;

import java.security.MessageDigest;
import java.util.Arrays;

import static java.lang.System.out;

public class MD5 {
    public static void main(String[] args) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            String text = "你好，世界";
            byte[] data = text.getBytes();
            sha256.update(data);
            sha512.update(data);
            md5.update(data);

            byte[] encodeSha256 = sha256.digest();
            byte[] encodeSha512 = sha512.digest();
            byte[] encodeMd5 = md5.digest();

            out.println(CodecUtil.bytesToHexString(encodeSha256));
            out.println(CodecUtil.bytesToHexString(encodeSha512));
            out.println(CodecUtil.bytesToHexString(encodeMd5));

            MessageDigest md52 = MessageDigest.getInstance("MD5");
            md52.update(Arrays.copyOfRange(data, 0, data.length/2));
            md52.update(Arrays.copyOfRange(data, data.length/2, data.length));

            byte[] encodeMd52 = md52.digest();
            System.out.println(CodecUtil.bytesToHexString(encodeMd52));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
