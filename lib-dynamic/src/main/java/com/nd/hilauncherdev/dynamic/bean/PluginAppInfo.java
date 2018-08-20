package com.nd.hilauncherdev.dynamic.bean;

import android.content.IntentFilter;
import android.content.pm.PackageParser;

import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;

import java.util.HashMap;
import java.util.Map;

public class PluginAppInfo {
	/**
	 * apk路径
	 */
	private String path;
	/**
	 * apk版本号
	 */
	private int version;
	/**
	 * apk组件的隐式匹配数据
	 */
	private Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> intentFilter;

	private PackageParser.Package pkg;

	/**
	 * dex加载器
	 */
	// private DexClassLoader dexClassLoader;

	public PluginAppInfo(String path, PackageParser.Package pkg) {
		this.path = path;
		this.pkg = pkg;
		intentFilter = new HashMap<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>>();
		if (null != pkg) {
			this.version = pkg.mVersionCode;
			PluginLoaderUtil.parseComponentIntentFilterInfo(intentFilter, pkg.activities);
			PluginLoaderUtil.parseComponentIntentFilterInfo(intentFilter, pkg.services);
		}
		// this.dexClassLoader = dexClassLoader;
	}

//	public String getPath() {
//		return path;
//	}

//	public void setPath(String path) {
//		this.path = path;
//	}

	public Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> getIntentFilter() {
		return intentFilter;
	}

/*	public void setIntentFilter(Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> intentFilter) {
		this.intentFilter = intentFilter;
	}*/

	// public DexClassLoader getDexClassLoader() {
	// return dexClassLoader;
	// }
	//
	// public void setDexClassLoader(DexClassLoader dexClassLoader) {
	// this.dexClassLoader = dexClassLoader;
	// }

//	public int getVersion() {
//		return version;
//	}

//	public void setVersion(int version) {
//		this.version = version;
//	}

	public PackageParser.Package getPkg() {
		return pkg;
	}

//	public void setPkg(PackageParser.Package pkg) {
//		this.pkg = pkg;
//	}
}
