package com.nd.hilauncherdev.dynamic.Transfer;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.felink.corelib.analytics.AnalyticsConstant;
import com.felink.corelib.analytics.HiAnalytics;
import com.felink.corelib.config.Global;
import com.nd.hilauncherdev.analysis.AppAnalysisConstant;
import com.nd.hilauncherdev.datamodel.DynamicConstant;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;

import com.nd.hilauncherdev.kitset.util.DigestUtils;
import com.nd.hilauncherdev.kitset.util.DynamicPluginUtil;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.DownloadUrlManage;
import com.nd.hilauncherdev.widget.shop.util.WidgetType;
import com.nd.hilauncherdev.dynamic.Transfer.ITransfer.*;
import java.io.File;
import java.io.FilenameFilter;

public class PluginTransferUtil {

	public static class PluginInfo {
		public String pluginName;
		public int pluginVersion;

		public PluginInfo() {
			pluginName = "";
			pluginVersion = 0;
		}
	}

	/**
	 * 获取PluginState默认实现
	 *
	 * @Function: com.nd.hilauncherdev.myphone.common.PluginTransferUtil.
	 *            getPluginStateDefaultImpl
	 * @Description:
	 * @param context
	 * @param path
	 * @param pkgName
	 * @param installName
	 * @param usedNames
	 *            使用过的包名，不包括最新的包名
	 * @param info
	 * @param isNeedRestart
	 * @return
	 *
	 * @version:v1.0
	 * @author:linyt
	 * @date:2015年2月2日 下午2:27:23
	 *
	 */
	public static PluginState getPluginStateDefaultImpl(Context context, String path, String pkgName, String installName, String[] usedNames, PluginUpgradeInfo info, boolean isNeedRestart) {
		/**
		 * 内置插件 启用过：验证插件版本、MD5 未启用过：直接启用 外置插件 启用过：
		 * 根据本地json判断是否有新版本：有则先从wifi目录启用，其次网络下载 未启用过： 先从wifi目录启用，其次网络下载
		 */
		File dir = new File(path);
		if (null != dir && !dir.exists()) {
			dir.mkdirs();
		}
		// 内置插件文件名,非空则是内置插件
		String innerPlugin = PluginLoaderUtil.getInnerPluginName(context, pkgName);
		if (null != innerPlugin && !"".equals(innerPlugin)) {
			if (DynamicPluginUtil.isPluginEnabled(context, path, pkgName, usedNames)) {
				int installedVer = getInstallPluginVersion(context, path, installName);
				int apkVer = PluginLoaderUtil.getVerCode(innerPlugin);
				if (apkVer != installedVer) {
					return PluginState.OUT_OF_SYNC_FOR_INNER;
				}
				return verifyPlugin(path + installName, innerPlugin);
			} else { // 未安装
				return PluginState.INNER_PLUGIN;
			}
		} else {
			if (DynamicPluginUtil.isPluginEnabled(context, path, pkgName, usedNames)) {
				/**
				 * 本地无插件，且服务端有新版本 服务端更新 新版本插件
				 */
				if (info == null)
					return PluginState.NORMAL;

				return getStateForUpdatePlugin(context, pkgName, path, installName, info, isNeedRestart);
			} else {
				// 检测wifi目录是否有com.xx_vx_xxx.jar包
				String wifiPluginName = checkHasPlugin(context, DynamicConstant.WIFI_DOWNLOAD_PATH, pkgName).pluginName;
				if (!"".equals(wifiPluginName)) {
					if (!isNeedRestart) {
						FileUtil.moveFile(DynamicConstant.WIFI_DOWNLOAD_PATH + wifiPluginName, WidgetType.MYPHONE_TYPE.getBaseDir() + installName);
					}
					return PluginState.OUTTER_PLUGIN;
				} else {
					if (info == null)
						return PluginState.ERROR;
					// 若未启用过，且桌面版本过低，需升级桌面
					if (needUpgradeLauncher(info))
						return PluginState.NEED_UPGRADE_LAUNCHER;
					return PluginState.NEED_DOWNLOAD;
				}
			}
		}
	}

	/**
	 * Description: 若未启用过，且桌面版本过低，需升级桌面
	 * Author: guojianyun_91 
	 * Date: 2015年4月23日 下午5:25:31
	 * @param info
	 * @return
	 */
	public static boolean needUpgradeLauncher(PluginUpgradeInfo info){
		int realV = TelephoneUtil.getVersionCode(Global.getApplicationContext());
//		if(Global.isBaiduLauncher()){
//			realV = MultiLauncherVersionController.get91VersionForBaidu(realV);
//		}
		return info.getTargetVersion() > realV;
	}
	
	/**
	 * 
	 *
	 * @Function: com.nd.hilauncherdev.myphone.common.PluginTransferUtil.
	 *            getStateForUpdatePlugin
	 * @Description:
	 * @param context
	 * @param pkgName
	 * @param path
	 *            插件存放目录
	 * @param installName
	 *            插件存放文件名
	 * @param info
	 *            json文件中定义的插件最新版本
	 * @param isNeedRestart
	 *            插件是否在 已更新但还未重启的状态重启
	 * @return 插件状态
	 *
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年10月21日 下午4:32:37
	 *
	 */

	private static PluginState getStateForUpdatePlugin(Context context, String pkgName, String path, String installName, PluginUpgradeInfo info, boolean isNeedRestart) {
		int installedVer = getInstallPluginVersion(context, path, installName);
		int apkVer = info.getVersion();
		// 判断本地已启用版本是否最新，或者桌面版本太低无法使用最新版插件
		if (installedVer >= apkVer || needUpgradeLauncher(info)) {
			return PluginState.NORMAL;
		} else {
			return getStateForUpdatePluginByWifiDownload(context, pkgName, installName, isNeedRestart, apkVer);
		}
	}

	/**
	 * Description: 根据wifidownload目录下载情况，判断启用插件的状态
	 * Author: guojianyun_dian91 
	 * Date: 2015年11月26日 下午5:39:27
	 * @param context
	 * @param pkgName 插件包名
	 * @param installName 插件放在myphone/plugin下的文件名称，一般格式是：插件包名.jar
	 * @param isNeedRestart 是否需要重启进程后再启用插件
	 * @param apkVer 需要启用新插件的版本号
	 * @return
	 */
	public static PluginState getStateForUpdatePluginByWifiDownload(Context context, String pkgName, String installName,
																			  boolean isNeedRestart, int apkVer) {
		// 查询wifi目录下是否有最新版本插件
		// FileUtil.delFile(path + installName);
		PluginInfo pluginInfo = checkHasPlugin(context, DynamicConstant.WIFI_DOWNLOAD_PATH, pkgName);
		int wifiDownVer = pluginInfo.pluginVersion;
		if (wifiDownVer >= apkVer) {
			if (!isNeedRestart) {
				try {
					FileUtil.moveFile(DynamicConstant.WIFI_DOWNLOAD_PATH + pluginInfo.pluginName, WidgetType.MYPHONE_TYPE.getBaseDir() + installName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return PluginState.OUT_OF_SYNC_FOR_OUTTER;
		} else {
			// 清理wifi目录旧版本插件
			//TODO:xuqunxing
			FileUtil.delFile(DynamicConstant.WIFI_DOWNLOAD_PATH + pluginInfo.pluginName);
			return PluginState.NEED_UPGRADE;
		}
	}

	/**
	 * 内置插件的包验证
	 *
	 * @Function: 
	 *            com.nd.hilauncherdev.myphone.common.PluginTransferUtil.verifyPlugin
	 * @Description:
	 * @param installName
	 *            安装到sd卡的文件路径
	 * @param innerPlugin
	 *            内置插件的文件名（带MD5值）
	 * @return
	 *
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年10月21日 下午4:24:42
	 *
	 */
	private static PluginState verifyPlugin(String installName, String innerPlugin) {
		// 验证md5
		String md5Cd = PluginLoaderUtil.getMd5(innerPlugin);
		if ("".equals(md5Cd)) {
			return PluginState.OUT_OF_SYNC_FOR_INNER;
		}
		File apkFile = new File(installName);
		String installedMd5 = DigestUtils.md5Hex(apkFile.length() + "91");
		if (!installedMd5.equals(md5Cd)) {
			return PluginState.OUT_OF_SYNC_FOR_INNER;
		} else {
			return PluginState.NORMAL;
		}
	}

	public static int getInstallPluginVersion(Context context, String path, String installName) {
		String installPath = path + installName;
		int installedVer = getRightVersionCode(context, installPath);
		return installedVer;
	}

	public static int getRightVersionCode(Context ctx, String installPath) {
		PackageManager pm = ctx.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(installPath, PackageManager.GET_ACTIVITIES);
		if (null == packageInfo) {
			return 0;
		}
		return packageInfo.versionCode;
	}

	public static String getRightVersionName(Context ctx, String installPath) {
		PackageManager pm = ctx.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(installPath, PackageManager.GET_ACTIVITIES);
		if (null == packageInfo) {
			return "";
		}
		return packageInfo.versionName;
	}

	/**
	 * 查找指定目录下是否有对应包名的插件，并返回插件信息,如果存在多个版本则取最高版本号的那个
	 *
	 * @Function: 
	 *            com.nd.hilauncherdev.myphone.common.PluginTransferUtil.checkHasPlugin
	 * @Description:
	 * @param path
	 * @param pkg
	 * @return
	 *
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年8月6日 下午2:10:52
	 *
	 */
	public static PluginInfo checkHasPlugin(Context ctx, String path, final String pkg) {
		PluginInfo pluginInfo = new PluginInfo();
		if (null == path || "".equals(path) || null == pkg || "".equals(pkg)) {
			return pluginInfo;
		}
		File pluginPath = new File(path);
		String[] plugins = pluginPath.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (null != filename && filename.contains(pkg) && (filename.endsWith(".jar") || filename.endsWith(".apk")))
					return true;
				return false;
			}
		});
		if (null == plugins || plugins.length <= 0) {
			return pluginInfo;
		}
		int tmpVersion = 0;
		for (String plugin : plugins) {
			int version = getRightVersionCode(ctx, path + plugin);
			if (version > tmpVersion) {
				tmpVersion = version;
				pluginInfo.pluginVersion = version;
				pluginInfo.pluginName = plugin;
			}
		}
		return pluginInfo;
	}

	/**
	 * 获取PluginState默认实现
	 * 
	 * @author dingdj Date:2014-1-13上午10:41:17
	 * @param mContext
	 * @param assetPath
	 * @param downloadPath
	 * @param assetPartName
	 * @param installName
	 * @return
	 */
	public static PluginState getPluginStateDefaultImpl(Context mContext, String assetPath, String downloadPath, String assetPartName, String installName) {
		try {
			File dir = new File(downloadPath);
			if (!dir.exists()) {
				dir.mkdirs();
				return PluginState.OUT_OF_SYNC_FOR_INNER;
			}

			String assetFullName = "";
			for (String name : mContext.getAssets().list(assetPath)) {
				if (name.contains(assetPartName))
					assetFullName = name;
			}

			if ("".equals(assetFullName)) {
				return PluginState.OUT_OF_SYNC_FOR_INNER;
			}

			File apkFile = new File(downloadPath + installName);
			if (apkFile.exists() && DynamicPluginUtil.isPluginDexExisted(mContext, PluginLoaderUtil.getPureName(installName))) {
				int installedVer = getInstallPluginVersion(mContext, downloadPath, installName);
				int apkVer = PluginLoaderUtil.getVerCode(assetFullName);
				if (apkVer == -1)
					return PluginState.OUT_OF_SYNC_FOR_INNER;

				String md5Cd = PluginLoaderUtil.getMd5(assetFullName); // 验证md5
				String installedMd5 = DigestUtils.md5Hex(apkFile.length() + "91");
				if ("".equals(md5Cd))
					return PluginState.OUT_OF_SYNC_FOR_INNER;

				if (apkVer == installedVer) {
					if (!installedMd5.equals(md5Cd))
						return PluginState.OUT_OF_SYNC_FOR_INNER;
					else
						return PluginState.NORMAL;
				} else {// asset 中的文件的文件版本高于（或低于，桌面版本回退造成）sd卡上，覆盖重新安装
					return PluginState.OUT_OF_SYNC_FOR_INNER;
				}

			} else { // 未安装
				return PluginState.OUT_OF_SYNC_FOR_INNER;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return PluginState.ERROR;
		}
	}

	public static String getPluginDownloadUrl(Context context, PluginUpgradeInfo info) {
		try {
			if (info == null)
				return null;
			String url = info.getDownloadPath();
			if (StringUtil.isEmpty(info.getDownloadPath())) {// 目前为空，使用默认下载路径
				url = DownloadUrlManage.getDownloadUrlFromPackageName(context, info.getPkgName(), null, AppAnalysisConstant.SP_DEFAULT_CHANNEL_APP);
				// url += "&vc=" + info.getVersion();
			}
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public static void startAppOrApi(Context ctx, Class<?> clazz, String path, String packageName, Intent intent) {
//		if (intent.getBooleanExtra(BaseTransferActivity.KEY_OPEN_API, false)) {
//			if (ctx instanceof Activity) {
//				((Activity) ctx).setResult(Activity.RESULT_OK);
//			}
//		} else {
//			try {
//				Method method = clazz.getDeclaredMethod("startPluginLoaderActivity", new Class[] { Context.class, String.class, String.class, Intent.class, Integer.TYPE, Boolean.TYPE });
//				method.invoke(clazz, new Object[] { ctx, path, packageName, intent, 0, false });
//			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	/**
	 * Description: 统计动态插件版本
	 * Author: guojianyun_91 
	 * Date: 2015年5月8日 下午4:26:45
	 * @param ctx
	 * @param fileName
	 * @param pkgName
	 */
	public static void statPluginVersion(Context ctx, String fileName, String pkgName){
		try{
			String pkgShortName = null;
			if("com.baidu.launcher.petfloat".equals(pkgName)){
				pkgShortName = "moliqiu";
			}else if("com.nd.android.anyshare".equals(pkgName)){
				pkgShortName = "kuaichuan";
			}else if("com.baidu91.launcher.manager".equals(pkgName)){
				pkgShortName = "guanjia";
			}else if("com.baidu91.launcher.individuation".equals(pkgName)){
				pkgShortName = "ziti";
			}else if("com.wireless.assistant.mobile.market".equals(pkgName)){
				pkgShortName = "appstore";
			}else if("com.nd.hilauncherdev.plugin.navigation".equals(pkgName)){
				pkgShortName = "0pin";
			}else if("com.nd.hilauncherdev.myphone.mywallpaper".equals(pkgName)){
				pkgShortName = "bizhi";
			}else if("com.nd.hilauncherdev.myphone.privatezone".equals(pkgName)){
				pkgShortName = "yinsi";
			}else if("com.wireless.android.jifenqiang".equals(pkgName)){
				pkgShortName = "jifenqiang";
			}else if("com.baidu91.launcher.netflow".equals(pkgName)){
				pkgShortName = "liuliang";
			}else if("com.baidu91.launcher.assist".equals(pkgName)){
				pkgShortName = "jiasu";
			}else if("com.baidu.launcher.plugin".equals(pkgName)){
				pkgShortName = "5in1";
			}else if("com.felink.readercenter".equals(pkgName)){
                pkgShortName = "ireader";
            }else if("com.felink.videopaper.publish".equals(pkgName)){
				pkgShortName = "publish";
			}

			
			if(pkgShortName == null)
				return;
			int ver = PluginTransferUtil.getInstallPluginVersion(ctx, DynamicConstant.PLUGIN_DIR, fileName);
			HiAnalytics.submitEvent(ctx, AnalyticsConstant.DYN_PLUGIN_VERSION, pkgShortName+"_"+ver);
			//Log.e("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", pkgShortName+"_"+ver); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
