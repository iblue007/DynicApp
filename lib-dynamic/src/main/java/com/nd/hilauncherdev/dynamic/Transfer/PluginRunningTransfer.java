package com.nd.hilauncherdev.dynamic.Transfer;

import android.content.Context;
import android.content.Intent;

import com.nd.hilauncherdev.datamodel.DynamicConstant;
import com.nd.hilauncherdev.dynamic.PluginLoaderActivity;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.widget.shop.util.WidgetPositionType;
import com.nd.hilauncherdev.widget.shop.util.WidgetType;

/**
 * <p>
 * 类说明： 为动态插件安装, 加载, 启动阶段的提简化统一的服务入口
 * </p>
 * <p>
 * 创建时间：2013-11-4 下午04:37:32
 * </p>
 * 
 * @author yuf
 * @version 1.0
 */
public class PluginRunningTransfer implements ITransfer {
	private Context mContext;
	private PluginTransferListener mListener;

	public PluginRunningTransfer(Context context) {
		this.mContext = context;
		mListener = new DefaultPluginTransferListener();
	}

	public void setPluginTransferListener(PluginTransferListener listener) {
		mListener = listener;
	}

	public PluginState getPluginState(String pkgName, String fileName, String[] usedNames, PluginUpgradeInfo info, boolean isNeedRestart) {
		return PluginTransferUtil.getPluginStateDefaultImpl(mContext, DynamicConstant.PLUGIN_DIR, pkgName, fileName, usedNames, info, isNeedRestart);
	}

	public void transfer(PluginState pluginState, String pkgName, String installName, Intent intent) {
		try {
			String dexpath = DynamicConstant.PLUGIN_DIR + installName;
			switch (pluginState) {
			case OUT_OF_SYNC_FOR_INNER:
			case INNER_PLUGIN:
				PluginLoaderUtil.enablePlugin(mContext, WidgetType.MYPHONE_TYPE, WidgetPositionType.OFFLINE_TYPE, pkgName, installName, mListener);
				PluginLoaderActivity.startPluginLoaderActivity(mContext, dexpath, pkgName, intent, 0, false);
				break;
			case OUT_OF_SYNC_FOR_OUTTER:
			case OUTTER_PLUGIN:
				//TODO:xuqnxing  第二步
				PluginLoaderUtil.enablePlugin(mContext, WidgetType.MYPHONE_TYPE, WidgetPositionType.ONLINE_WIFI_TYPE, "", installName, mListener);
				PluginLoaderActivity.startPluginLoaderActivity(mContext, dexpath, pkgName, intent, 0, false);
				break;
			case NORMAL: // 运行apk中的Activity
			case NEED_UPGRADE:
				mListener.onRunning(mContext);
				PluginLoaderActivity.startPluginLoaderActivity(mContext, dexpath, pkgName, intent, 0, false);
				break;
			case NEED_DOWNLOAD:
				break;
			case ERROR:
				mListener.onTransferError(mContext, -1);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			mListener.onTransferError(mContext, -1);
			e.printStackTrace();
		}
	}

}
