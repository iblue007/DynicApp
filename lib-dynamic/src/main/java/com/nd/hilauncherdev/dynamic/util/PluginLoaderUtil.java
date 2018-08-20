package com.nd.hilauncherdev.dynamic.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageParser;
import android.content.pm.PackageParser.ActivityIntentInfo;
import android.content.pm.PackageParser.Component;
import android.content.pm.PackageParser.IntentInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.nd.hilauncherdev.datamodel.DynamicConstant;
import com.nd.hilauncherdev.dynamic.PluginContext;
import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.dynamic.Transfer.Plugin;
import com.nd.hilauncherdev.dynamic.Transfer.PluginParser;
import com.nd.hilauncherdev.dynamic.bean.PluginAppInfo;
import com.nd.hilauncherdev.dynamic.other.PluginContextForShopProcess;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.kitset.util.DynamicPluginUtil;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.launcher.LauncherApplicationLike;
import com.nd.hilauncherdev.widget.shop.util.WidgetPositionType;
import com.nd.hilauncherdev.widget.shop.util.WidgetType;
import com.nd.hilauncherdev.dynamic.Transfer.ITransfer.PluginTransferListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginLoaderUtil {
	private static final String TAG = "intent-filter";

	/**
	 * 查找主activity的类路径
	 * 
	 * @Title: findMainActivityClassName
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param pkg
	 * @return
	 */
	public static String findMainActivityClassName(PackageParser.Package pkg) {
		String className = "";
		if (null == pkg) {
			return className;
		}
		ArrayList<PackageParser.Activity> activities = pkg.activities;
		if (null != activities && activities.size() > 0) {
			for (PackageParser.Activity activity : activities) {
				ArrayList<ActivityIntentInfo> intents = activity.intents;
				boolean isBingo = false;
				if (null != intents && intents.size() > 0) {
					for (ActivityIntentInfo activityIntentInfo : intents) {
						int num = activityIntentInfo.countActions();
						if (num > 0) {
							for (int i = 0; i < num; i++) {
								if (Intent.ACTION_MAIN.equals(activityIntentInfo.getAction(i))) {
									isBingo = true;
									break;
								}
							}
						}
						if (isBingo) {
							break;
						}
					}
				}
				if (isBingo) {
					className = activity.className;
				}
			}
		}
		return className;
	}

	/**
	 * 进行意图匹配,失败返回空串
	 * 
	 * @Title: tryToMatchIntent
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param intent
	 * @param intentFilter
	 * @return
	 */
	public static String tryToMatchIntent(Intent intent, Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> intentFilter) {
		String className = "";
		for (Iterator<Entry<IntentFilter, Component<IntentInfo>>> iterator = intentFilter.entrySet().iterator(); iterator.hasNext();) {
			Entry<IntentFilter, Component<IntentInfo>> entry = iterator.next();
			int match = matchIntent(intent, entry.getKey());
			if (match > 0) {
				className = entry.getValue().className;
				break;
			}
		}
		return className;
	}

	/**
	 * 计算意图的匹配度,返回值大小代表匹配度,值越大,代表越匹配
	 * 
	 * @Title: matchIntent
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param intent
	 * @param filter
	 * @return
	 */
	public static int matchIntent(Intent intent, IntentFilter filter) {
		return filter.match(intent.getAction(), intent.getType(), intent.getScheme(), intent.getData(), intent.getCategories(), TAG);
	}

	/**
	 * 搜索到Activity的Class对象
	 * 
	 * @Title: tryToGetRealActivityClass
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param 
	 * @return
	 */
/*	public static Class<?> tryToGetRealActivityClass(Object activity) {
		if (activity instanceof Activity) {
			Class<?> activityClass = activity.getClass();
			do {
				activityClass = activityClass.getSuperclass();
			} while (null != activityClass && !"android.app.Activity".equals(activityClass.getName()));
			return activityClass;
		}
		return null;
	}*/

	public static String getStartClassName(String path, Intent intent) {
		if (null == intent) {
			return "";
		}
		String toClass = intent.getStringExtra(PluginConstant.EXTRA_MAIN_CLASS_NAME);
		if (!DynamicPluginUtil.isStringEmpty(toClass)) {
			return toClass;
		}
		String className = "";
		ComponentName cn = intent.getComponent();
		String action = intent.getAction();
		Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> intentFilter = getApkIntentFilterInfo(path);
		// if ((null == cn) || (null != action && !"".equals(action))) {
		// Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>>
		// intentFilter = PluginLoaderUtil.getApkIntentFilterInfo(path);
		// className = PluginLoaderUtil.tryToMatchIntent(intent, intentFilter);
		// } else {
		// className = cn.getClassName();
		// }
		if (!DynamicPluginUtil.isStringEmpty(action)) {
			className = tryToMatchIntent(intent, intentFilter);
		}
		if ("".equals(className)) {
			if (null != cn) {
				className = cn.getClassName();
			}
		}
		return className;
	}

	/**
	 * 找到指定字段,不在子类则往父类查找
	 * 
	 * @Title: getClassField
	 * @author lytjackson@gmail.com
	 * @date 2014-2-12
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getClassField(Class<?> clazz, String fieldName) {
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			return getClassField(superclass, fieldName);
		}
		return null;
	}

	/**
	 * 解析apk对象,组件的intent-filter信息并缓存,用于隐式intent的匹配
	 * 
	 * @Title: parseApkIntentFilterInfo
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param dexpath
	 * @return
	 */
/*	public static Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> parseApkIntentFilterInfo(String dexpath) {
		PackageParser pp = new PackageParser(dexpath);
		File scanFile = new File(dexpath);
		String scanPath = scanFile.getPath();
		DisplayMetrics metrics = new DisplayMetrics();
		metrics.setToDefaults();
		PackageParser.Package pkg = pp.parsePackage(scanFile, scanPath, metrics, PackageParser.PARSE_IS_SYSTEM | PackageParser.PARSE_IS_SYSTEM_DIR);
		Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> intentFilter = new HashMap<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>>();
		parseComponentIntentFilterInfo(intentFilter, pkg.activities);
		parseComponentIntentFilterInfo(intentFilter, pkg.services);
		return intentFilter;
	}*/

	/**
	 * 解析组件的intent-filter信息,用于隐式intent的匹配
	 * 
	 * @Title: parseComponentIntentFilterInfo
	 * @author lytjackson@gmail.com
	 * @date 2013-11-4
	 * @param intentFilter
	 *            不能为空
	 * @param components
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void parseComponentIntentFilterInfo(Map<IntentFilter, Component<IntentInfo>> intentFilter, ArrayList<? extends PackageParser.Component> components) {
		if (null != components && components.size() > 0 && null != intentFilter) {
			for (PackageParser.Component component : components) {
				ArrayList<PackageParser.IntentInfo> ci = component.intents;
				if (null == ci || ci.size() <= 0) {
					continue;
				}
				for (PackageParser.IntentInfo ii : ci) {
					IntentFilter filter = new IntentFilter(ii);
					intentFilter.put(filter, component);
				}
			}
		}
	}


	/**
	 * 从插件文件给中获取对应的插件版本号
	 * @param pluginFileName
	 * @return
	 */
	public static int getPluginVersionFromName(String pluginFileName){
		if(TextUtils.isEmpty(pluginFileName))
			return 0;
		try {
			int lastIndex = pluginFileName.lastIndexOf(".");
			String fileNameWithoutType = pluginFileName.substring(0,lastIndex);
			int secondLastIndex =fileNameWithoutType.lastIndexOf(".");
			String verString = pluginFileName.substring(secondLastIndex+1,lastIndex);
			return Integer.parseInt(verString);
		}catch (Exception e){
		}
		return 0;

	}


	/**
	 * 获取当前版本最新的插件名
	 * @param mPkgName
	 * @return
	 */
	public static String getCurrentNewestPluginFilename(final String mPkgName){
		if(TextUtils.isEmpty(mPkgName))
			return "";

		try {
			File pluginDir = new File(DynamicConstant.PLUGIN_DIR);
			File newestPluginFile = null;
			int newestPluginversion = 0;
			if(pluginDir.exists()){
				File pluginFiles[] = pluginDir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File file, String s) {
						if(!TextUtils.isEmpty(s) && s.contains(mPkgName)){
							return true;
						}
						return false;
					}
				});
				//查找出版本号最大的插件
				if (pluginFiles != null && pluginFiles.length > 0) {
					for (int i = 0; i < pluginFiles.length; i++) {
						File currentPluginFile = pluginFiles[i];
						String currentPluginFileName = currentPluginFile.getName();
						int currentPluginVer = getPluginVersionFromName(currentPluginFileName);
						if (!TextUtils.isEmpty(currentPluginFileName) &&
								currentPluginVer >= newestPluginversion) {
							newestPluginversion = currentPluginVer;
							newestPluginFile = currentPluginFile;
						}
					}
				}

			}
			if(newestPluginFile != null){
				return newestPluginFile.getName();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return "";
	}


	/**
	 * 获取规定的插件存放lib的路径,如果不存在,则创建
	 * 
	 * @Title: getPluginLibPath
	 * @author lytjackson@gmail.com
	 * @date 2013-12-13
	 * @param ctx
	 * @param pluginPackageName
	 *            插件包名
	 * @return
	 */
	public static String getPluginLibPath(Context ctx, String pluginPackageName) {
		String cpu = "armeabi";
		String cpuType = Build.CPU_ABI.toLowerCase();
		if (!cpuType.contains("armeabi")) {
			cpu = "x86";
		}
		File lib = null;
		if(!DynamicPluginUtil.isNeedRestart(pluginPackageName)){
			String latePluginPackage = getPureNameWithVer(getCurrentNewestPluginFilename(pluginPackageName));
			if(TextUtils.isEmpty(latePluginPackage)){
				latePluginPackage = pluginPackageName;
			}
			lib = new File(DynamicPluginUtil.getRootApkFile(ctx) + "/" + latePluginPackage + "/lib/" + cpu);
		}else{
			lib = new File(DynamicPluginUtil.getRootApkFile(ctx) + "/" + pluginPackageName + "/lib/" + cpu);
		}
		if (!lib.exists()) {
			lib.mkdirs();
		}
		return lib.getAbsolutePath();
	}

	/**
	 * 获取规定的插件存放dex的路径,如果不存在,则创建
	 * 
	 * @Title: getPluginDexOptPath
	 * @author lytjackson@gmail.com
	 * @date 2013-12-13
	 * @param ctx
	 * @param pluginPackageName
	 *            插件包名
	 * @return
	 */
	public static String getPluginDexOptPath(Context ctx, String pluginPackageName) {
		String latestPluginPackage = "";
		if(!DynamicPluginUtil.isNeedRestart(pluginPackageName)){
			latestPluginPackage = getPureNameWithVer(getCurrentNewestPluginFilename(pluginPackageName));
			if(TextUtils.isEmpty(latestPluginPackage)){
				latestPluginPackage = pluginPackageName;
			}
		}else{
			latestPluginPackage = pluginPackageName;
		}

		File dex = new File(DynamicPluginUtil.getRootApkFile(ctx) + "/" + latestPluginPackage);
		if (!dex.exists()) {
			dex.mkdir();
		}
		return dex.getAbsolutePath();
	}

	public static Map<IntentFilter, PackageParser.Component<PackageParser.IntentInfo>> getApkIntentFilterInfo(String dexpath) {
		PluginAppInfo plugin = PluginContext.getPluginAppInfo(dexpath);
		return plugin.getIntentFilter();
	}

	public static boolean enableInnerPlugin(Context ctx, WidgetType widgetType, String assetPartName, String pluginName, PluginTransferListener listener) {
		return enablePlugin(ctx, widgetType, WidgetPositionType.OFFLINE_TYPE, assetPartName, pluginName, listener);
	}

	public static boolean enableOuterPlugin(Context ctx, WidgetType widgetType, String pluginName, PluginTransferListener listener) {
		return enablePlugin(ctx, widgetType, WidgetPositionType.ONLINE_TYPE, "", pluginName, listener);
	}

	/**
	 * 插件启用入口
	 * 
	 * @Title: enablePlugin
	 * @author lytjackson@gmail.com
	 * @date 2014-1-14
	 * @param ctx
	 * @param widgetType
	 *            插件类型
	 * @param positionType
	 *            插件存放位置,内置或者服务端
	 * @param assetPartName
	 *            内置assets目录,内置插件必须制定目录,服务端下载的插件可传""
	 * @param pluginName
	 *            插件名
	 * @param listener
	 *            插件启用进度监控器
	 * @return
	 */
	public static boolean enablePlugin(Context ctx, WidgetType widgetType, WidgetPositionType positionType, String assetPartName, String pluginName, PluginTransferListener listener) {
		try {
			String dexpath = widgetType.getBaseDir() + pluginName;
			if (null != listener) {
				listener.onPreparing(ctx);
			}
			String pkgName = getPureName(pluginName);
			String dexoutputpath = getPluginDexOptPath(ctx, pkgName);
			String pluginLibPath = getPluginLibPath(ctx, pkgName);
			copyApk(ctx, widgetType, positionType, assetPartName, pluginName);
			copySo(ctx, widgetType.getBaseDir(), pluginName);
			if (null != listener) {
				listener.onInstalling(ctx);
			}
			PluginContext.updatePluginCache(dexpath);
			PluginContextForShopProcess.updatePluginCache(dexpath);
			PluginContext.getApkClassLoader(dexpath, dexoutputpath, pluginLibPath, LauncherApplicationLike.class.getClassLoader());
			if (null != listener) {
				listener.onInstallSuc(ctx);
			}
			if (WidgetType.LAUNCHER_TYPE == widgetType) {
				//TODO:zhou 暂时先屏蔽(猜测是用来刷新widget的，不需要)
//				Intent intent = new Intent(BeautyUpgradeService.ACTION_REFRESH_WIDGETS);
//				intent.putExtra("pkg", pkgName);
//				ctx.sendBroadcast(intent);
			}
		} catch (Exception e) {
			if (null != listener) {
				listener.onTransferError(ctx, -1);
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean repairInnerPlugin(Context ctx, String path, String packageName) {
		// 1、判断插件存放位置---查询assets目录是否存在
		// 2、内置的直接复制，服务端的提示跳转下载
		if (null == packageName || "".equals(packageName)) {
			return false;
		}
		WidgetType widgetType = getWidgetType(path);
		return enableInnerPlugin(ctx, widgetType, packageName, packageName + ".jar", null);
	}

	public static boolean isInnerPlugin(Context ctx, String packageName) {
		boolean isInnerplugin = false;
		List<String> innerPkg = getAllInnerPluginPkgName(ctx);
		for (String string : innerPkg) {
			if (string.contains(packageName)) {
				isInnerplugin = true;
				break;
			}
		}
		return isInnerplugin;
	}

	public static String getInnerPluginName(Context ctx, String packageName) {
		List<String> innerPkg = getAllInnerPluginPkgName(ctx);
		for (String string : innerPkg) {
			if (string.contains(packageName)) {
				return string;
			}
		}
		return "";
	}

	private static List<String> getAllInnerPluginPkgName(Context ctx) {
		// List<String> list = new ArrayList<String>();
		// try {
		// for (String name : ctx.getAssets().list("plugin")) {
		// list.add(name);
		// }
		// for (String name : ctx.getAssets().list("plugin/dynamic")) {
		// list.add(name);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return PluginParser.getInstance(ctx).getAllInnerPluginPkgName();
	}

	/**
	 * 获取插件类型--小部件,我的手机
	 * 
	 * @Title: getWidgetType
	 * @author lytjackson@gmail.com
	 * @date 2014-2-8
	 * @param path
	 * @return
	 */
	public static WidgetType getWidgetType(String path) {
		return LoaderUtil.isClientExist(LoaderUtil.readClientApkInfo(path)) ? WidgetType.LAUNCHER_TYPE : WidgetType.MYPHONE_TYPE;
	}

	private static void copyApk(Context ctx, WidgetType widgetType, WidgetPositionType positionType, String assetPartName, String pluginName) throws IOException {
		// 将插件从下载存放目录移动到使用目录
		switch (positionType) {
		case OFFLINE_TYPE:
			String assetFullName = "";
			String assetsPath = "";
			if (widgetType == WidgetType.MYPHONE_TYPE) {
				assetsPath = "plugin";
			} else if (widgetType == WidgetType.LAUNCHER_TYPE) {
				assetsPath = "plugin/dynamic";
			} else {
				assetsPath = "plugin";
			}
			for (String name : ctx.getAssets().list(assetsPath)) {
				if (name.contains(assetPartName)) {
					assetFullName = name;
					break;
				}
			}
			FileUtil.copyAssetsFile(ctx, widgetType.getAssetsDir() + assetFullName, widgetType.getBaseDir(), pluginName);
			break;
		case ONLINE_TYPE:
		case ONLINE_WIFI_TYPE:
			FileUtil.createDir(widgetType.getBaseDir());
			FileUtil.moveFile(positionType.getBaseDir() + pluginName, widgetType.getBaseDir() + pluginName);
			break;
		default:
			break;
		}
	}

/*	public static PluginState verifyPlugin(Context ctx, WidgetType widgetType, String pluginName) {

		return PluginState.NORMAL;
	}*/

	/**
	 * 拷贝so文件
	 * 
	 * @Title: copySo
	 * @author lytjackson@gmail.com
	 * @date 2014-1-14
	 * @param context
	 * @param pluginName
	 */
	public static void copySo(Context context, String dir, String pluginName) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			String pkgName = getPureNameWithVer(pluginName);
			ZipFile zip = new ZipFile(dir + pluginName);
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				if (zipEntry == null)
					continue;

				String str = zipEntry.getName();
				if (!str.startsWith("lib"))
					continue;

				ZipEntry entry = zip.getEntry(str);
				is = zip.getInputStream(entry);

				File tarDir = new File(context.getFilesDir(), pkgName + "/" + str.substring(0, str.lastIndexOf("/")));
				if (!tarDir.exists())
					tarDir.mkdirs();

				File targetFile = new File(context.getFilesDir(), pkgName + "/" + str);
				if (targetFile.exists())
					targetFile.delete();

				fos = new FileOutputStream(targetFile);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (fos != null)
					fos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static String getMd5(String name) {
		try {
			String[] ss = name.split("_");
			if (ss != null && ss.length >= 5) {
				return getPureName(ss[4]);
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 根据插件文件名取得插件包名 插件命名规则 com.xx.jar
	 *
	 * @Title: getPureName
	 * @author lytjackson@gmail.com
	 * @date 2014-1-14
	 * @param name
	 * @return
	 */
	public static String getPureName(String name) {
		try {
			int dotIdx = name.lastIndexOf(".");
			if (dotIdx != -1) {
				String fileNameWithoutType = name.substring(0,dotIdx);
				int dotIdx2 = fileNameWithoutType.lastIndexOf(".");
				if(dotIdx2 != -1){
					try{
						int ver = Integer.parseInt(name.substring(dotIdx2+1,dotIdx));
						return name.substring(0,dotIdx2);
					}catch (Exception e){
						e.printStackTrace();
					}
					return name.substring(0,dotIdx);
				}else{
					return name.substring(0, dotIdx);
				}
			} else {
				return name;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return name;
		}
	}


	public static String getPureNameWithVer(String name) {
		try {
			int dotIdx = name.lastIndexOf(".");
			if (dotIdx != -1) {
				return name.substring(0, dotIdx);
			} else {
				return name;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return name;
		}
	}

	public static int getVerCode(String name) {
		try {
			String[] ss = name.split("_");
			if (ss != null && ss.length >= 3) {
				return Integer.parseInt(ss[2]);
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 插件修复,必须在线程中运行
	 * 
	 * @Title: repairPlugin
	 * @author lytjackson@gmail.com
	 * @date 2014-2-25
	 * @param ctx
	 * @param path
	 * @param packageName
	 */
	public static void repairPlugin(Context ctx, String path, String packageName) {
		// 插件包不存在处理逻辑
		// 1、判断插件存放位置---查询assets目录是否存在
		// 2、内置的直接复制，服务端的提示跳转下载
		if (!DynamicPluginUtil.isStringEmpty(packageName)) {
			boolean isInnerplugin = isInnerPlugin(ctx, packageName);
			Handler mHandler = new RepairHandler(ctx, ctx.getMainLooper());
			if (isInnerplugin) {
				mHandler.sendEmptyMessage(RepairHandler.REPAIR_START);
				boolean suc = repairInnerPlugin(ctx, path, packageName);
				Message msg = mHandler.obtainMessage(RepairHandler.REPAIR_RESULT, suc);
				mHandler.sendMessage(msg);
			} else {
				mHandler.sendEmptyMessage(RepairHandler.REPAIR_OUTER_PLUGIN);
				tryToRepairPlugin(ctx, path);
			}
		}
	}

	/**
	 * 修复外部插件，桌面小部件直接跳转到商城 我的手机插件。。
	 */
	private static void tryToRepairPlugin(Context ctx, String path) {
		if (null == path || "".equals(path)) {
			return;
		}
		//TODO:zhou 跳转到匣子里的小部件商城，本APP并没有这个商城所以不实现
		if (path.contains(DynamicConstant.WIDGET_PLUGIN_DIR)) {
//			Intent intent = new Intent();
//			intent.setClass(ctx, WidgetShopActivity.class);
//			SystemUtil.startActivitySafely(ctx, intent);
		} else if (path.contains(DynamicConstant.PLUGIN_DIR)) {
			// 留待有缘人继续。。
		}
	}

	/**
	 * 检查插件是否需要更新新版本
	 * 
	 * @Function: com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil.check
	 * @Description:
	 * @param 
	 * @param 
	 * @param 
	 * @return
	 * 
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年8月8日 下午2:46:56
	 * 
	 */
/*	public static boolean checkPluginNeedUpdate(Context ctx, String pckageName, int version) {
		if (DynamicPluginUtil.isPluginEnabled(ctx, CommonGlobal.PLUGIN_DIR, pckageName)) {
			String path = CommonGlobal.PLUGIN_DIR + pckageName + ".jar";
			PackageManager pm = ctx.getPackageManager();
			PackageInfo packageInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
			if (null != packageInfo) {
				int installedVer = packageInfo.versionCode;
				if (installedVer >= version) {
					return false;
				}
			}
		}
		return true;
	}*/


	public static String assembleInstallPath(Plugin plugin) {
		String installPath = "";
		if (null != plugin) {
			WidgetType widgetType = WidgetType.getEnumValue(plugin.type);
			if (widgetType == WidgetType.MYPHONE_TYPE) {
				installPath = DynamicConstant.PLUGIN_DIR + plugin.pkg + ".jar";
			} else if (widgetType == WidgetType.LAUNCHER_TYPE) {
				installPath = DynamicConstant.WIDGET_PLUGIN_DIR + plugin.pkg + ".jar";
			}
		}
		return installPath;
	}

	public static void showPluginError(final Activity ctx, final String dexpath) {
		ViewFactory.getAlertDialogEx(ctx, -1, ctx.getString(R.string.common_tip), ctx.getString(R.string.plugin_update_restart_tip2), ctx.getString(R.string.plugin_update_restart2), ctx.getString(R.string.plugin_update_skip2), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				File s = new File(dexpath);
				if (null != s && s.exists()) {
					s.delete();
				}
				System.exit(0);
			}
		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ctx.finish();
			}
		}).show();
	}
}
