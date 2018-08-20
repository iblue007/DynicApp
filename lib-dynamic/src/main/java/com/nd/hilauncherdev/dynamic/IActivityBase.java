package com.nd.hilauncherdev.dynamic;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public interface IActivityBase {
	/**
	 * 获取插件的组件主题
	 * 
	 * @Title: getDefinedTheme
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @return
	 */
	public abstract int getDefinedTheme();

	/**
	 * 开启插件Activity
	 * 
	 * @Title: startPluginActivity
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @param intent
	 * @param requestCode
	 * @param b
	 */
	public abstract void startPluginActivity(Intent intent, int requestCode, boolean b);

	/**
	 * 开启插件Service
	 * 
	 * @Title: startPluginService
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @param intent
	 * @return
	 */
	public abstract ComponentName startPluginService(Intent intent);

	/**
	 * 关闭插件Service
	 * 
	 * @Title: stopPluginService
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @param name
	 * @return
	 */
	public abstract boolean stopPluginService(Intent name);

	/**
	 * 开启插件,调用各个需要的生命周期函数以及调用借口传递数据
	 * 
	 * @Title: startPlugin
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @param appClassName
	 * @param activityClassName
	 * @param savedInstanceState
	 * @param intent
	 */
	public abstract void startPlugin(String appClassName, String activityClassName, Bundle savedInstanceState, Intent intent);
}
