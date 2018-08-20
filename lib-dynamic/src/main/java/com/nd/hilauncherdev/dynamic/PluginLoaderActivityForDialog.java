package com.nd.hilauncherdev.dynamic;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nd.hilauncherdev.dynamic.cache.ActivityCacheCenter;
import com.nd.hilauncherdev.dynamic.util.ManagerUtil;
import com.nd.hilauncherdev.dynamic.util.PluginConstant;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;

import java.lang.reflect.Constructor;

public class PluginLoaderActivityForDialog extends PluginLoaderActivityBase {
	private static ActivityCacheCenter mActivityCacheCenter = new ActivityCacheCenter();

	public static void startPluginLoaderActivity(Context ctx, String path, String pluginPackageName, Intent intent, int requestCode, boolean forResult) {
		ManagerUtil.startPluginLoaderActivityWithConfirmClass(ctx, path, pluginPackageName, intent, requestCode, forResult);
	}

	public static ComponentName startPluginLoaderService(Context ctx, String pluginApkPath, String pluginPackageName, Intent intent) {
		return ManagerUtil.startPluginLoaderServiceWithConfirmClass(PluginLoaderService.class, ctx, pluginApkPath, pluginPackageName, intent);
	}

	public static boolean stopPluginLoaderService(Context ctx, String dexpath, String pluginPackageName, Intent name) {
		return ManagerUtil.stopPluginLoaderServiceWithConfirmClass(PluginLoaderService.class, ctx, dexpath, pluginPackageName, name);
	}

	public void startPlugin(String appClassName, String activityClassName, Bundle savedInstanceState, Intent intent) {
		try {
			Object application = null;
			PluginContext pluginContext = new PluginContext(getApplicationContext(), 0, mDexpath, mPluginPackageName, mDexClassLoader);
			if (null == mApplication.getPluginApplication(mDexpath) && null != appClassName && !"".equals(appClassName)) {
				application = mLoadInvoker.invokeApplication(mDexClassLoader, appClassName, pluginContext);
				mApplication.setPluginApplication(mDexpath, (Application) application);
			}
			if (null != activityClassName && !"".equals(activityClassName)) {
				Class<?> activityClass = mDexClassLoader.loadClass(activityClassName);
				Constructor<?> activityConstructor = activityClass.getConstructor(new Class[] {});
				Object activity = activityConstructor.newInstance(new Object[] {});
				mTaskActivity.setCurrentClass(activityClass);
				mTaskActivity.setCurrentActivity(activity);

				mLoadInvoker.invokeActivity(this, activityClass, activity, application, pluginContext, mPluginPackageName);

				((Activity) activity).setIntent(intent);
				mLoadInvoker.invokeClientOnCreate(mTaskActivity, null);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			PluginLoaderUtil.showPluginError(this, mDexpath);
		} catch (final PluginSrcNotFoundException e) {
			e.printStackTrace();
			ThreadUtil.executeMore(new Runnable() {
				@Override
				public void run() {
					PluginLoaderUtil.repairPlugin(mContext, e.getDexpath(), e.getPackageName());
				}
			});
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getDefinedTheme() {
		return mActivityCacheCenter.getDefinedTheme(mContext, mDexpath, mPluginClassName);
	}

	public void startPluginActivity(Intent intent, int requestCode, boolean forResult) {
		intent.putExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false);
		startPluginLoaderActivity(this, mDexpath, mPluginPackageName, intent, requestCode, forResult);
	}

	public ComponentName startPluginService(Intent intent) {
		intent.putExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false);
		return startPluginLoaderService(this, mDexpath, mPluginPackageName, intent);
	}

	public boolean stopPluginService(Intent intent) {
		intent.putExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false);
		return stopPluginLoaderService(this, mDexpath, mPluginPackageName, intent);
	}

}