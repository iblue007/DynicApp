package com.felink.corelib.kitset;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: </br>
 * Author: cxy
 * Date: 2017/3/3.
 */

public class NetLibUtil {

    /**
     * 取得IMEI号
     *
     * @param ctx
     * @return
     */
    public static String getIMEI(Context ctx) {
        if (ctx == null)
            return "91";

        String imei = "91";
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
            if (imei == null || "".equals(imei))
                return "91";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imei;
    }

    public static String getIMSI(Context ctx) {
        if (ctx == null)
            return "91";

        String imsi = "91";
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
            if (imsi == null || "".equals(imsi))
                return "91";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imsi;
    }

    /**
     * 通过反射的方法，获取CUID
     *
     * @param ctx
     */
    public static String getCUID(Context ctx) {
        if (null == ctx)
            return "";

//        if (Build.VERSION.SDK_INT >= 23) {
//            if (Settings.System.canWrite(ctx)) {
//
//            } else {
//                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:" + Global.getPkgName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(intent);
//            }
//        }
        //TODO:xuqunxing
        return "91";//NdAnalytics.getCUID(ctx);
    }

    /**
     * 获取versionName
     *
     * @param context
     * @return String
     */
    public static String getDivideVersion(Context context) {
        String versionName = "";
        try {
            PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versionName = packageinfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


    public static boolean isZh(Context context) {
        Locale lo;
        if (null == context) {
            return true;
        } else {
            lo = context.getResources().getConfiguration().locale;
        }
        if (lo.getLanguage().equals("zh"))
            return true;
        return false;
    }

    public static String utf8URLencode(String url) {
        StringBuffer result = new StringBuffer();
        if (url != null)
            for (int i = 0; i < url.length(); i++) {
                char c = url.charAt(i);
                if ((c >= 0) && (c <= 255)) {
                    result.append(c);
                } else {
                    byte[] b = new byte[0];
                    try {
                        b = Character.toString(c).getBytes("utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        if (k < 0)
                            k += 256;
                        result.append("%" + Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        return result.toString();
    }

    /**
     * <br>Description: 替换非法字符(去除非中文、字母、数字、空格、下划线、"-"的字符)
     * <br>Author:caizp
     * <br>Date:2016年10月13日下午3:11:21
     */
    public static String replaceIllegalCharacter(String source, String replaceChar) {
        if (TextUtils.isEmpty(replaceChar)) {
            return source;
        }
        if (TextUtils.isEmpty(source)) {
            return "";
        }
        String regex = "[^\\sa-zA-Z0-9\u4e00-\u9fa5_-]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        String result = matcher.replaceAll(replaceChar);
        return result;
    }


    public static String getBuildMode() {
        return Build.MODEL;
    }

    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }
}
