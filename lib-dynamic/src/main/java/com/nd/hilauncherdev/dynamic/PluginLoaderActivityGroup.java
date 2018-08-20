package com.nd.hilauncherdev.dynamic;

import android.app.Activity;
import android.app.Application;
import android.app.LocalActivityManager;
import android.app.PluginLocalActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nd.hilauncherdev.dynamic.cache.ActivityCacheCenter;
import com.nd.hilauncherdev.dynamic.util.ManagerUtil;
import com.nd.hilauncherdev.dynamic.util.PluginConstant;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.kitset.util.ReflectUtilEx;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class PluginLoaderActivityGroup extends PluginLoaderActivityGroupBase {
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

				Method methodSetActivityManager = activityClass.getMethod("setActivityManager", new Class[] { LocalActivityManager.class });
				methodSetActivityManager.setAccessible(true);
				//methodSetActivityManager.invoke(activity, new Object[] { new PluginLocalActivityManager(this, true, mDexpath, mPluginPackageName) });
				PluginLocalActivityManager pluginLocalActivityManager = new PluginLocalActivityManager(this, false, mDexpath, mPluginPackageName);
				methodSetActivityManager.invoke(activity, new Object[] { pluginLocalActivityManager });
				//this.mLocalActivityManager = pluginLocalActivityManager;
				//使用反射替代依赖值
				ReflectUtilEx.setFiledValue2LocalActivityManager(this, pluginLocalActivityManager);
				((Activity) mTaskActivity.getCurrentActivity()).setIntent(intent);
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

	@Override
	public boolean stopPluginService(Intent intent) {
		intent.putExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false);
		return stopPluginLoaderService(this, mDexpath, mPluginPackageName, intent);
	}

}
