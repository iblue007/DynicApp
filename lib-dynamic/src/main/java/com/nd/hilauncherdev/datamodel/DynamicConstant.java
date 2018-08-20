package com.nd.hilauncherdev.datamodel;

import android.os.Build;
import com.felink.corelib.config.BaseConfig;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DynamicConstant {
    /**
     * 动态插件会用到的一些配置信息，不同的宿主APP要做一些改变
     * */
    public static String PLUGIN_DIR;
    public static String WIFI_DOWNLOAD_PATH;
    public static String WIDGET_PLUGIN_DIR;
    public static String DOWNLOAD_DIR;
    //动态插件参数配置名
    public static String DYNAMIC_PARAM_NAME="DynamicPluginInfoForVp";
    public static String getBaseDir() {
        return BaseConfig.getBaseDir();
    }
    static {
        WIFI_DOWNLOAD_PATH = BaseConfig.WIFI_DOWNLOAD_PATH;
        DOWNLOAD_DIR = getBaseDir() + "/Downloads/";

        if (Build.VERSION.SDK_INT >= 26) {
            PLUGIN_DIR = BaseConfig.getApplicationDataPath() + "/plugin/";
        } else {
            PLUGIN_DIR = getBaseDir() + "/myphone/plugin/";
        }
        WIDGET_PLUGIN_DIR = getBaseDir() + "/myphone/widgets/";
    }
}
