package com.nd.hilauncherdev.dynamic.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.nd.hilauncherdev.dynamic.PluginContext;
import com.nd.hilauncherdev.dynamic.PluginLoaderActivity;
import com.nd.hilauncherdev.dynamic.PluginLoaderActivityForDialog;
import com.nd.hilauncherdev.dynamic.PluginLoaderActivityGroup;
import com.nd.hilauncherdev.dynamic.PluginLoaderListActivity;
import com.nd.hilauncherdev.dynamic.PluginLoaderTabActivity;
import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.dynamic.appstore.PluginLoaderActivityForShopProcessForAppStore;
import com.nd.hilauncherdev.dynamic.appstore.PluginLoaderActivityGroupForShopProcessForAppStore;
import com.nd.hilauncherdev.dynamic.appstore.PluginLoaderListActivityForShopProcessForAppStore;
import com.nd.hilauncherdev.dynamic.appstore.PluginLoaderTabActivityForShopProcessForAppStore;
import com.nd.hilauncherdev.dynamic.bean.ActivityProcess;
import com.nd.hilauncherdev.dynamic.other.PluginContextForShopProcess;
import com.nd.hilauncherdev.dynamic.other.PluginLoaderActivityForShopProcess;
import com.nd.hilauncherdev.dynamic.other.PluginLoaderActivityGroupForShopProcess;
import com.nd.hilauncherdev.dynamic.other.PluginLoaderListActivityForShopProcess;
import com.nd.hilauncherdev.dynamic.other.PluginLoaderTabActivityForShopProcess;
import com.nd.hilauncherdev.kitset.util.MessageUtils;

public class ManagerUtil {
	private static final String ActivitySupport = "android.app.Activity";
	private static final String ListActivitySupport = "android.app.ListActivity";
	private static final String TabActivitySupport = "android.app.TabActivity";
	private static final String ActivityGroupSupport = "android.app.ActivityGroup";

	public static void startPluginLoaderActivityWithConfirmClass(Context ctx, String path, String pluginPackageName, Intent intent, int requestCode, boolean forResult) {
		String className = PluginLoaderUtil.getStartClassName(path, intent);
		if ("".contains(className)) {
//			MessageUtils.makeLongToast(ctx, R.string.dyanmic_plugin_err_activity_not_found);
			return;
		}
		ActivityProcess activityProcess = getCurrentProcess(ctx);
		Intent newIntent;
		try {
			newIntent = assembleIntent(ctx, path, pluginPackageName, className, intent, activityProcess);
			if (forResult) {
				((Activity) ctx).startActivityForResult(newIntent, requestCode);
			} else {
				ctx.startActivity(newIntent);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MessageUtils.makeLongToast(ctx, R.string.dyanmic_plugin_err_coding_wrong);
			((Activity) ctx).finish();
		}
	}

	/**
	 * 为找到进程对应的loaderActivity类,组装传递intent
	 * 
	 * @Title: assembleIntent
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @param ctx
	 * @param path
	 * @param pluginPackageName
	 * @param className
	 * @param intent
	 * @param activityProcess
	 *            插件运行所在进程
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static Intent assembleIntent(Context ctx, String path, String pluginPackageName, String className, Intent intent, ActivityProcess activityProcess) throws ClassNotFoundException {
		Intent newIntent = null;
		Class<?> clazz = null;
		//TODO:xuqunxing 替换占位ativity
		if (activityProcess == ActivityProcess.PROCESS_LAUNCHER) {
			clazz = PluginContext.getApkClassLoader(path, PluginLoaderUtil.getPluginDexOptPath(ctx, pluginPackageName), PluginLoaderUtil.getPluginLibPath(ctx, pluginPackageName), null).loadClass(className);
		} else if (activityProcess == ActivityProcess.PROCESS_SHOP) {
			clazz = PluginContextForShopProcess.getApkClassLoader(path, PluginLoaderUtil.getPluginDexOptPath(ctx, pluginPackageName), PluginLoaderUtil.getPluginLibPath(ctx, pluginPackageName), null).loadClass(className);
		}

		Class<?> startClass = getPluginClazz(ctx, clazz, activityProcess,pluginPackageName);
		if (null != intent && intent.getBooleanExtra("is_dialog", false)) {
			startClass = PluginLoaderActivityForDialog.class;
		}
		if (intent == null) {
			newIntent = new Intent(ctx, startClass);
		} else {
			newIntent = (Intent) intent.clone();
			newIntent.setClass(ctx, startClass);
		}
		newIntent.putExtra(PluginConstant.KEY_DEXPATH, path);
		newIntent.putExtra(PluginConstant.KEY_DEXOPTPATH, PluginLoaderUtil.getPluginDexOptPath(ctx, pluginPackageName));
		newIntent.putExtra(PluginConstant.KEY_LIB_PATH, PluginLoaderUtil.getPluginLibPath(ctx, pluginPackageName));
		newIntent.putExtra(PluginConstant.KEY_CLASSNAME, className);
		return newIntent;
	}

	public static ComponentName startPluginLoaderServiceWithConfirmClass(Class<?> startClass, Context ctx, String pluginApkPath, String pluginPackageName, Intent intent) {
		String className = PluginLoaderUtil.getStartClassName(pluginApkPath, intent);
		if ("".contains(className)) {
//			MessageUtils.makeLongToast(ctx, R.string.dyanmic_plugin_err_service_not_found);
			return null;
		}
		Intent newIntent = new Intent(ctx, startClass);
		newIntent.putExtra(PluginConstant.KEY_DEXPATH, pluginApkPath);
		newIntent.putExtra(PluginConstant.KEY_DEXOPTPATH, PluginLoaderUtil.getPluginDexOptPath(ctx, pluginPackageName));
		newIntent.putExtra(PluginConstant.KEY_LIB_PATH, PluginLoaderUtil.getPluginLibPath(ctx, pluginPackageName));
		newIntent.putExtra(PluginConstant.KEY_PKGNAME, pluginPackageName);
		newIntent.putExtra(PluginConstant.KEY_CLASSNAME, className);
		if (intent != null) {
			newIntent.putExtras(intent);
		}
		return ctx.startService(newIntent);
	}

	public static boolean stopPluginLoaderServiceWithConfirmClass(Class<?> startClass, Context ctx, String dexpath, String pluginPackageName, Intent intent) {
		String className = PluginLoaderUtil.getStartClassName(dexpath, intent);
		if ("".contains(className)) {
//			MessageUtils.makeLongToast(ctx, R.string.dyanmic_plugin_err_service_not_found);
			return false;
		}
		Intent newIntent = new Intent(ctx, startClass);
		newIntent.putExtra(PluginConstant.KEY_DEXPATH, dexpath);
		newIntent.putExtra(PluginConstant.KEY_DEXOPTPATH, PluginLoaderUtil.getPluginDexOptPath(ctx, pluginPackageName));
		newIntent.putExtra(PluginConstant.KEY_LIB_PATH, PluginLoaderUtil.getPluginLibPath(ctx, pluginPackageName));
		newIntent.putExtra(PluginConstant.KEY_PKGNAME, pluginPackageName);
		newIntent.putExtra(PluginConstant.KEY_CLASSNAME, className);
		newIntent.putExtra(PluginConstant.KEY_STOP_MODE, true);
		ctx.startService(newIntent);
		return true;
	}

	/**
	 * 找到开启的activity的真实类对象,返回相应的loader类对象
	 * 
	 * @Title: getPluginClazz
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @param ctx
	 * @param clazz
	 * @return
	 */
	public static Class<?> getPluginClazz(Context ctx, Class<?> clazz, ActivityProcess activityProcess,String pkgName) {
		if (null == clazz) {
			return PluginLoaderActivity.class;
		}
		boolean isAppStore = false;
		if(pkgName != null && pkgName.equals("com.wireless.assistant.mobile.market"))
		    isAppStore = true;
		Class<?> realClazz = PluginLoaderActivity.class;
		boolean bingo = false;
		while ((clazz = clazz.getSuperclass()) != null) {
			if (ListActivitySupport.equals(clazz.getName())) {
				if (isAppStore) {
					realClazz = PluginLoaderListActivityForShopProcessForAppStore.class;
				}else {
					if (activityProcess == ActivityProcess.PROCESS_SHOP) {
						realClazz = PluginLoaderListActivityForShopProcess.class;
					} else {
						realClazz = PluginLoaderListActivity.class;
					}
				}
				bingo = true;
			} else if (TabActivitySupport.equals(clazz.getName())) {
				if (isAppStore){
					realClazz = PluginLoaderTabActivityForShopProcessForAppStore.class;
				}else {
					if (activityProcess == ActivityProcess.PROCESS_SHOP) {
						realClazz = PluginLoaderTabActivityForShopProcess.class;
					} else {
						realClazz = PluginLoaderTabActivity.class;
					}
				}
				bingo = true;
			} else if (ActivityGroupSupport.equals(clazz.getName())) {
				if (isAppStore){
					realClazz = PluginLoaderActivityGroupForShopProcessForAppStore.class;
				}else {
					if (activityProcess == ActivityProcess.PROCESS_SHOP) {
						realClazz = PluginLoaderActivityGroupForShopProcess.class;
					} else {
						realClazz = PluginLoaderActivityGroup.class;
					}
				}
				bingo = true;
			} else if (ActivitySupport.equals(clazz.getName())) {
				if (isAppStore){
					realClazz = PluginLoaderActivityForShopProcessForAppStore.class;
				    bingo = true;
				}
				break;
			}
			if (bingo) {
				break;
			}
		}
		if (bingo) {
			return realClazz;
		}
		if (activityProcess == ActivityProcess.PROCESS_SHOP) {
			realClazz = PluginLoaderActivityForShopProcess.class;
		} else {
			realClazz = PluginLoaderActivity.class;
		}
		return realClazz;
	}

	/**
	 * 获取调用者所在进程
	 * 
	 * @Title: getCurrentProcess
	 * @author lytjackson@gmail.com
	 * @date 2014-2-26
	 * @param context
	 * @return
	 */
	public static ActivityProcess getCurrentProcess(Context context) {
		ActivityProcess activityProcess = ActivityProcess.PROCESS_LAUNCHER;
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if(null == mActivityManager.getRunningAppProcesses()) return activityProcess;
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				activityProcess = ActivityProcess.toEnum(appProcess.processName);
				break;
			}
		}
		return activityProcess;
	}

}
