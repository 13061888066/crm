package com.mage.crm.util;

import org.apache.commons.codec.binary.Base64;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    public static String enCode(String str){
        String str2 = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] bytes = md5.digest(str.getBytes());
            str2 = Base64.encodeBase64String(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str2;
    }

    public static void main(String[] args) {
        String s = Md5Util.enCode("123456");
        System.out.println(s);
    }
}
