package com.felink.corelib.webview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;


//import com.jd.ads.commons.RSACoder;
//import com.jdwx.sdk.ApiManager;


/**
 * 京东SDK 工具类
 * Created by linliangbin on 16-6-28.
 */
public class JDSdkToolForCommon {
    
    
    /**
     * INTENT 中传递数据标识
     */
    public static final String INTENT_TAG = "viaJd" ;
    
    /**
     * INTENT 中传递的数据来源
     */
    public static final String INTENT_FROM_TAG = "from";
    /**
     * INTENT 中传递的地址
     */
    public static final String INTENT_TAG_URL = "viaJdUrl" ;
    
    
    public static String JINGDONG_ICON_URL = "http://x.jd.com/sia?ad_id=2100";
    
    
    /**
     * 打开地址中的参数
     */
    public static final String USE_JD_SDK_TAG1 = "?isViaJDSdkFor91=1";
    public static final String USE_JD_SDK_TAG2 = "&isViaJDSdkFor91=1";
    
    //来源-未知
    public static final String FROM_POSITION_UNKNOWN = "un";
    //来源-零屏
    public static final String FROM_POSITION_NAVIGATION = "0p";
    //来源-LOADING
    public static final String FROM_POSITION_LOADING = "ld";
    //来源-PUSH
    public static final String FROM_POSITION_PUSH = "ts";
    //来源-ICON 
    public static final String FROM_POSITION_ICON = "tb";
    //来源-APP
    public static final String FROM_POSITION_APP = "yy";
    
    
    
    
    /**
     * 当前appinfo 是否可用
     * @param applicationInfo
     * @return
     */
//    public static boolean isAppInfoAvailable(ApplicationInfo applicationInfo){
//        if(applicationInfo == null)
//            return  false;
//        if(applicationInfo.componentName == null)
//            return false;
//        if(applicationInfo.componentName.getPackageName() == null)
//            return false;
//        return true;
//    }
    
    public static boolean isJDApp(ApplicationInfo applicationInfo){
        
//        if(isAppInfoAvailable(applicationInfo)){
//            return isJDApp(applicationInfo.componentName.getPackageName());
//        }
        return false;
    }
    
//    public static boolean isJDApp(String pkg){
//
//        if(TextUtils.isEmpty(pkg))
//            return false;
//        if("com.jingdong.app.mall".equals(pkg)){
//            return true;
//        }
//        return false;
//    }
 
    
    /**
     * 对给定的京东URL 添加IMEI 号并加密处理
     * @param context
     * @param url
     * @return
     */
//    private static String secretUrl(Context context,String url){
//        String newurl =url;
//
//        String source_str = "device_type:8\ndevice_id:"+"\"" + TelephoneUtil.getIMEI(context) + "\"";
//        try {
//            String device_info = RSACoder.encryptByPublicKey(source_str, RSACoder.PUBLIC_KEY);
//            newurl = url + "&did=" + device_info;
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return newurl;
//    }
    
 
    
    /**
     * 使用京东SDK 打开
     *
     * @param context
     * @param url 为未添加加密IMEI参数的地址
     * @return 打开失败时返回false
     */
    public static boolean startJdSDKWithRawUrl(Context context, String url, String from){
//        try {
//            //过滤多余参数
//            String noParamUrl  = getUrlWithouJDParams(url);
//            //加密IMEI号信息
//            String secretUrl = secretUrl(context,noParamUrl);
//            //使用SDK打开地址
//            ApiManager.getInstance().openUrl(context, secretUrl);
//
//            if(TextUtils.isEmpty(from)){
//                from = FROM_POSITION_UNKNOWN;
//            }
//            HiAnalytics.submitEvent(context, AnalyticsConstant.NAVIGATION_SCREEN_JD_SDK_OPEN_URL, from);
//        }catch (Throwable t){
//            return false;
//        }
//        return true;
        return false;
    }
    
    /**
     * 启动京东SDK
     * 地址为已经加密的地址，直接打开
     * 启动出现异常，则使用内置浏览器打开
     */
//    public static boolean startJdSDK(Context context,String url){
//        try {
//            ApiManager.getInstance().openUrl(context, url);
//        }catch (Throwable t){
//            return false;
//        }
//        return true;
//    }
    
   
    /**
     * 当前地址是否为使用京东SDK 打开的地址
     * @param url
     * @return
     */
    public static boolean isUrlViaJDSdk(String url){
//        if(TextUtils.isEmpty(url))
//            return false;
//        if(url.contains(USE_JD_SDK_TAG1) || url.contains(USE_JD_SDK_TAG1))
//            return true;
        return false;
        
    }
    
    /**
     * 将当前地址京东相关的参数替换掉，
     * 返回过滤京东打开参数后的地址
     * @param url
     * @return
     */
    public static String getUrlWithouJDParams(String url){
        
        if(TextUtils.isEmpty(url))
            return url;
        url = url.replace(USE_JD_SDK_TAG1,"");
        url = url.replace(USE_JD_SDK_TAG2,"");
        
        return url;
        
    }
    
}
