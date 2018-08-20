package com.nd.hilauncherdev.kitset.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ScreenUtil {
    private static float currentDensity = 0.0F;
    public static int dip2px(Context context, float dipValue) {
        if(currentDensity > 0.0F) {
            return (int)(dipValue * currentDensity + 0.5F);
        } else {
            currentDensity = context.getResources().getDisplayMetrics().density;
            return (int)(dipValue * currentDensity + 0.5F);
        }
    }
    public static int getCurrentScreenWidth(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        boolean isLand = isOrientationLandscape(context);
        return isLand?metrics.heightPixels:metrics.widthPixels;
    }
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
    public static boolean isOrientationLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

}
