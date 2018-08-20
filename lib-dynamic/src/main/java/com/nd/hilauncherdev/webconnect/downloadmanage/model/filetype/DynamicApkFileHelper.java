package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import android.content.Context;

import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
//TODO:zhou 下载管理后，打开类态插件类型的文件实现器，因国没有下载管理界面所以暂时不用
public class DynamicApkFileHelper implements IFileTypeHelper {
    public static final String EXTRAS_WIDGET_TYPE = "extras_widget_type";
    public static final String EXTRAS_WIDGET_POS_TYPE = "extras_widget_pos_type";
//    @Override
//    public void onClickWhenFinished(Context context, ViewHolder viewHolder, BaseDownloadInfo downloadInfo) {
//        
//    }

    @Override
    public String getItemTextWhenFinished(BaseDownloadInfo downloadInfo) {
        return null;
    }

    @Override
    public void onDownloadCompleted(Context context, BaseDownloadInfo downloadInfo, String file) {

    }

    @Override
    public String getItemDefIconPath(BaseDownloadInfo downloadInfo) {
        return null;
    }

    @Override
    public boolean fileExists(BaseDownloadInfo downloadInfo) {
        return false;
    }
}