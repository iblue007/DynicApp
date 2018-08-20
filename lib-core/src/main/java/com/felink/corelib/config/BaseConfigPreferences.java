package com.felink.corelib.config;

import android.content.Context;
import android.content.SharedPreferences;

public class BaseConfigPreferences {

	public static final String NAME = "configsp";

	private static BaseConfigPreferences baseConfig;
	private static SharedPreferences baseSP;

	/**
	 * 第一次启动时间
	 */
	private static final String KEY_FIRST_LAUNCH_TIME = "first_launch_time";

	//用户第一次安装的版本
	private static final String KEY_VERSION_FROM = "is_resident";

	//是否已经弹过，拍摄按钮的引导
	private static final String KEY_HAVE_SHOW_GUIDE = "key_have_show_guide";

	/**
	 * 是否系统音量改变
	 */
	public static final String IS_SYSTEM_VOLUME_CHANGE = "is_system_volume_change";


	/**
	 * 首页推荐视频壁纸音乐是否开启
	 */
	public static final String IS_VIDEO_WALLPAPER_VOLUMN_ENABLE = "is_video_wallpaper_volumn_enable";

	/**
	 * 预览视频音乐是否开启
	 */
	public static final String IS_VIDEO_PREVIEW_VOLUMN_ENABLE = "is_video_preview_volumn_enable";

	/**
	 * 是否是第一次进入裁剪页面
	 */
	public static final String IS_FIRST_VIDEO_CLIP = "is_first_video_clip";

	/**
	 * 动态插件升级配置版本号(视频壁纸3.1使用)
	 */
	private static final String KEY_PLUGIN_UPGRADE_CONFIG_VERSION_2318 = "key_plugin_upgrade_version_2318";


	protected BaseConfigPreferences(Context context) {
		baseSP = context.getSharedPreferences(NAME, Context.MODE_PRIVATE | 4);
	}

	public SharedPreferences getBaseSP() {
		return baseSP;
	}

	public static BaseConfigPreferences getInstance(Context context) {
		if(null == baseConfig) {
			baseConfig = new BaseConfigPreferences(context);
		}
		return baseConfig;
	}

	/**
	 * 获取第一次使用的时间
	 * @return 时间
	 */
	public long getFirstLaunchTime() {
		long current = System.currentTimeMillis();
		long time = baseSP.getLong(KEY_FIRST_LAUNCH_TIME, current);
		if(current == time) {
			setFirstLaunchTime(current);
		}
		return time;
	}

	/**
	 * 设置第一次使用的时间
	 * @param time 时间
	 */
	public void setFirstLaunchTime(long time) {
		baseSP.edit().putLong(KEY_FIRST_LAUNCH_TIME, time).commit();
	}

	/**
	 * 某个版本是否已被启动过
	 * @param currentVer 版本
	 * @return true表示已被启动过
	 */
	public boolean isVersionShowed(String currentVer) {
		return baseSP.getBoolean(currentVer, false);
	}

	/**
	 * 设置某个版本是否被启动过
	 * @param version 版本
	 * @param showed true已启动过/false还未启动过
	 */
	public void setVersionShowed(String version, boolean showed) {
		baseSP.edit().putBoolean(version, showed).commit();
	}

	/**
	 * 获取某个版本是否被启动过
	 * @param versionCode 版本号
	 */
	public boolean isVersionCodeShowed(int versionCode) {
		return baseSP.getBoolean("#"+ String.valueOf(versionCode), false);
	}

	/**
	 * 设置某个版本是否被启动过
	 * @param versionCode 版本号
	 * @param showed true已启动过/false还未启动过
	 */
	public void setVersionCodeShowed(int versionCode, boolean showed) {
		baseSP.edit().putBoolean("#"+ String.valueOf(versionCode), showed).commit();
	}

	/**
	 * 获取新增用户时记录的版本号
	 * @Title: getVersionCodeForResident
	 * @author lytjackson@gmail.com
	 * @date 2014-3-31
	 * @return
	 */
	public int getVersionCodeFrom() {
		return baseSP.getInt(KEY_VERSION_FROM, -1);
	}

	/**
	 * 新增时记录版本号
	 * @Title: setVersionCodeForResident
	 * @author lytjackson@gmail.com
	 * @date 2014-3-31
	 * @param versionCode
	 */
	public void setVersionCodeFrom(int versionCode) {
		int vcFrom = baseSP.getInt(KEY_VERSION_FROM, 0);
		if (vcFrom <= 0) {
			baseSP.edit().putInt(KEY_VERSION_FROM, versionCode).commit();
		}
	}


	/**
	 * 是否已经显示过引导
	 * */
	public boolean getHaveShowGuide() {
		return baseSP.getBoolean(KEY_HAVE_SHOW_GUIDE, false);
	}

	public void setHaveShowGuide(boolean have) {
		baseSP.edit().putBoolean(KEY_HAVE_SHOW_GUIDE, have).commit();
	}


	/**
	 * 系统音量是否改变
	 *
	 * @return
	 */
	public boolean isSystemVolumeChanged() {
		return baseSP.getBoolean(IS_SYSTEM_VOLUME_CHANGE, false);
	}

	/**
	 * 设置系统音量已经改变
	 *
	 * @param value
	 */
	public void setSystemVolumeChanged(boolean value) {
		baseSP.edit().putBoolean(IS_SYSTEM_VOLUME_CHANGE, value).commit();
	}



	/**
	 * 视频壁纸音乐是否开启
	 *
	 * @return
	 */
	public boolean isVideoWallpaperVolumnEnable() {
		return baseSP.getBoolean(IS_VIDEO_WALLPAPER_VOLUMN_ENABLE, false);
	}

	/**
	 * 设置视频壁纸音乐是否开启
	 *
	 * @param value
	 */
	public void setVideoWallpaperVolumnEnable(boolean value) {
		baseSP.edit().putBoolean(IS_VIDEO_WALLPAPER_VOLUMN_ENABLE, value).commit();
	}


	/**
	 * 视频壁纸预览音乐是否开启
	 *
	 * @return
	 */
	public boolean isVideoPreviewVolumnEnable() {
		return baseSP.getBoolean(IS_VIDEO_WALLPAPER_VOLUMN_ENABLE, false);
	}

	/**
	 * 设置视频壁纸预览音乐是否开启
	 *
	 * @param value
	 */
	public void setVideoPreviewVolumnEnable(boolean value) {
		baseSP.edit().putBoolean(IS_VIDEO_WALLPAPER_VOLUMN_ENABLE, value).commit();
	}


	/**
	 * 是否是第一次进入裁剪页面
	 * @return
	 */
	public boolean isFirstVideoClip() {
		return baseSP.getBoolean(IS_FIRST_VIDEO_CLIP, true);
	}

	public void setIsFirstVideoClip(boolean value) {
		baseSP.edit().putBoolean(IS_FIRST_VIDEO_CLIP, value).commit();
	}

	/**
	 * 获取动态插件升级配置版本号
	 * @return
	 */
	public int getPluginUpgradeConfigVersion(){
		return baseSP.getInt(KEY_PLUGIN_UPGRADE_CONFIG_VERSION_2318, 1);
	}

	/**
	 * 设置动态插件升级配置版本号
	 * @param version
	 */
	public void setPluginUpgradeConfigVersion(int version){
		baseSP.edit().putInt(KEY_PLUGIN_UPGRADE_CONFIG_VERSION_2318, version).commit();
	}
}
