package com.felink.corelib.kitset;

import android.content.Context;

import com.felink.corelib.analytics.HiAnalytics;

/**
 * Description: 渠道工具</br>
 * Author: cxy
 * Date: 2017/2/28.
 */

public class ChannelUtil {

    /**
     * 获取渠道号
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        try {
            return HiAnalytics.getChannel(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
