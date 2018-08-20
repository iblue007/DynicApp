package com.nd.hilauncherdev.kitset.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import static com.nd.hilauncherdev.widget.shop.util.WidgetType.DATA_DATA_TYPE;

public class DynamicPluginUtil {


	/**
	 *	保存后台配置成需要提示重启进程生效的插件包
	 *  如果后台配置成需要提示重启进程生效，则走原先的逻辑
	 *  否则，走更新后的逻辑
	 */
	private static Map<String,Integer> isServerConfigToNeedRestart = new HashMap<String, Integer>();

	/**
	 *
	 * @param pkgName 包名
     */
	public static void addToNeedRestart(String pkgName){
		if(isServerConfigToNeedRestart != null){
			Log.e("=====","======mPkgName:"+pkgName+"--mFileName:"+null);
			isServerConfigToNeedRestart.put(pkgName,0);
		}
	}

	/**
	 * 当前包名的插件是否配置成需要提示重启的方式
	 * @param pkgName 可能为包名+ “.” + 序号 如 com.**.1
	 * @return
     */
	public static boolean isNeedRestart(String pkgName){
		if(isServerConfigToNeedRestart == null)
			return false;
		if(isServerConfigToNeedRestart.containsKey(pkgName))
			return true;
		if(isServerConfigToNeedRestart.containsKey(stripeVersionFromName(pkgName)))
			return true;
		return false;

	}

	/**
	 * 移除
	 * @param pkgName
     */
	public static void removeFromNeedRestart(String pkgName){

		if(isServerConfigToNeedRestart == null)
			return;
		if(isServerConfigToNeedRestart.containsKey(pkgName)){
			isServerConfigToNeedRestart.remove(pkgName);
		}
		if(isServerConfigToNeedRestart.containsKey(stripeVersionFromName(pkgName))){
			isServerConfigToNeedRestart.remove(stripeVersionFromName(pkgName));
		}

	}

	/**
	 * 插件从名字中去掉版本号
	 * @param pkgName 如 com.**.1
	 * @return
     */
	public static String stripeVersionFromName(String pkgName){

		try {
			int dotIdx = pkgName.lastIndexOf(".");
			if (dotIdx != -1) {
				try {
					int ver = Integer.parseInt(pkgName.substring(dotIdx));
					String pkgWithouVer = pkgName.substring(0,dotIdx);
					if(!TextUtils.isEmpty(pkgWithouVer))
						return pkgWithouVer;
				}catch (Exception e) {
					
				}
				return pkgName;
			} else {
				return pkgName;
			}
		} catch (Exception e) {
			return pkgName;
		}
	}

	/**
	 * 获取加载端包的file路径,如果不存在file路径,则创建
	 * 
	 * @Title: getRootApkFile
	 * @author lytjackson@gmail.com
	 * @date 2013-12-13
	 * @param ctx
	 * @return
	 */
	public static String getRootApkFile(Context ctx) {
		File root = ctx.getFilesDir();
		if (null == root) {
			root = new File("/data/data/" + ctx.getPackageName() + "/files");
		}
		if (!root.exists()) {
			root.mkdir();
		}
		return root.getAbsolutePath();
	}

	/**
	 * 清理插件，不保留数据
	 * 
	 * @Function: 
	 *            com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil.clearPluginDir
	 * @Description:
	 * @param ctx
	 * @param pluginPackageName
	 * 
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年8月7日 下午3:43:28
	 * 
	 */
	public static void clearPluginDir(Context ctx, String pluginPackageName) {
		String path = getRootApkFile(ctx) + "/" + pluginPackageName;
		File pluginFolder = new File(path);
		if (null != pluginFolder && pluginFolder.exists()) {
			FileUtil.delFolder(path);
		}
	}

	/**
	 * 清理插件旧版dex代码，保留旧数据
	 * 
	 * @Function: com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil.
	 *            clearPluginDirWithoutData
	 * @Description:
	 * @param ctx
	 * @param pluginPackageName
	 * 
	 * @version:v1.0
	 * @author:linyt
	 * @date:2014年8月7日 下午3:43:02
	 * 
	 */
/*	public static void clearPluginDirWithoutData(Context ctx, String pluginPackageName) {
		FileUtil.delFile(getRootApkFile(ctx) + "/" + pluginPackageName + "/" + pluginPackageName + ".dex");
	}*/

	/**
	 * 查询给定目录下插件JAR 包是否已经存在
	 * @param ctx
	 * @param pkgName
     * @return
     */
	public static boolean isPluginJarExisted(Context ctx, String folder, final String pkgName){

		if(TextUtils.isEmpty(folder) || TextUtils.isEmpty(pkgName))
			return false;

		File pluginFolder = new File(folder);
		//验证是否存在不同版本的插件jar
		File pluginVers[] = pluginFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				if(!TextUtils.isEmpty(s) && s.contains(pkgName))
					return true;
				return false;
			}
		});
		if (pluginVers != null && pluginVers.length > 0) {
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 查询动态插件的dex是否已经解压到file目录下
	 * 
	 * @Title: isPluginDexExisted
	 * @author lytjackson@gmail.com
	 * @date 2013-12-19
	 * @param ctx
	 * @param pluginPackageName
	 * @return
	 */
	public static boolean isPluginDexExisted(Context ctx, String pluginPackageName) {
		boolean isExisted = false;
		File dex = new File(getRootApkFile(ctx) + "/" + pluginPackageName + "/" + pluginPackageName + ".dex");
		if (null != dex && dex.exists()) {
			isExisted = true;
		}
		if (!isExisted && Build.VERSION.SDK_INT >= 26) {
			String old;
			String cpu = Build.CPU_ABI.toLowerCase();
			if (TextUtils.isEmpty(cpu)) {
				cpu = "armeabi";
			}
			old = cpu;
			if (cpu.startsWith("arm")) {
				cpu = "arm";
			}
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginPackageName + ".odex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginPackageName + ".vdex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}
			//实在找不到，再找全称下的
			cpu = old;
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginPackageName + ".odex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginPackageName + ".vdex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}
		}
		return isExisted;
	}

	/**
	 * 查询动态插件的dex是否已经解压到file目录下
	 * @param ctx
	 * @param pluginFolderName dex 文件夹名
	 * @param pluginDexName dex 文件名
     * @return
     */
	public static boolean isPluginDexExisted(Context ctx, String pluginFolderName,String pluginDexName) {
		boolean isExisted = false;
		File dex = new File(getRootApkFile(ctx) + "/" + pluginFolderName + "/" + pluginDexName + ".dex");
		if (null != dex && dex.exists()) {
			isExisted = true;
			Log.e("======","======dex:"+dex.toString());
		}
		if (!isExisted && Build.VERSION.SDK_INT >= 26) {
			String old;
			String cpu = Build.CPU_ABI.toLowerCase();
			if (TextUtils.isEmpty(cpu)) {
				cpu = "armeabi";
			}
			old=cpu;
			if (cpu.startsWith("arm")) {
				cpu = "arm64";
			}
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginDexName + ".odex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginDexName + ".vdex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}
			//实在找不到，再找全称下的
			cpu=old;
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginDexName + ".odex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}
			dex = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu + "/" + pluginDexName + ".vdex");
			if (null != dex && dex.exists()) {
				isExisted = true;
				return isExisted;
			}


		}
		return isExisted;
	}


	public static boolean isStringEmpty(String str) {
		return null == str || "".equals(str);
	}

	/**
	 * 动态插件是否已经启用
	 * 
	 * @Title: isPluginEnabled
	 * @author lytjackson@gmail.com
	 * @date 2014-4-8
	 * @param ctx
	 * @param pckageName
	 * @return
	 */
	public static boolean isPluginEnabled(Context ctx, String path, String packageName) {
		return isPluginEnabled(ctx, path, packageName, null);
	}

	/**
	 * 动态插件是否已经启用
	 * 支持多个包名查询
	 * @Title: isPluginEnabled
	 * @author lytjackson@gmail.com
	 * @date 2014-4-8
	 * @param ctx
	 * @param pckageNames[]
	 * @return
	 */
	public static boolean isPluginEnabled(Context ctx, String path, String packageName, String[] packageNames) {
		if (isStringEmpty(path) || isStringEmpty(packageName)) {
			return false;
		}
		if (checkPackageEnabled(ctx, path, packageName)) {
			return true;
		}
		if (null == packageNames || packageNames.length < 1) {
			return false;
		}
		for (int i = 0; i < packageNames.length; i++) {
			if (checkPackageEnabled(ctx, path, packageNames[i])) {
				return true;
			}
		}
		return false;
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
	public static String getPureNameWithVersion(String name) {
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

	private static boolean checkPackageEnabled(Context ctx, String path, final String packageName) {
		String plugin = path + packageName + ".jar";
		File apkFile = new File(plugin);
		if (apkFile.exists() && isPluginDexExisted(ctx, packageName)) {
			return true;
		}
		if(!isNeedRestart(packageName)){
			File pluginFolder = new File(path);
			//验证是否存在不同版本的插件
			File pluginVers[] = pluginFolder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String s) {
					if(!TextUtils.isEmpty(s) && s.contains(packageName))
						return true;
					return false;
				}
			});
			//TODO:xuqunxing 这块吃透
			if (pluginVers != null && pluginVers.length > 0) {
				for (int i = 0; i < pluginVers.length; i++) {
					File currentPlugin = pluginVers[i];
					if (currentPlugin.exists() && isPluginDexExisted(ctx, getPureNameWithVersion(currentPlugin.getName()), getPureNameWithVersion(currentPlugin.getName()))) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
