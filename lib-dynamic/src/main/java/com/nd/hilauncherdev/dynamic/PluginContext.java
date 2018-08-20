package com.nd.hilauncherdev.dynamic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.nd.hilauncherdev.dynamic.bean.PluginAppInfo;
import com.nd.hilauncherdev.dynamic.cache.ContextCacheCenter;

import dalvik.system.DexClassLoader;

public class PluginContext extends PluginContextBase {
	/**
	 * 一个进程使用一个缓存器,此类为myphone进程使用的context
	 */
	private static final ContextCacheCenter mCacheCenter = new ContextCacheCenter();

	/**
	 * 
	 * @param base
	 *            父Context
	 * @param themeres
	 *            样式-0
	 * @param pluginPath
	 *            插件apk路径
	 * @param pluginPackageName
	 *            插件包名
	 * @param pluginDexClassLoader
	 *            自定义的DexClassLoader
	 * @throws PluginSrcNotFoundException
	 */
	public PluginContext(Context base, int themeres, String pluginPath, String pluginPackageName, DexClassLoader pluginDexClassLoader) throws PluginSrcNotFoundException {
		super(base, themeres, pluginPath, pluginPackageName, pluginDexClassLoader);
		mCacheCenter.cacheClassLoader(pluginPath, pluginDexClassLoader);
	}

	/**
	 * 获取apk对应的classloader
	 * 
	 * @Title: getApkClassLoader
	 * @author lytjackson@gmail.com
	 * @date 2014-1-9
	 * @param dexpath
	 * @param dexoutputpath
	 * @param libpath
	 * @param classLoader
	 * @return
	 */
	public static DexClassLoader getApkClassLoader(String dexpath, String dexoutputpath, String libpath, ClassLoader classLoader) {
		return mCacheCenter.getApkClassLoader(dexpath, dexoutputpath, libpath, classLoader);
	}

	public static PluginAppInfo getPluginAppInfo(String dexpath) {
		return mCacheCenter.getPluginAppInfo(dexpath);
	}
	
/*	public static boolean isPluginAppInfoExist(String dexpath) {
		return mCacheCenter.isPluginAppInfoExist(dexpath);
	}*/

	public static boolean isPluginDexClassLoaderExist(String dexpath){
		return mCacheCenter.isPluginClassLoaderExist(dexpath);
	}

	/**
	 * 插件更新的时候,更新缓存
	 * 
	 * @Title: updatePlugin
	 * @author lytjackson@gmail.com
	 * @date 2014-1-9
	 * @param dexpath
	 */
	public static void updatePluginCache(String dexpath) {
		mCacheCenter.updatePluginCache(dexpath);
	}

	public void startPluginActivity(Intent intent, int requestCode, boolean forResult) {
		PluginLoaderActivity.startPluginLoaderActivity(mContext, mDexpath, mPluginPackageName, intent, -1, false);
	}

	public ComponentName startPluginService(Intent intent) {
		return PluginLoaderActivity.startPluginLoaderService(mContext, mDexpath, mPluginPackageName, intent);
	}

	public boolean stopPluginService(Intent intent) {
		return PluginLoaderActivity.stopPluginLoaderService(mContext, mDexpath, mPluginPackageName, intent);
	}

}
