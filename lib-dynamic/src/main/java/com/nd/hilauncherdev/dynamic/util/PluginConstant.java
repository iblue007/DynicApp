package com.nd.hilauncherdev.dynamic.util;

public class PluginConstant {
	/**
	 * sdk中传递过来的插件包名
	 */
	public static final String KEY_PLUGIN_PKG_NAME = "plugin_pkg_name";
	/**
	 * intent传递数据的key--包名
	 */
	public static final String KEY_PKGNAME = "pkgname";
	/**
	 * intent传递数据的key--类名
	 */
	public static final String KEY_CLASSNAME = "classname";
	/**
	 * intent传递数据的key--lib路径
	 */
	public static final String KEY_LIB_PATH = "libPath";
	/**
	 * intent传递数据的key--dex优化文件存放路径
	 */
	public static final String KEY_DEXOPTPATH = "dexoptpath";
	/**
	 * intent传递数据的key--dex文件存放路径
	 */
	public static final String KEY_DEXPATH = "dexpath";
	/**
	 * intent传递数据的key--关闭服务的模式
	 */
	public static final String KEY_STOP_MODE = "stopservice";
	/**
	 * intent传递数据的key--指定开启插件中某个组件的类名
	 */
	public final static String EXTRA_MAIN_CLASS_NAME = "pluginLoaderActivity.MainClassName";
	/**
	 * 标识是否隐式intent
	 */
	public static final String FLAG_IS_EXPLICIT_INTENT = "is_explicit_intent";
}
