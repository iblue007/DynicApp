package com.nd.hilauncherdev.dynamic.Transfer;

import android.content.Context;
import android.util.Log;

import com.felink.corelib.config.BaseConfig;
import com.felink.corelib.config.BaseConfigPreferences;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.ThreadUtil;
import com.felink.corelib.kitset.URLs;
import com.felink.corelib.net.VideopaperHttpRequestParam;
import com.felink.corelib.net.base.HttpCommon;
import com.felink.corelib.net.base.NetworkAccess;
import com.felink.corelib.net.base.ServerResultHeader;
import com.nd.hilauncherdev.datamodel.DynamicConstant;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 动态插件升级
 *
 * @author guojianyun
 */
public class PluginUpgrader implements OnLauncherStartListener {
    private static PluginUpgrader mPluginUpgrader = new PluginUpgrader();
    /**
     * 动态插件版本配置文件路径
     */
    private String configFilePath;
    private final String assetsFilePath = "plugin/plugin_upgrade.json";

    private static final int GET_INFO_ERROR_CODE = 5016001;//获取信息失败
    private static final int IS_LATEST_VERSION_CODE = 5016002;//已是最新版本

    private PluginUpgrader() {
        configFilePath = BaseConfig.getApplicationDataPath() + "/files/plugin_upgrade_new.json";
    }

    public static PluginUpgrader getInstance() {
        return mPluginUpgrader;
    }

    @Override
    public int getType() {
        return OnLauncherStartListener.TYPE_ONCE_A_DAY;
    }

    @Override
    public void onLauncherStart(Context ctx) {
        updatePluginConfig();
    }

    public void updatePluginConfig() {
        //TODO:xuqunxing
        // 更新动态插件配置信息
		ThreadUtil.executeOther(new Runnable() {
			@Override
			public void run() {
				NetworkAccess.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("ver", BaseConfigPreferences.getInstance(com.felink.corelib.config.Global.getContext()).getPluginUpgradeConfigVersion());
							jsonObject.put("configName", DynamicConstant.DYNAMIC_PARAM_NAME);
                            String jsonParams = jsonObject.toString();
							HashMap<String, String> paramsMap = new HashMap<String, String>();
                            VideopaperHttpRequestParam.addGlobalLauncherRequestValue(paramsMap, jsonParams);
							HttpCommon httpCommon = new HttpCommon(URLs.DYN_PLUGIN_UPGRADE_URL);
							ServerResultHeader csResult = httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
							if (!csResult.isRequestOK() || csResult.getResultCode() == GET_INFO_ERROR_CODE
									|| csResult.getResultCode() == IS_LATEST_VERSION_CODE) {
								return;
							}
							String configStr = csResult.getResponseJson();
                            BaseConfigPreferences.getInstance(Global.getContext()).setPluginUpgradeConfigVersion(new JSONObject(configStr).getInt("version"));
							writePluginConfigs(configStr);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
    }


    /**
     * 获取插件最新版本信息
     * 查找符合桌面版本要求的最高版本插件
     *
     * @param pkgName 插件包名
     * @return 返回null表示无插件升级信息
     */
    public PluginUpgradeInfo getPluginUpgradeInfo(String pkgName) {
        if (StringUtil.isEmpty(pkgName))
            return null;

        PluginUpgradeInfo info = getPluginUpgradeInfo(readPluginConfigs(), pkgName);
        if (info == null) {//从assets文件里读取插件升级信息
            info = getPluginUpgradeInfo(readPluginConfigsFromAssets(), pkgName);
            Log.e("PluginUpgrader", "read config from assets!");
        }
        return info;
    }

    /**
     * 查找所有插件升级信息
     *
     * @param pkgNames
     * @return
     */
    public List<PluginUpgradeInfo> getAllPluginUpgradeInfo(List<String> pkgNames) {
        if (pkgNames == null)
            return null;

        List<PluginUpgradeInfo> list = new ArrayList<PluginUpgradeInfo>();
        for (String pkgName : pkgNames) {
            PluginUpgradeInfo info = getPluginUpgradeInfo(pkgName);
            if (info != null) {
                list.add(info);
            }
        }
        return list;
    }

    /**
     * Description: 获取插件最新版本信息
     * Author: guojianyun_91
     * Date: 2015年4月9日 下午2:26:09
     *
     * @param jsonObj
     * @param pkgName
     * @return
     */
    private PluginUpgradeInfo getPluginUpgradeInfo(JSONObject jsonObj, String pkgName) {
        if (jsonObj == null)
            return null;
        try {
            JSONArray array = jsonObj.getJSONArray("plugin");
            int len = array.length();
            PluginUpgradeInfo info = null;
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                if (pkgName.equals(obj.getString("pkg"))) {
                    info = new PluginUpgradeInfo();
                    info.setPkgName(pkgName);
                    info.setVersion(obj.getInt("version"));
                    info.setType(obj.getInt("type"));
                    info.setTargetVersion(obj.getInt("targetVersion"));
                    info.setDownloadPath(obj.getString("downloadfile"));
                    info.setDownloadType(obj.optString("downloadtype"));
                    info.setMd5Value(obj.optString("md5"));
                    info.setNeedRestart(obj.optInt("needRestart", PluginUpgradeInfo.NEED_NOT_RESTART));
                    info.setCurrentState(obj.optInt("state", PluginUpgradeInfo.STATE_ENABLE));
                    return info;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description: 从assets文件里读取插件升级信息
     * Author: guojianyun_91
     * Date: 2015年4月9日 下午2:19:03
     *
     * @return
     */
    private JSONObject readPluginConfigsFromAssets() {
        try {
            return new JSONObject(FileUtil.readAssetsContent(Global.getApplicationContext(), assetsFilePath));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新插件配置信息
     *
     * @param str
     */
    private void writePluginConfigs(String str) {
        FileUtil.writeFile(configFilePath, str, false);
    }


    /**
     * 读取插件配置信息
     *
     * @return
     * @throws Exception
     */
    private JSONObject readPluginConfigs() {
        try {
            File file = new File(configFilePath);
            boolean isExist = file.exists();
            JSONObject mJSONObject = null;
            if (!isExist) {
                createConfigFileFromAsset();
                mJSONObject = new JSONObject(FileUtil.readFileContent(configFilePath));
                BaseConfigPreferences.getInstance(Global.getContext()).setPluginUpgradeConfigVersion(mJSONObject.getInt("version"));
            } else {
                mJSONObject = new JSONObject(FileUtil.readFileContent(configFilePath));
            }
            return mJSONObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createConfigFileFromAsset() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(Global.getApplicationContext().getAssets().open(assetsFilePath)));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            FileUtil.writeFile(configFilePath, sb.toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
