package com.nd.hilauncherdev.dynamic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.nd.hilauncherdev.dynamic.bean.TaskService;
import com.nd.hilauncherdev.dynamic.util.PluginConstant;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class PluginLoaderService extends Service {

	private DexClassLoader mDexClassLoader;
	private String mDexpath;
	private String mDexoutputpath;
	private String mLibpath;
	/**
	 * 服务缓存,key值为dex路径+包名+类名
	 */
	private Map<String, TaskService> serviceCache = new HashMap<String, TaskService>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// Runnable clientSrviceRunable = new Runnable() {
	// @Override
	// public void run() {
	// launcherPluginService(mIntent);
	// }
	// };

	@Override
	public void onCreate() {
		super.onCreate();
		if (null != serviceCache) {
			serviceCache.clear();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 加载client的服务
		if (null != intent) {
			init(intent);
			boolean isStop = intent.getBooleanExtra(PluginConstant.KEY_STOP_MODE, false);
			if (isStop) {
				pluginServiceDestroy(intent);
			} else {
				launcherPluginService(intent);
			}
		}
		return START_STICKY;
	}

	public void onDestroy() {
		super.onDestroy();
		if (null != serviceCache) {
			serviceCache.clear();
		}
	};

	@Override
	public boolean stopService(Intent name) {
		if (null != name) {
			pluginServiceDestroy(name);
		}
		return false;
	}

	private void pluginServiceDestroy(Intent intent) {
		String key = getKeyFromIntent(intent);
		if (null != serviceCache && serviceCache.containsKey(key)) {
			((Service) serviceCache.get(key).getCurrentService()).onDestroy();
			serviceCache.remove(key);
		}
	}

	private void init(Intent intent) {
		mDexpath = intent.getStringExtra(PluginConstant.KEY_DEXPATH);
		mDexoutputpath = intent.getStringExtra(PluginConstant.KEY_DEXOPTPATH);
		mLibpath = intent.getStringExtra(PluginConstant.KEY_LIB_PATH);
		mDexClassLoader = PluginContext.getApkClassLoader(mDexpath, mDexoutputpath, mLibpath, null);
	}

	private void launcherPluginService(Intent intent) {
		if (null == intent) {
			return;
		}
		String dexKey = intent.getStringExtra(PluginConstant.KEY_DEXPATH);
		String pkgName = intent.getStringExtra(PluginConstant.KEY_PKGNAME);
		String className = intent.getStringExtra(PluginConstant.KEY_CLASSNAME);
		try {
			if ((null != className) && (!"".equals(className))) {
				Object service = null;
				Class<?> localClass = null;
				String key = dexKey + pkgName + className;
				if (null != serviceCache && serviceCache.containsKey(key)) {
					service = serviceCache.get(key).getCurrentService();
					localClass = serviceCache.get(key).getCurrentClass();
				} else {
					localClass = mDexClassLoader.loadClass(className);
					Constructor<?> constructor = localClass.getConstructor(new Class[] {});
					service = constructor.newInstance(new Object[] {});

					TaskService taskService = new TaskService();
					taskService.setCurrentClass(localClass);
					taskService.setCurrentService(service);
					serviceCache.put(key, taskService);

					Method methodInit = localClass.getMethod("initContext", new Class[] { Context.class });
					methodInit.setAccessible(true);
					methodInit.invoke(service, new Object[] { new PluginContext(getApplicationContext(), 0, mDexpath, pkgName, mDexClassLoader) });

					Method methodSetContext = localClass.getMethod("setParentContext", new Class[] { Context.class });
					methodSetContext.setAccessible(true);
					methodSetContext.invoke(service, new Object[] { this });

					Method methodonCreate = localClass.getMethod("onCreate", new Class[] {});
					methodonCreate.setAccessible(true);
					methodonCreate.invoke(service, new Object[] {});
				}
				Method methodonStartCommand = localClass.getMethod("onStartCommand", new Class[] { Intent.class, Integer.TYPE, Integer.TYPE });
				methodonStartCommand.setAccessible(true);
				methodonStartCommand.invoke(service, new Object[] { intent, 1, 1 });
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getKeyFromIntent(Intent intent) {
		if (null == intent) {
			return "";
		}
		String dexKey = intent.getStringExtra(PluginConstant.KEY_DEXPATH);
		String pkgName = intent.getStringExtra(PluginConstant.KEY_PKGNAME);
		String className = intent.getStringExtra(PluginConstant.KEY_CLASSNAME);
		return dexKey + pkgName + className;
	}
}
