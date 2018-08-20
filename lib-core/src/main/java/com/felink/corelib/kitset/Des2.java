package com.felink.corelib.kitset;

/**
 * @ProjectName Cmobo_launcher
 * @Author C.xt
 * @Version 1.0.0
 * @CreateDate： 2015-7-24上午10:11:53
 * @JDK <JDK1.6>
 * Description:							DES 加密
 */

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des2 {

    public static final String ALGORITHM_3DES = "DESede/ECB/PKCS7Padding";

    public static final String ALGORITHM_DES = "DES/CBC/PKCS7Padding";

    public final static byte[] KEY_3DES = {57, 99, 102, 51, 51, 51, 51, 56, 57, 99, 102, 51, 51, 51, 51, 56, 57, 99, 102, 51, 51, 51, 51, 56};

    //    public final static byte[] KEY = {18, 71, (byte) 219, 97, 71, 112, (byte) 235, (byte) 184};
    public final static byte[] KEY = {57, 99, 102, 51, 51, 51, 51, 56};

    public final static byte[] DESIV = {57, 99, 102, 51, 51, 51, 51, 56};

//    public final static byte[] DESIV = {(byte) 234, 75, 16, 113, 41, 8, (byte) 143, 99};

    public final static String CHARSET_UTF8 = "UTF-8";


    /**
     * @param key   加密私钥，长度不能够小于8位
     * @param desiv
     * @param data  待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @Author C.xt
     * @Title: encode
     * @Description: DES算法，加密
     * @date 2015-7-27下午4:08:21
     */
    public static String encode(byte[] key, byte[] desiv, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(desiv);
            AlgorithmParameterSpec paramSpec = iv;
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);

            return parseByte2HexStr(bytes);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * @param key   解密私钥，长度不能够小于8位
     * @param desiv
     * @param data  待解密字符串
     * @return 解密后的字节数组byte[]
     * @throws Exception Exception 异常
     * @Author C.xt
     * @Title: decode
     * @Description:
     * @date 2015-7-27下午4:10:04
     */
    public static byte[] decode(byte[] key, byte[] desiv, byte[] data) throws Exception {
        try {
            //         	SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(desiv);
            AlgorithmParameterSpec paramSpec = iv;
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * @param key   加密私钥，长度不能够小于8位
     * @param desiv
     * @param data  待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @Author C.xt
     * @Title: encode
     * @Description: 3DES算法，加密
     * @date 2015-7-27下午4:08:21
     */
    public static String encode3Des(byte[] key, byte[] desiv, byte[] data) throws Exception {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            //key的长度不能够小于24位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_3DES);
            //ECB模式不需要IV
//            IvParameterSpec iv = new IvParameterSpec(desiv);
//            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);

            return parseByte2HexStr(bytes);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * @param key   解密私钥，长度不能够小于8位
     * @param desiv
     * @param data  待解密字符串
     * @return 解密后的字节数组byte[]
     * @throws Exception Exception 异常
     * @Author C.xt
     * @Title: decode
     * @Description:
     * @date 2015-7-27下午4:10:04
     */
    public static byte[] decode3Des(byte[] key, byte[] desiv, byte[] data) throws Exception {
        try {
            //         	SecureRandom sr = new SecureRandom();
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            //key的长度不能够小于24位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_3DES);
            //ECB模式不需要IV
//            IvParameterSpec iv = new IvParameterSpec(desiv);
//            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * @param buf
     * @return String
     * @throws
     * @Author C.xt
     * @Title: parseByte2HexStr
     * @Description: 将二进制转换为16进制
     * @date 2015-7-23下午2:37:10
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * @param hexStr
     * @return byte[]
     * @throws
     * @Author C.xt
     * @Title: parseHexStr2Byte
     * @Description: 将16进制转换为二进制
     * @date 2015-7-23下午2:37:00
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
