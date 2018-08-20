package com.felink.corelib.net.base;

import android.content.Context;
import android.text.TextUtils;

import com.felink.corelib.config.BaseConfig;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.DigestUtil;
import com.felink.corelib.kitset.NetLibUtil;
import com.felink.corelib.kitset.ScreenUtil;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by dingdj on 2016/5/13.
 */
public class HttpRequestParam {

    public static String DivideVersion;
    public static String SupPhone;
    public static String SupFirm;
    public static String IMEI;
    public static String IMSI;
    public static String CUID;
    public static final String MT = "4";
    public static final String ProtocolVersion_2 = NetLibUtil.utf8URLencode("2.0");
    public static final String ProtocolVersion_3 = NetLibUtil.utf8URLencode("3.0");
    public static final String ProtocolVersion_4 = NetLibUtil.utf8URLencode("4.0");
    public static final String LAUNCHER_REQUEST_KEY = "27B1F81F-1DD8-4F98-8D4B-6992828FB6E2";
    public static final String PO_REQUEST_KEY = "2B1F781F-1D8D-984F-D84B-9826E6928FB2";
    public static final String REQUEST_KEY = "58D1BAC3-3477-4870-9AD4-4879259652B7";

    public static void addGlobalLauncherRequestValue(HashMap<String, String> paramsMap, String jsonParams) {
//        String sessionID = getUserSession(Global.getApplicationContext()).sessionId;
//        if (sessionID == null) sessionID = "";
        String sessionID = "";
        addGlobalLauncherRequestValue(paramsMap, jsonParams, sessionID);
    }


    /**
     * 添加桌面接口通用参数
     *
     * @param paramsMap
     * @param jsonParams
     */
    public static void addGlobalLauncherRequestValue(HashMap<String, String> paramsMap, String jsonParams, String sessionId) {
        Context ctx = Global.getApplicationContext();
        if (paramsMap == null)
            return;
        if (jsonParams == null) {
            jsonParams = "";
        }
        try {
            if (null == DivideVersion)
                DivideVersion = NetLibUtil.utf8URLencode(NetLibUtil.getDivideVersion(ctx));
            if (null == SupPhone)
                SupPhone = NetLibUtil.utf8URLencode(NetLibUtil.getBuildMode());
            if (null == SupFirm)
                SupFirm = NetLibUtil.utf8URLencode(NetLibUtil.getBuildVersion());
            if (null == IMEI)
                IMEI = NetLibUtil.utf8URLencode(NetLibUtil.getIMEI(ctx));
            if (null == IMSI)
                IMSI = NetLibUtil.utf8URLencode(NetLibUtil.getIMSI(ctx));
            if (null == CUID)
                CUID = URLEncoder.encode(NetLibUtil.getCUID(ctx), "UTF-8");

            CUID = TextUtils.isEmpty(CUID) ? "123456789" : CUID;
            sessionId = sessionId == null ? "" : sessionId;

            paramsMap.put("PID", BaseConfig.APPID + "");
            paramsMap.put("MT", MT);
            paramsMap.put("DivideVersion", DivideVersion);
            paramsMap.put("SupPhone", SupPhone);
            paramsMap.put("SupFirm", SupFirm);
            paramsMap.put("IMEI", IMEI);
            paramsMap.put("IMSI", IMSI);
            paramsMap.put("SessionId", sessionId);
            paramsMap.put("CUID", CUID);//通用用户唯一标识 NdAnalytics.getCUID(ctx)
            paramsMap.put("ProtocolVersion", ProtocolVersion_3);
            String Sign = DigestUtil.md5Hex(BaseConfig.APPID + MT + DivideVersion + SupPhone + SupFirm + IMEI + IMSI + sessionId + CUID + ProtocolVersion_3 + jsonParams + LAUNCHER_REQUEST_KEY);
            paramsMap.put("Sign", Sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加PO接口通用参数
     *
     * @param paramsMap
     * @param jsonParams
     */
    public static void addGlobalPoRequestValue(HashMap<String, String> paramsMap, String jsonParams, String sessionId) {
        Context ctx = Global.getApplicationContext();
        if (paramsMap == null)
            return;
        if (jsonParams == null) {
            jsonParams = "";
        }
        try {
            if (null == DivideVersion)
                DivideVersion = NetLibUtil.utf8URLencode(NetLibUtil.getDivideVersion(ctx));
            if (null == SupPhone)
                SupPhone = NetLibUtil.utf8URLencode(NetLibUtil.getBuildMode());
            if (null == SupFirm)
                SupFirm = NetLibUtil.utf8URLencode(NetLibUtil.getBuildVersion());
            if (null == IMEI)
                IMEI = NetLibUtil.utf8URLencode(NetLibUtil.getIMEI(ctx));
            if (null == IMSI)
                IMSI = NetLibUtil.utf8URLencode(NetLibUtil.getIMSI(ctx));
            if (null == CUID)
                CUID = URLEncoder.encode(NetLibUtil.getCUID(ctx), "UTF-8");

            CUID = TextUtils.isEmpty(CUID) ? "123456789" : CUID;
            sessionId = sessionId == null ? "" : sessionId;
            String resolution = ScreenUtil.getCurrentScreenWidth(ctx) + "x" + ScreenUtil.getCurrentScreenHeight(ctx);

            paramsMap.put("PID", BaseConfig.PID);
            paramsMap.put("MT", MT);
            paramsMap.put("DivideVersion", DivideVersion);
            paramsMap.put("SupPhone", SupPhone);
            paramsMap.put("SupFirm", SupFirm);
            paramsMap.put("IMEI", IMEI);
            paramsMap.put("IMSI", IMSI);
            paramsMap.put("SessionId", sessionId);
            paramsMap.put("CUID", CUID);//通用用户唯一标识 NdAnalytics.getCUID(ctx)
            paramsMap.put("ProtocolVersion", ProtocolVersion_3);
            paramsMap.put("Resolution", resolution);
            String Sign = DigestUtil.md5Hex(BaseConfig.PID + MT + DivideVersion + SupPhone + SupFirm + IMEI + IMSI + sessionId + CUID + resolution + ProtocolVersion_3 + jsonParams + PO_REQUEST_KEY);
            paramsMap.put("Sign", Sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class UserSession {
        public String appName;
        public int appId;
        public String nickName;
        public String loginUin;
        public String sessionId;
    }

    /**
     * 添加接口通用参数
     *
     * @param paramsMap
     * @param jsonParams
     */
    public static void addGlobalRequestValue(HashMap<String, String> paramsMap, String jsonParams, String sessionId) {
        Context ctx = Global.getApplicationContext();
        if (paramsMap == null)
            return;
        if (jsonParams == null) {
            jsonParams = "";
        }
        try {
            if (null == DivideVersion)
                DivideVersion = NetLibUtil.utf8URLencode(NetLibUtil.getDivideVersion(ctx));
            if (null == SupPhone)
                SupPhone = NetLibUtil.utf8URLencode(NetLibUtil.getBuildMode());
            if (null == SupFirm)
                SupFirm = NetLibUtil.utf8URLencode(NetLibUtil.getBuildVersion());
            if (null == IMEI)
                IMEI = NetLibUtil.utf8URLencode(NetLibUtil.getIMEI(ctx));
            if (null == IMSI)
                IMSI = NetLibUtil.utf8URLencode(NetLibUtil.getIMSI(ctx));
            if (null == CUID)
                CUID = URLEncoder.encode(NetLibUtil.getCUID(ctx), "UTF-8");

            sessionId = sessionId == null ? "" : sessionId;

            paramsMap.put("PID", BaseConfig.APPID + "");
            paramsMap.put("MT", MT);
            paramsMap.put("DivideVersion", DivideVersion);
            paramsMap.put("SupPhone", SupPhone);
            paramsMap.put("SupFirm", SupFirm);
            paramsMap.put("IMEI", IMEI);
            paramsMap.put("IMSI", IMSI);
            paramsMap.put("SessionId", sessionId);
            paramsMap.put("CUID", CUID);//通用用户唯一标识 NdAnalytics.getCUID(ctx)
            paramsMap.put("ProtocolVersion", ProtocolVersion_2);
            String Sign = DigestUtil.md5Hex(BaseConfig.APPID + MT + DivideVersion + SupPhone + SupFirm + IMEI + IMSI + sessionId + CUID + ProtocolVersion_2 + jsonParams + REQUEST_KEY);
            paramsMap.put("Sign", Sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
