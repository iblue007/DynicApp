package com.nd.hilauncherdev.dynamic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;


import com.nd.hilauncherdev.dynamic.util.DynamicInflater;
import com.nd.hilauncherdev.kitset.util.ReflectUtilEx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * 插件上下文基类
 * 
 * @ClassName: PluginContextBase
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lytjackson@gmail.com
 * @date 2014-2-12
 * 
 */
public abstract class PluginContextBase extends ContextThemeWrapper implements IContextBase {
	// private static final String SP_PREFIX = "plugin.sp.";
	private AssetManager mAssetManager = null;
	private Resources mResources = null;
	private Theme mTheme = null;
	protected String mPluginPackageName = "";
	private int mPluginThemeResId = -1;
	protected String mDexpath = "";
	protected Context mContext;
	private boolean isFactorySet = false;
	private LayoutInflater mLayoutInflater;
	private ClassLoader mClassLoader = null;

	/**
	 * 
	 * @param base
	 *            父Context
	 * @param themeres
	 *            样式-0
	 * @param pluginPath
	 *            插件apk路径
	 * @param pluginPackageName
	 *            插件包名
	 * @param pluginDexClassLoader
	 *            自定义的DexClassLoader
	 * @throws PluginSrcNotFoundException
	 */
	public PluginContextBase(Context base, int themeres, String pluginPath, String pluginPackageName, DexClassLoader pluginDexClassLoader) throws PluginSrcNotFoundException {
		super(base, themeres);
		mContext = base;
		mDexpath = pluginPath;
		mPluginPackageName = pluginPackageName;
		mClassLoader = pluginDexClassLoader;
		mAssetManager = initPluginAssetManager(pluginPath);
		mResources = initPluginResources(base, mAssetManager);
		mTheme = initPluginTheme(mResources);
	}

	protected AssetManager initPluginAssetManager(String pluginApkPath) throws PluginSrcNotFoundException {
		/**
		 * 1.验证插件源文件是否存在 2.验证插件源文件是否正确
		 */
		File src = new File(pluginApkPath);
		if (!src.exists()) {
			throw new PluginSrcNotFoundException(mDexpath, mPluginPackageName);
		}
		// String innerPluginName = PluginLoaderUtil.getInnerPluginName(this,
		// mPluginPackageName);
		// if(!"".equals(innerPluginName)){
		// PluginLoaderUtil.getMd5(innerPluginName);
		// }

		AssetManager am = null;
		am = ReflectUtilEx.invokeNewAssetManager();
		ReflectUtilEx.invokeAssetManager_addAssetPath(am, pluginApkPath);
		return am;
	}

	@Override
	public Object getSystemService(String name) {
		if ((Context.WINDOW_SERVICE.equals(name)) || (Context.SEARCH_SERVICE.equals(name))) {
			return mContext.getSystemService(name);
		} else if (Context.LAYOUT_INFLATER_SERVICE.equals(name)) {
			return getDynamicInflate();
		} else {
			return mContext.getSystemService(name);
		}
	}

	public LayoutInflater getDynamicInflate() {
		if (null != mLayoutInflater) {
			return mLayoutInflater;
		}
		// mLayoutInflater = LayoutInflater.from(mContext);
		mLayoutInflater = new DynamicInflater(this);
		if (!isFactorySet) {
			mLayoutInflater.setFactory(new LayoutInflater.Factory() {
				@Override
				public View onCreateView(String name, Context context, AttributeSet attrs) {
					View v = null;
					/**
					 * 插件view 使用2种构造方法，尝试构造插件自定义view
					 */
					try {
						v = invokeViewWithAttributeSet(name, PluginContextBase.this, attrs);
						TypedArray a = PluginContextBase.this.obtainStyledAttributes(attrs, com.android.internal.R.styleable.View, 0, 0);
						int id = a.getResourceId(com.android.internal.R.styleable.View_id, View.NO_ID);
						a.recycle();
						if (null != v) {
							if (id != View.NO_ID) {
								v.setId(id);
							}
						}
					} catch (NoSuchMethodException e) {
						try {
							v = invokeView(name, PluginContextBase.this);
						} catch (NoSuchMethodException e1) {
							Log.w("asd", "Custom View must have one constructor at least");
						} catch (IllegalArgumentException e2) {
							e2.printStackTrace();
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					return v;
				}

				/**
				 * view构造方法 带Context和AttributeSet参数
				 * 
				 * @Title: invokeViewWithAttributeSet
				 * @author lytjackson@gmail.com
				 * @date 2013-11-1
				 * @param className
				 * @param context
				 * @param attrs
				 * @return
				 * @throws NoSuchMethodException
				 */
				private View invokeViewWithAttributeSet(String className, Context context, AttributeSet attrs) throws NoSuchMethodException {
					View v = null;
					try {
						Object instance = null;
						Class<?> loadClass = mClassLoader.loadClass(className);
						/**
						 * 找不到此构造方法，抛出NoSuchMethodException
						 */
						Constructor<?> constructor = loadClass.getConstructor(new Class[] { Context.class, AttributeSet.class });
						instance = constructor.newInstance(new Object[] { context, attrs });
						v = (View) instance;
					} catch (ClassNotFoundException e) {
						// ignore exception message.
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					return v;
				}

				/**
				 * view构造方法 只带Context参数
				 * 
				 * @Title: invokeView
				 * @author lytjackson@gmail.com
				 * @date 2013-11-1
				 * @param className
				 * @param context
				 * @return
				 * @throws NoSuchMethodException
				 */
				private View invokeView(String className, Context context) throws NoSuchMethodException {
					View v = null;
					try {
						Object instance = null;
						Class<?> loadClass = mClassLoader.loadClass(className);
						/**
						 * 找不到此构造方法，抛出NoSuchMethodException
						 */
						Constructor<?> constructor = loadClass.getConstructor(new Class[] { Context.class });
						instance = constructor.newInstance(new Object[] { context });
						v = (View) instance;
					} catch (ClassNotFoundException e) {
						// ignore exception message.
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					return v;
				}
			});
			isFactorySet = true;
		}
		return mLayoutInflater;
	}

	/**
	 * 防止多个插件间SharedPreferences文件重名以及便于统一管理,因此重命名SharedPreferences文件,格式为:plugin.
	 * sp.插件包名.sp文件名.xml
	 */
	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		// return super.getSharedPreferences(SP_PREFIX + mPluginPackageName +
		// "." + name, mode);
		return super.getSharedPreferences(name, mode);
	}

	public File getSharedPrefsFile(String name) {
		// return super.getSharedPrefsFile(mPluginPackageName + name);
		return ReflectUtilEx.invokeContext_getSharedPrefsFile(this,  name);
	}

	protected Resources initPluginResources(Context ctx, AssetManager am) {
		Resources res = null;
		res = new Resources(am, ctx.getResources().getDisplayMetrics(), ctx.getResources().getConfiguration());
		return res;
	}

	protected Theme initPluginTheme(Resources res) {
		Theme theme = res.newTheme();
		mPluginThemeResId = getResId("com.android.internal.R.style.Theme");
		theme.applyStyle(mPluginThemeResId, true);
		return theme;
	}

	private int getResId(String res) {
		int resId = -1;
		String str1 = res.substring(0, 2 + res.indexOf(".R."));
		int i = res.lastIndexOf(".");
		String str2 = res.substring(i + 1, res.length());
		String str3 = res.substring(0, i);
		String str4 = str3.substring(1 + str3.lastIndexOf("."), str3.length());
		try {
			resId = Class.forName(str1 + "$" + str4).getDeclaredField(str2).getInt(null);
		} catch (Throwable t) {
			resId = -1;
			t.printStackTrace();
		}
		return resId;
	}

	@Override
	public Theme getTheme() {
		return mTheme;
	}

	@Override
	public AssetManager getAssets() {
		return mAssetManager;
	}

	@Override
	public Resources getResources() {
		return mResources;
	}

	@Override
	public ClassLoader getClassLoader() {
		ClassLoader classLoader = null;
		if (mClassLoader != null) {
			classLoader = mClassLoader;
		} else {
			classLoader = super.getClassLoader();
		}
		return classLoader;
	}

	@Override
	public void startActivity(Intent intent) {
		List<ResolveInfo> resolveInfo = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if ((resolveInfo == null) || (resolveInfo.size() <= 0)) {
			startPluginActivity(intent, -1, false);
		} else {
			mContext.startActivity(intent);
		}
	}

	@Override
	public ComponentName startService(Intent service) {
		List<ResolveInfo> resolveInfo = mContext.getPackageManager().queryIntentServices(service, PackageManager.MATCH_DEFAULT_ONLY);
		if ((resolveInfo == null) || (resolveInfo.size() <= 0)) {
			return startPluginService(service);
		} else {
			return mContext.startService(service);
		}
	}

	@Override
	public boolean stopService(Intent name) {
		List<ResolveInfo> resolveInfo = mContext.getPackageManager().queryIntentServices(name, PackageManager.MATCH_DEFAULT_ONLY);
		if ((resolveInfo == null) || (resolveInfo.size() <= 0)) {
			return stopPluginService(name);
		} else {
			return mContext.stopService(name);
		}
	}

/*	public int getWidgetID() {
		int id = -1;
		try {
			Launcher launcher = (Launcher) mContext;
			id = launcher.mAppWidgetHost.allocateAppWidgetId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}*/

}
