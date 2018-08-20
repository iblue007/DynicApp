package com.nd.hilauncherdev.dynamic.clientparser;

import java.util.Map;

public class Client {

	/**
	 * Widget上下事件是否需要 true -需要,则桌面不拦截上下手势事件 false -不需要,则桌面拦截上下手势事件
	 */
	private boolean isPluginGestureIntercepted = false;
	/**
	 * 包名
	 */
	private String pluginPackageName;
	/**
	 * Widget版本号
	 */
	private int pluginVersion;
	/**
	 * Widget名称
	 */
	private String pluginWidgetName;
	/**
	 * 插件视图集合
	 */
	private Map<String, ClientWidgetView> pluginWidgetViews;
	/**
	 * 插件是否支持动态切换
	 */
	private boolean isPluginCanSwitch = false;

	public String getPluginPackageName() {
		return pluginPackageName;
	}

	public int getPluginVersion() {
		return pluginVersion;
	}

	public String getPluginWidgetName() {
		return pluginWidgetName;
	}

	public Map<String, ClientWidgetView> getPluginWidgetViews() {
		return pluginWidgetViews;
	}

	public void setPluginPackageName(String pluginPackageName) {
		this.pluginPackageName = pluginPackageName;
	}

	public void setPluginVersion(int pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public void setPluginWidgetName(String pluginWidgetName) {
		this.pluginWidgetName = pluginWidgetName;
	}

	public void setPluginWidgetViews(Map<String, ClientWidgetView> pluginWidgetViews) {
		this.pluginWidgetViews = pluginWidgetViews;
	}

	public boolean isPluginGestureIntercepted() {
		return isPluginGestureIntercepted;
	}

	public void setPluginGestureIntercepted(boolean isPluginGestureIntercepted) {
		this.isPluginGestureIntercepted = isPluginGestureIntercepted;
	}

	@Override
	public String toString() {
		return "Client [isPluginGestureIntercepted=" + isPluginGestureIntercepted + ", pluginPackageName=" + pluginPackageName + ", pluginVersion=" + pluginVersion + ", pluginWidgetName=" + pluginWidgetName + ", pluginWidgetViews=" + pluginWidgetViews + "]";
	}

/*	public boolean isPluginCanSwitch() {
		return isPluginCanSwitch;
	}*/

	public void setPluginCanSwitch(boolean isPluginCanSwitch) {
		this.isPluginCanSwitch = isPluginCanSwitch;
	}

}