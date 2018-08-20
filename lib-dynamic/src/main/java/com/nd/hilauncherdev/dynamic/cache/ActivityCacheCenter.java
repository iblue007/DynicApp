package com.nd.hilauncherdev.dynamic.cache;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Map;

public class ActivityCacheCenter {

	/**
	 * Activity使用的缓存器,可缓存解包生成的Package信息，都以apk路径为key 非单例，各进程独立使用
	 */

	private static Map<String, PackageInfo> pluginPackageInfoCache = new HashMap<String, PackageInfo>();

	public void cachePackage(String pluginPath, PackageInfo pluginAppInfo) {
		pluginPackageInfoCache.put(pluginPath, pluginAppInfo);
	}

	public PackageInfo getPackageCache(String pluginPath) {
		return pluginPackageInfoCache.get(pluginPath);
	}

	public Map<String, PackageInfo> getPluginPackageInfoCache() {
		return pluginPackageInfoCache;
	}

	public void setPluginPackageInfoCache(Map<String, PackageInfo> pluginPackageInfoCache) {
		ActivityCacheCenter.pluginPackageInfoCache = pluginPackageInfoCache;
	}

	private PackageInfo findPackageInfoByDexPath(Context ctx, String path) {
		if (pluginPackageInfoCache.containsKey(path)) {
			return getPackageCache(path);
		}
		PackageInfo info = ctx.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		pluginPackageInfoCache.put(path, info);
		return info;
	}

	public int getDefinedTheme(Context ctx, String path, String pluginClassName) {
		int theme = com.android.internal.R.style.Theme_NoTitleBar;
		PackageInfo packageInfo = findPackageInfoByDexPath(ctx, path);
		if (null == packageInfo || packageInfo.activities == null || packageInfo.activities.length <= 0) {
			return theme;
		}
		try {
			theme = packageInfo.applicationInfo.theme;
			for (int i = 0; i < packageInfo.activities.length; i++) {
				ActivityInfo activity = packageInfo.activities[i];
				if (pluginClassName.equals(activity.name)) {
					int id = activity.theme;
					if (id > 0) {
						theme = id;
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theme;
	}
}
