package com.mage.crm.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {


    /**
     * base64加密
     * @param str
     * @return
     */
    public static String enCode(String str){
        String s = Base64.encodeBase64String(str.getBytes());
        return s;

    }

    /**
     * base64解密
     * @param str
     * @return
     */
    public static String decode(String str){
        byte[] bytes = Base64.decodeBase64(str);
        return new String(bytes);
    }

}
