package com.nd.hilauncherdev.webconnect.downloadmanage;

import android.content.Context;
import android.text.TextUtils;

import com.felink.corelib.config.BaseConfig;
import com.felink.corelib.kitset.ChannelUtil;
import com.felink.corelib.kitset.NetLibUtil;
import com.felink.corelib.net.VideopaperHttpRequestParam;
import com.felink.corelib.net.base.HttpRequestParam;
import com.felink.corelib.kitset.TelephoneUtil;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DownloadUrlManage {
    /**
     * 桌面统一下载应用地址
     */
    public static final String PANDAHOME_BASE_URL = "http://pandahome.ifjing.com/";
    public static final String UNIFIED_DOWNLOAD_PATH = PANDAHOME_BASE_URL + "soft/download.aspx?Identifier=%s&sp=%d";
    /** 熊猫桌面项目编号 */
    public static final String PROJECT_OPTION = "1900";
    public static String getDownloadUrlFromPackageName(Context context, String packageName, String sessionID, int sp) {
        if (context == null || packageName == null || "".equals(packageName)) {
            return null;
        }
        String downloadAddress = String.format(UNIFIED_DOWNLOAD_PATH, packageName,sp);
        StringBuffer result = new StringBuffer(downloadAddress);
        // 拼接通用统计参数
        addGlobalRequestValue(context, result, sessionID);
        return result.toString();
    }
    /**
     * 添加全局参数
     * Title: addGlobalRequestValue
     * Description:
     * @param context
     * @param sb
     * @param sessionID
     *            能获取到就传，获取不到就传null
     * @author maolinnan_350804
     */
    public static void addGlobalRequestValue(Context context, StringBuffer sb, String sessionID) {
        if (sb == null || context == null)
            return;

        String imsiNumber = TelephoneUtil.getIMSI(context);
        if (null == imsiNumber) {
            imsiNumber = "";
        }
        String imeiNumber = TelephoneUtil.getIMEI(context);
        if (null == imeiNumber) {
            imeiNumber = "";
        }
        appendAttrValue(sb, "mt", HttpRequestParam.MT);
        appendAttrValue(sb, "tfv", "40000");
        appendAttrValue(sb, "pid", BaseConfig.APPID + "");
        appendAttrValue(sb, "imei", imeiNumber);
        appendAttrValue(sb, "imsi", imsiNumber);
        //TODO:zhou 去掉视频壁纸用不到
        //appendAttrValue(sb, "projectoption", PROJECT_OPTION);
        appendAttrValue(sb, "DivideVersion", TelephoneUtil.getVersionName(context, context.getPackageName()));
        appendAttrValue(sb, "SupPhone", encodeAttrValue(TelephoneUtil.getMachineName())); // 型号
        appendAttrValue(sb, "supfirm", TelephoneUtil.getFirmWareVersion()); // Android版本号
        appendAttrValue(sb, "company", encodeAttrValue(TelephoneUtil.getManufacturer())); // 制造商
        appendAttrValue(sb, "nt", TelephoneUtil.getNT(context)); // 网络类型
        appendAttrValue(sb, "chl", ChannelUtil.getChannel(context)); // 渠道ID
        String CUID =NetLibUtil.getCUID(context);
        CUID = TextUtils.isEmpty(CUID) ? "123456789" : CUID;
        appendAttrValue(sb, "CUID", encodeAttrValue(CUID)); //加入CUID
        String isRoot = TelephoneUtil.hasRootPermission() ? "1" : "0"; // 是否root
        appendAttrValue(sb, "JailBroken", isRoot); // 渠道ID
        if (sessionID != null && !"".equals(sessionID)) {
            appendAttrValue(sb, "sessionid", sessionID);
        }
    }

    /**
     * 拼接参数
     * <p>
     * Title: appendAttrValue
     * Description:
     * @param sb
     * @param key
     * @param values
     * @author maolinnan_350804
     */
    private static void appendAttrValue(StringBuffer sb, String key, String... values) {
        if (sb.indexOf("?" + key + "=") != -1 || sb.indexOf("&" + key + "=") != -1) {
            return;
        }
        for (String value : values) {
            if (sb.indexOf("?") == -1) {
                sb.append("?");
            } else {
                sb.append("&");
            }
            sb.append(key);
            sb.append("=");
            sb.append(value);
        }
    }

    /**
     * 对参数进行转码
     * <p>Title: encodeAttrValue</p>
     * <p>Description: </p>
     * @param value
     * @return
     * @author maolinnan_350804
     */
    private static String encodeAttrValue(String value){
        String returnValue = "";
        try {
            value = URLEncoder.encode(value+"", "UTF-8");
            returnValue = value.replaceAll("\\+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
