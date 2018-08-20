package com.nd.hilauncherdev.dynamic.cache;

import android.content.pm.PackageParser;
import android.content.pm.PackageParser.Package;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.nd.hilauncherdev.dynamic.CustomDexClassLoader;
import com.nd.hilauncherdev.dynamic.bean.PluginAppInfo;
import com.nd.hilauncherdev.launcher.LauncherApplicationLike;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class ContextCacheCenter {

	/**
	 * Context使用的缓存器,可缓存classloader、apk解包生成的Package信息，都以apk路径为key 非单例，各进程独立使用
	 */

	private final Map<String, DexClassLoader> classLoaderCache = new HashMap<String, DexClassLoader>();
	private final Map<String, PluginAppInfo> pluginPackageCache = new HashMap<String, PluginAppInfo>();

	public boolean isPluginAppInfoExist(String dexpath) {
		return null != pluginPackageCache.get(dexpath);
	}

	public boolean isPluginClassLoaderExist(String dexpath){
		return null != classLoaderCache.get(dexpath);
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
	public DexClassLoader getApkClassLoader(String dexpath, String dexoutputpath, String libpath, ClassLoader classLoader) {
		DexClassLoader dexClassLoader = getApkClassLoaderFromCache(dexpath);
		if (null == dexClassLoader) {
			//dexClassLoader = new DexClassLoader(dexpath, dexoutputpath, libpath, classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader);
			dexClassLoader = new CustomDexClassLoader(dexpath, dexoutputpath, libpath, classLoader == null ? LauncherApplicationLike.class.getClassLoader() : classLoader);
			addNewClassLoaderToCache(dexpath, dexClassLoader);
			Log.e("======","======dexpath:"+dexpath+"---dexoutputpath:"+dexoutputpath+"--libpath:"+libpath);
//			FileUtil.copyFolder("/data/data/com.xxm.dynicapp/", BaseConfig.WIFI_DOWNLOAD_PATH+"test");
//			FileUtil.copy("/data/data/com.xxm.dynicapp/", BaseConfig.WIFI_DOWNLOAD_PATH+"test");
		}
		return dexClassLoader;
	}

	public PluginAppInfo getPluginAppInfo(String dexpath) {
		PluginAppInfo plugin = pluginPackageCache.get(dexpath);
		if (null == plugin) {
			File scanFile = new File(dexpath);
			int sdk = Build.VERSION.SDK_INT;
			if (sdk > 20) {
				try {
					Class clazz = ClassLoader.getSystemClassLoader().loadClass("android.content.pm.PackageParser");
					Constructor c = clazz.getDeclaredConstructor(new Class[] {});
					Object obj = c.newInstance(new Object[] {});
					Method m = clazz.getDeclaredMethod("parsePackage", new Class[] { File.class, Integer.TYPE });
					PackageParser.Package pkg = (Package) m.invoke(obj, new Object[] { scanFile, PackageParser.PARSE_IS_SYSTEM | PackageParser.PARSE_IS_SYSTEM_DIR });
					plugin = new PluginAppInfo(dexpath, pkg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				PackageParser pp = new PackageParser(dexpath);
				String scanPath = scanFile.getPath();
				DisplayMetrics metrics = new DisplayMetrics();
				metrics.setToDefaults();
				PackageParser.Package pkg = pp.parsePackage(scanFile, scanPath, metrics, PackageParser.PARSE_IS_SYSTEM | PackageParser.PARSE_IS_SYSTEM_DIR);
				plugin = new PluginAppInfo(dexpath, pkg);
			}
			pluginPackageCache.put(dexpath, plugin);
		}
		return plugin;
	}

	/**
	 * 插件更新的时候,更新缓存
	 * 
	 * @Title: updatePlugin
	 * @author lytjackson@gmail.com
	 * @date 2014-1-9
	 * @param dexpath
	 */
	public void updatePluginCache(String dexpath) {
		if (classLoaderCache.containsKey(dexpath)) {
			classLoaderCache.remove(dexpath);
		}
		if (pluginPackageCache.containsKey(dexpath)) {
			pluginPackageCache.remove(dexpath);
		}
	}

	public void cacheClassLoader(String pluginPath, DexClassLoader pluginDexClassLoader) {
		classLoaderCache.put(pluginPath, pluginDexClassLoader);
	}

/*	public void cachePackage(String pluginPath, PluginAppInfo pluginAppInfo) {
		pluginPackageCache.put(pluginPath, pluginAppInfo);
	}*/

/*	public DexClassLoader getClassLoaderCache(String pluginPath) {
		return classLoaderCache.get(pluginPath);
	}

	public PluginAppInfo getPackageCache(String pluginPath) {
		return pluginPackageCache.get(pluginPath);
	}*/

	private void addNewClassLoaderToCache(String dexpath, DexClassLoader dexClassLoader) {
		if (!classLoaderCache.containsKey(dexpath)) {
			classLoaderCache.put(dexpath, dexClassLoader);
		}
	}

	private DexClassLoader getApkClassLoaderFromCache(String dexpath) {
		DexClassLoader dexClassLoader = null;
		if (classLoaderCache.containsKey(dexpath)) {
			dexClassLoader = classLoaderCache.get(dexpath);
		}
		return dexClassLoader;
	}
}
