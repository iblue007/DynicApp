package com.nd.hilauncherdev.dynamic;

import android.content.ComponentName;
import android.content.Intent;

public interface IContextBase {

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

}
