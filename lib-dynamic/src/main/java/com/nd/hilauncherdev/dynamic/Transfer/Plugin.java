package com.nd.hilauncherdev.dynamic.Transfer;

public class Plugin {
	/**
	 * 包名
	 */
	public String pkg;
	/**
	 * 本地文件名
	 */
	public String file;
	/**
	 * 版本号
	 */
	public int version;
	/**
	 * 插件类型--0为widget,1为插件 {@link}WidgetType
	 */
	public int type;
	/**
	 * 是否内置插件
	 */
	public boolean isNative;

	@Override
	public String toString() {
		return "Plugin [pkg=" + pkg + ", file=" + file + ", version=" + version + ", type=" + type + ", isNative=" + isNative + "]";
	}

}