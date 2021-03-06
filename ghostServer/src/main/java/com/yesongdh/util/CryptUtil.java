package com.yesongdh.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.yesongdh.bean.Admin;

public class CryptUtil {
	 
    /**
     * 进行MD5加密
     *
     * @param info 要加密的信息
     * @return String 加密后的字符串
     */
    public static String encryptToMD5(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String rs = byte2hex(digesta);
        return rs;
    }
    /**
     * 将二进制转化为16进制字符串
     *
     * @param b 二进制字节数组
     * @return String
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    
    public static void encrptPasswd(Admin admin) {
    	if (admin == null || admin.getPasswd() == null) {
			return;
		}
    	
    	String hashAlgorithName = "MD5";
        String password = admin.getPasswd();
        int hashIterations = 1024;
        String credentialSalt = UUID.randomUUID().toString();
        admin.setSalt(credentialSalt);
        ByteSource credentialsSalt = ByteSource.Util.bytes(credentialSalt);
        Object obj = new SimpleHash(hashAlgorithName, password, credentialsSalt, hashIterations);
        admin.setPasswd(obj.toString());
	}
}
