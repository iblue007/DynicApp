package com.felink.corelib.kitset;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * Created by linliangbin on 2017/7/10 18:27.
 */

public class OpUtil {


    /**
     * 检测 miui 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        } else {
            return true;
        }
    }

    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int result = (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
                return AppOpsManager.MODE_ERRORED != result && AppOpsManager.MODE_IGNORED != result;
            } catch (Throwable t) {
            }
        } else {
        }
        return false;
    }
}
