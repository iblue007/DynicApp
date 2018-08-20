package com.nd.hilauncherdev.launcher;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/16.
 */

public class LauncherApplicationLike {
    private static LauncherApplicationLike instance;

    public synchronized static LauncherApplicationLike getInstance() {
        if (instance == null) {
            instance = new LauncherApplicationLike();
        }
        return instance;
    }
    private Map<String, Application> mPluginApplicationCache = new HashMap();
    public Application getPluginApplication(String dexPath) {
        return (Application)this.mPluginApplicationCache.get(dexPath);
    }

    public void setPluginApplication(String dexPath, Application pluginApplication) {
        this.mPluginApplicationCache.put(dexPath, pluginApplication);
    }
}
