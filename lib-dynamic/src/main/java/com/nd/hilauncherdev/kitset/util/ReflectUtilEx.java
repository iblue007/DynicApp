package com.nd.hilauncherdev.kitset.util;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.app.SearchManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.nd.hilauncherdev.dynamic.PluginLoaderActivityGroupBase;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * description: <br/>
 * author: dingdongjin_dian91<br/>
 * date: 2015/12/11<br/>
 */
public class ReflectUtilEx extends ReflectUtil {

    //没有使用
    public static void setFiledValue2LocalActivityManager(PluginLoaderActivityGroupBase pluginLoaderActivityGroup,
                                                          LocalActivityManager localActivityManager) {
        try {
            Field privateStringField = ActivityGroup.class
                    .getDeclaredField("mLocalActivityManager");
            privateStringField.setAccessible(true);
            privateStringField.set(pluginLoaderActivityGroup, localActivityManager);
            privateStringField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试通过
    public static int getDisplayMetricsStaticValue(String fieldName){
        try {
            Field privateStringField = DisplayMetrics.class.getDeclaredField(fieldName);
            privateStringField.setAccessible(true);
            return (Integer)privateStringField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //测试通过
    public static String getIntentStaticValue(String fieldName){
        try {
            Field privateStringField = Intent.class.getDeclaredField(fieldName);
            privateStringField.setAccessible(true);
            return (String)privateStringField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    //测试通过
    public static InputMethodManager getInputMethodManager() {
        try {
            Method method = InputMethodManager.class.getDeclaredMethod("peekInstance");
            return (InputMethodManager)method.invoke(null);
        } catch  (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //没有调用到
    public static ComponentName getGlobalSearchActivityFromSearchManager() {
        try {
            Method method = SearchManager.class.getDeclaredMethod("getGlobalSearchActivity");
            return (ComponentName)method.invoke(null);
        } catch  (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //已测试
    public static void invokeConnectivityManager_setMobileDataEnabled(ConnectivityManager conman, boolean isEnabled) {
        try {
            Method method = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            method.invoke(conman, isEnabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //已测试
    public static void invokeService_setForeground(Service service, Boolean value) {
        try {
            Method method = Service.class.getDeclaredMethod("setForeground", Boolean.TYPE);
            method.invoke(service, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //已测试
    public static AssetManager invokeNewAssetManager() {
        try {
            return AssetManager.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //已测试
    public static void invokeAssetManager_addAssetPath(AssetManager assetManager, String path) {
        try {
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(assetManager, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //没有调用到
    public static File invokeContext_getSharedPrefsFile(Context context, String name) {
        try {
            Method method = Context.class.getDeclaredMethod("getSharedPrefsFile", String.class);
            return (File)method.invoke(context, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
