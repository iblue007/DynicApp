package com.felink.corelib.analytics;

import android.content.Context;
import android.util.Log;

import com.felink.corelib.config.BaseConfig;
import com.nd.analytics.NdAnalytics;
import com.nd.analytics.NdAnalyticsSettings;

/**
 * Created by linliangbin on 2016/10/13.
 */

public class HiAnalytics {

    private static int init = -1;
    private static final String TAG = "HiAnalytics";
    /*
     * 用户统计数据上传后的广播
     */
    public static final String START_UP_ACTION = "com.nd.analytics.startup";
    public static final String START_UP_EXTRA = "startup_result";

    /**
     *区别分支版本升级统计key
     */
    public static final String CONST_STRING_KEY_ANALYTICS_WAY = "key_analytics_way";



    /**
     * 统计分析初始化
     * @param context
     *            不能使用Application Context
     */
    public static void init(Context context) {
        if (init != -1)
            return;

        // 初始化数据分析
        NdAnalyticsSettings settings = new NdAnalyticsSettings();
        settings.setAppId(BaseConfig.APPID);
        settings.setAppKey(BaseConfig.APPKEY);
//        Log.e("HiAnalytics", "AppId:" + BaseConfig.APPID +"   AppKey:" + BaseConfig.APPKEY);
        NdAnalytics.setReportStartupOnlyOnceADay(true);
        NdAnalytics.initialize(context, settings);
        init = 1;
        Log.e(TAG, "=============================HiAnalytics.init=============================");
    }



    public static void submitEvent(Context context, int eventId, String label) {
        NdAnalytics.onEvent(context, eventId, label);
    }


    public static void submitEvent(Context context, int eventId) {
        submitEvent(context, eventId, "");
    }

    public static String getChannel(Context ctx) {
        return NdAnalytics.getChannel(ctx);
    }

    /**
     * Description: 统计活跃
     * Author: linliangbin
     * Date: 2016/11/17 16:52
     */
    public static void startUp(Context ctx) {
        NdAnalytics.startup(ctx);
        Log.e(TAG, "=============================HiAnalytics.startUp=============================");
    }

}
