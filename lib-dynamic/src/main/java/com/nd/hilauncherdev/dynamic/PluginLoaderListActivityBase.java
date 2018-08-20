package com.nd.hilauncherdev.dynamic;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageParser;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.view.KeyEvent;

import com.nd.hilauncherdev.dynamic.bean.TaskActivity;
import com.nd.hilauncherdev.dynamic.util.LoadInvoker;
import com.nd.hilauncherdev.dynamic.util.PluginConstant;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.launcher.LauncherApplicationLike;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public abstract class PluginLoaderListActivityBase extends ListActivity implements IActivityBase{
	protected Context mContext;
	protected LauncherApplicationLike mApplication;
	protected DexClassLoader mDexClassLoader;
	protected LoadInvoker mLoadInvoker;
	protected TaskActivity mTaskActivity = new TaskActivity();
	protected String mDexpath;
	private String mDexoutputpath;
	private String mLibpath;
	private String mPluginApplicatonClassName;
	protected String mPluginPackageName;
	protected String mPluginClassName;
	private int themeID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mLoadInvoker = new LoadInvoker();
		Intent intent = getIntent();
		if (null != intent) {
			init(intent);
		}

		themeID = getDefinedTheme();
		this.setTheme(themeID);
		mApplication = (LauncherApplicationLike) LauncherApplicationLike.getInstance();
		startPlugin(mPluginApplicatonClassName, mPluginClassName, savedInstanceState, intent);
	}

	protected void init(Intent intent) {
		mDexpath = intent.getStringExtra(PluginConstant.KEY_DEXPATH);
		mDexoutputpath = intent.getStringExtra(PluginConstant.KEY_DEXOPTPATH);
		mLibpath = intent.getStringExtra(PluginConstant.KEY_LIB_PATH);
		mPluginClassName = intent.getStringExtra(PluginConstant.KEY_CLASSNAME);

		mDexClassLoader = PluginContext.getApkClassLoader(mDexpath, mDexoutputpath, mLibpath, null);
		PackageParser.Package pkg = PluginContext.getPluginAppInfo(mDexpath).getPkg();
		if (null != pkg) {
			mPluginPackageName = pkg.packageName;
			mPluginApplicatonClassName = pkg.applicationInfo.className;
			if ((null == mPluginClassName) || ("".equals(mPluginClassName))) {
				mPluginClassName = PluginLoaderUtil.findMainActivityClassName(pkg);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLoadInvoker.invokeClientOnResume(mTaskActivity);
	}

	@Override
	public void startActivity(Intent intent) {
		if (intent.getBooleanExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false)) {
			startPluginActivity(intent, 0, false);
		} else {
			super.startActivity(intent);
		}
	}

	@Override
	public ComponentName startService(Intent intent) {
		if (intent.getBooleanExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false)) {
			return startPluginService(intent);
		} else {
			return super.startService(intent);
		}
	}

	@Override
	public boolean stopService(Intent name) {
		if (name.getBooleanExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false)) {
			return stopPluginService(name);
		} else {
			return super.stopService(name);
		}
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if (intent.getBooleanExtra(PluginConstant.FLAG_IS_EXPLICIT_INTENT, false)) {
			startPluginActivity(intent, requestCode, true);
		} else {
			super.startActivityForResult(intent, requestCode);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mLoadInvoker.invokeClientOnActivityResult(mTaskActivity, requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLoadInvoker.invokeClientOnPause(mTaskActivity);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mLoadInvoker.invokeClientOnStop(mTaskActivity);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mLoadInvoker.invokeClientOnStart(mTaskActivity);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLoadInvoker.invokeClientOnDestroy(mTaskActivity);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mLoadInvoker.invokeClientOnRestart(mTaskActivity);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mLoadInvoker.invokeClientOnNewIntent(mTaskActivity, intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
			boolean result = mLoadInvoker.invokeClientOnKeyDown(mTaskActivity, keyCode, event);
			return result ? result : super.onKeyDown(keyCode, event);
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (null != mTaskActivity) {
			try {
				Method methodOnBackPressed = mTaskActivity.getCurrentClass().getDeclaredMethod("onBackPressed", new Class[] {});
				methodOnBackPressed.setAccessible(true);
				methodOnBackPressed.invoke(mTaskActivity.getCurrentActivity(), new Object[] {});
			} catch (NoSuchMethodException e) {
				super.onBackPressed();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		try {
			mLoadInvoker.invokeClientOnRestoreInstanceState(mTaskActivity, savedInstanceState);
		} catch (BadParcelableException e) {
			e.printStackTrace();
			this.finish();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mLoadInvoker.invokeClientOnSaveInstanceState(mTaskActivity, outState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		mLoadInvoker.invokeClientOnWindowFocusChanged(mTaskActivity, hasFocus);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mLoadInvoker.invokeClientOnPostCreate(mTaskActivity, savedInstanceState);
	}

}
