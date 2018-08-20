package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import android.content.Context;

//import com.nd.hilauncherdev.webconnect.downloadmanage.activity.DownloadAdapter.ViewHolder;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

import java.io.Serializable;

/**
 * 用于处理不同类型的下载项在下载管理界面被点击时的动作
 */
public interface IFileTypeHelper extends Serializable {

	/**
	 * 当下载状态为DownloadState.STATE_FINISHED时，点击DownloadManageActivity上的item时执行的动作
	 */
	//TODO:zhou 因为没有下载管理界面所以暂时用不到
	//public void onClickWhenFinished(Context context, ViewHolder viewHolder, BaseDownloadInfo downloadInfo);
	
	/**
	 * 当下载状态为DownloadState.STATE_FINISHED时，显示在DownloadManageActivity的item上的文字
	 */
	public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo); 
	
	/**
	 * 下载完成时，调用该方法
	 */
	public void onDownloadCompleted(Context context, BaseDownloadInfo downloadInfo, String file); 
	
	/**
	 * 默认显示在DownloadManageActivity的item上的icon
	 */
	public String getItemDefIconPath(BaseDownloadInfo downloadInfo);
	
	/**
	 * 文件是否已经存在
	 */
	public boolean fileExists(BaseDownloadInfo downloadInfo);
}
