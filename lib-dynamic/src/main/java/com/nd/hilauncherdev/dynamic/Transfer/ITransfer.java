package com.nd.hilauncherdev.dynamic.Transfer;

import android.content.Context;
import android.content.Intent;



/**
 * <p>类说明： 动态插件中转工具接口 </p>
 * <p>创建时间：2013-12-24 下午04:54:58</p>
 * @author yuf
 * @version 1.0
 */
public interface ITransfer {

	public enum PluginState {
		/** 
		 * 插件安装状态正常, 可以直接运行 
		 */
		NORMAL,
		/** 
		 * 安装包与已安装到系统的插件不同步, 需要重新安装 
		 */
		OUT_OF_SYNC_FOR_OUTTER,
		/** 
		 * 安装包与已安装到系统的插件不同步, 需要重新安装 
		 */
		OUT_OF_SYNC_FOR_INNER,
		/** 
		 * 插件需要下载
		 */
		NEED_DOWNLOAD,
		/** 
		 * 插件需要更新
		 */
		NEED_UPGRADE,
		/**
		 *  wifi下载完毕，并且移动到插件目录，可启用状态
		 */
		OUTTER_PLUGIN,
		/** 
		 * 内置插件
		 */
		INNER_PLUGIN,
		/**
		 * 插件需升级桌面才能使用
		 */
		NEED_UPGRADE_LAUNCHER,
		/** 
		 * 运行时出错 
		 */
		ERROR,
	}

	public PluginState getPluginState(String assetPartName, String installName, String[] usedNames, PluginUpgradeInfo info, boolean isNeedRestart);

	/**
	 * <p>
	 * 说明: 跳转到动态插件中的Activity
	 * </p>
	 * 
	 * @author Yu.F, 2013-11-4
	 * @param assetPartName
	 *            动态插件在asset文件夹下的部分名称(前缀, 不包含版本号与md5, 如 "MyFile_V_")
	 * @param installName
	 *            动态插件安装(复制到sdcard并解压)后的名称, 如: MyFile.jar
	 * @param intent
	 *            动态插件入口intent, 同时用于传递启动参数(intent.getXXXExtra...), 如果不指定,
	 *            则默认入口为manisfest中指定的
	 * @param hasSo
	 *            指示动态插件是否包含so文件, 如果是则将附带安装so
	 */
	/**
	 * 
	 * @Title: transfer
	 * @author lytjackson@gmail.com
	 * @date 2014-3-31
	 * @param pluginState 插件安装状态
	 * @param assetPartName 
	 * @param installName
	 * @param intent
	 */
	public void transfer(PluginState pluginState, String assetPartName, String installName, Intent intent);

	public void setPluginTransferListener(PluginTransferListener listener);

	public interface PluginTransferListener {
		/**
		 * <p>
		 * 说明: 准备阶段, 即将复制apk到SD卡中, 为解压安装apk到系统为准备
		 * </p>
		 * 
		 * @author Yu.F, 2013-11-25
		 */
		public void onPreparing(Context context);

		/**
		 * <p>
		 * 说明: 安装阶段(在onPreparing方法执行完成之后调用), 即将解压安装apk文件到系统, 为运行插件apk作准备
		 * </p>
		 * 
		 * @author Yu.F, 2013-11-25
		 */
		public void onInstalling(Context context);

		/**
		 * <p>
		 * 说明: 安装成功阶段(在onInstalling方法执行完成之后调用), 插件启用成功,可运行
		 * </p>
		 * 
		 * @author Yu.F, 2013-11-25
		 */
		public void onInstallSuc(Context context);

		/**
		 * <p>
		 * 说明: 安装阶段(在onInstallSuc方法执行完成之后调用), 即将运行运行插件apk
		 * </p>
		 * 
		 * @author Yu.F, 2013-11-25
		 */
		public void onRunning(Context context);

		/**
		 * <p>
		 * 说明: 在跳转过程中发生错误时, 将调用此方法
		 * </p>
		 * 
		 * @author Yu.F, 2013-11-25
		 */
		public void onTransferError(Context context, int code);

		public void onRefreshState(Context context, int progress);
	}
}
