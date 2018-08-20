package com.nd.hilauncherdev.dynamic.Transfer;

import android.content.Context;

import com.felink.corelib.config.BaseConfig;
import com.nd.hilauncherdev.datamodel.DynamicConstant;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.kitset.util.DynamicPluginUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;

import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.nd.hilauncherdev.dynamic.Transfer.PluginTransferUtil.PluginInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype.FileType;

public class PluginParser {
	private static final String PLUGIN_INFO = "plugin/plugin_native.json";
	private static PluginParser instance;
	private String downloadPath = BaseConfig.WIFI_DOWNLOAD_PATH;
	private Context mContext;
	/**
	 * 内置的插件文件名
	 */
	private List<String> allInnerPluginFileNames = new ArrayList<String>();
	/**
	 * 桌面所有动态插件包名
	 */
	private List<String> allPluginApkNames = new ArrayList<String>();

	private PluginParser(Context ctx) {
		mContext = ctx;
		getPluginInfoFromFile();
	}

	public synchronized static PluginParser getInstance(Context ctx) {
		if (null == instance) {
			instance = new PluginParser(ctx);
		}
		return instance;
	}

	/**
	 * 获取内置的插件文件名列表
	 * 
	 * @return
	 */
	public List<String> getAllInnerPluginPkgName() {
		return allInnerPluginFileNames;
	}

	private boolean update(List<PluginUpgradeInfo> datas) {
		if (null == datas || datas.size() <= 0) {
			return false;
		}
		ArrayList<BaseDownloadInfo> infos = new ArrayList<BaseDownloadInfo>();
		PluginUpgradeInfo plugin;
		for (int i = 0; i < datas.size(); i++) {
			plugin = datas.get(i);
			if (plugin.isNative) {
				continue;
			}
			// CommonGlobal.PLUGIN_DIR + plugin.pkg + ".jar";
			String installPath = PluginLoaderUtil.assembleInstallPath(plugin);
			File apkFile = new File(installPath);
			if (apkFile.exists() && DynamicPluginUtil.isPluginDexExisted(mContext, plugin.pkg)) {
				int installedVer = PluginTransferUtil.getRightVersionCode(mContext, installPath);
				if (plugin.version > installedVer) {
					// 加入wifi
					assemblePluginDownloadInfo(infos, plugin);
				}
			} else {
				PluginInfo pluginInfo = PluginTransferUtil.checkHasPlugin(mContext, DynamicConstant.WIFI_DOWNLOAD_PATH, plugin.pkg);
				if (plugin.version > pluginInfo.pluginVersion) {
					assemblePluginDownloadInfo(infos, plugin);
				}
			}
		}

		if (null != infos && infos.size() > 0) {
			DownloadManager.getInstance(mContext).addSilent23GTask(infos, false);
			return true;
		}
		return false;
	}

/*	public boolean updatePlugin(String pkg) {
		PluginUpgradeInfo p = PluginUpgrader.getInstance().getPluginUpgradeInfo(pkg);
		if(p == null)
			return false;
		List<PluginUpgradeInfo> list = new ArrayList<PluginUpgradeInfo>();
		list.add(p);
		return update(list);
	}*/

	/**
	 * 读取插件配置信息，进行wifi环境下载
	 */
	public boolean updatePlugin() {
		return update(PluginUpgrader.getInstance().getAllPluginUpgradeInfo(allPluginApkNames));
	}

	private void assemblePluginDownloadInfo(ArrayList<BaseDownloadInfo> infos, PluginUpgradeInfo plugin) {
		String url = PluginTransferUtil.getPluginDownloadUrl(mContext, plugin);
		if(StringUtil.isEmpty(url))
			return;
		BaseDownloadInfo dlInfo = new BaseDownloadInfo(plugin.pkg, FileType.FILE_DYNAMIC_APK.getId(), url, plugin.pkg, getDownloadPath(), plugin.pkg + ".jar", "");
		infos.add(dlInfo);
	}

	/**
	 * 解析插件信息，获取所有动态插件包名和安装包带有的动态插件包名
	 */
	private void getPluginInfoFromFile() {
		try {
			allPluginApkNames.clear();
			allInnerPluginFileNames.clear();
			
			InputStream is = mContext.getAssets().open(PLUGIN_INFO);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			JSONObject allStr = new JSONObject(sb.toString());
			JSONArray pluginArray = allStr.getJSONArray("plugin");
			final int num = pluginArray.length();
			for (int i = 0; i < num; i++) {
				JSONObject pluginObj = (JSONObject) pluginArray.get(i);
				allPluginApkNames.add(pluginObj.getString("pkg"));
				if(pluginObj.has("file")){//本地包自带插件
					allInnerPluginFileNames.add(pluginObj.getString("file"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDownloadPath() {
		return downloadPath;
	}

/*	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}*/
}
