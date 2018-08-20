package com.nd.hilauncherdev.kitset.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

/**
 * Created by Administrator on 2017/10/16.
 */

public class TelephoneUtil {
    public static int getApiLevel() {
        int apiLevel = 7;

        try {
            apiLevel = Integer.parseInt(Build.VERSION.SDK);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return apiLevel;
    }
    public static boolean isSdcardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }
    public static int getVersionCode(Context ctx) {
        return getVersionCode(ctx, ctx.getPackageName());
    }

    public static int getVersionCode(Context ctx, String packageName) {
        int versionCode = 0;

        try {
            PackageInfo e = ctx.getPackageManager().getPackageInfo(packageName, 16);
            versionCode = e.versionCode;
        } catch (Exception var4) {
            Log.e("TelephoneUtil", var4.toString());
        }

        return versionCode;
    }
}
