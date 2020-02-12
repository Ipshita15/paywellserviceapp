package com.cloudwell.paywell.services.utils.algorithem;

import javax.crypto.spec.SecretKeySpec;




/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2020-01-23.
 */
public class AES {
    public static SecretKeySpec secretKey;
    public static byte[] key;


//    public static String decrypt(String strToDecrypt, String secret) {
//        try {
//
//
//            key = myKey.getBytes("UTF-8");
//            sha = MessageDigest.getInstance("SHA-1");
//            key = sha.digest(key);
//            key = Arrays.copyOf(key, 16);
//            secretKey = new SecretKeySpec(key, "AES");
//
//
//
//
//
//
//            key = strToDecrypt.substring(0, 16).getBytes();
//            secretKey = new SecretKeySpec(key, "AES");
//            String lastData = strToDecrypt.substring(17, strToDecrypt.length() - 1);
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            return new String(cipher.doFinal(Base64.decode(lastData.getBytes(), Base64.DEFAULT)));
//        } catch (Exception e) {
//            System.out.println("Error while decrypting: " + e.toString());
//        }
//        return null;
//    }


//    public static String decrypt(String key, String initVector, String encrypted) {
//        try {
//            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//
//            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
//
//            return new String(original);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }

//    public static String decrypt(String key, String initVector,  String encrypted) {
//        try
//        {
//            IvParameterSpec ivspec = new IvParameterSpec(initVector.getBytes("UTF-8"));
//
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec(key.toCharArray(), 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
//            return new String(cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT)));
//        }
//        catch (Exception e) {
//            System.out.println("Error while decrypting: " + e.toString());
//        }
//        return null;
//    }


}
