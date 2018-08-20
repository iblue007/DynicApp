package com.felink.corelib.kitset;

import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by linliangbin on 2016/10/12.
 */

public class DigestUtil {

    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public DigestUtil() {
    }

    static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException(var2.getMessage());
        }
    }

    public static String MD5(String str) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception var8) {
            var8.printStackTrace();
            return str;
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int md5Bytes = 0; md5Bytes < charArray.length; ++md5Bytes) {
            byteArray[md5Bytes] = (byte) charArray[md5Bytes];
        }

        byte[] var9 = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < var9.length; ++i) {
            int val = var9[i] & 255;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    private static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }

    public static byte[] md5(byte[] data) {
        return getMd5Digest().digest(data);
    }

    public static byte[] md5(String data) {
        return md5(data.getBytes());
    }

    public static String md5Hex(String data) {
        if(!TextUtils.isEmpty(data)){
            return new String(encodeHex(md5(data)));
        }
        return "";
    }

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int j = 0; i < l; ++i) {
            out[j++] = DIGITS[(240 & data[i]) >>> 4];
            out[j++] = DIGITS[15 & data[i]];
        }

        return out;
    }

    public static String md5Encode(String value) {
        return md5Encode(value, (String) null);
    }

    public static String md5Encode(String value, String encode) {
        String tmp = null;
        encode = encode != null && encode.length() != 0 ? encode : "utf8";

        try {
            tmp = md5Encode(value.getBytes(encode));
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        return tmp;
    }

    public static String md5Encode(byte[] datas) {
        String tmp = null;

        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(datas);
            byte[] md = e.digest();
            tmp = binToHex(md);
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }

        return tmp;
    }

    public static String binToHex(byte[] md) {
        StringBuffer sb = new StringBuffer("");
        boolean read = false;

        for (int i = 0; i < md.length; ++i) {
            int var4 = md[i];
            if (var4 < 0) {
                var4 += 256;
            }

            if (var4 < 16) {
                sb.append("0");
            }

            sb.append(Integer.toHexString(var4));
        }

        return sb.toString();
    }

    /**
     * RandomAccessFile 获取文件的MD5值
     *
     * @param file 文件路径
     * @return md5
     */
    public static String getFileMD5(File file) {
        MessageDigest messageDigest;
        RandomAccessFile randomAccessFile = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            if (file == null) {
                return "";
            }
            if (!file.exists()) {
                return "";
            }
            randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[1024 * 1024 * 2];
            int len = 0;
            while ((len = randomAccessFile.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            String md5 = bigInt.toString(16);
            while (md5.length() < 32) {
                md5 = "0" + md5;
            }
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    randomAccessFile = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
