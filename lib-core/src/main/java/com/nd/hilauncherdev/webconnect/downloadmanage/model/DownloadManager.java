package com.nd.hilauncherdev.webconnect.downloadmanage.model;

import android.content.Context;

import com.nd.hilauncherdev.framework.db.AbstractDataBase;
import com.nd.hilauncherdev.framework.db.BaseDataBase;

import java.util.HashMap;

public class DownloadManager extends AbstractDownloadManager {

    /**
     * apk应用下载广播通知
     */
    public static final String ACTION_DOWNLOAD_STATE = "com.felink.android.okeyboard_APK_DOWNLOAD_STATE";
    /**
     * 打开下载管理界面
     */
    public static final String ACTION_SHOW = "com.nd.android.pandahome2.downloadmanager.SHOW";

    private static DownloadManager sInstance = null;

    private static HashMap<String, String> sId2Path = new HashMap<String, String>();

    public static DownloadManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DownloadManager(context);
        }
        return sInstance;
    }

    private DownloadManager(Context context) {
        super(context);
    }

    @Override
    protected Class<? extends AbstractDataBase> getDownloadDb() {
        return BaseDataBase.class;
    }

    @Override
    protected Class<? extends AbstractDownloadCallback> getDownloadCallback() {
        return DownloadCallback.class;
    }

    @Override
    protected String getBroadcastAction() {
        return ACTION_DOWNLOAD_STATE;
    }

    @Override
    public void addNormalTask(BaseDownloadInfo info, ResultCallback callback) {
        super.addNormalTask(info, callback);
        putId2Path(info);
    }

    @Override
    public void addSilentTask(BaseDownloadInfo info) {
        super.addSilentTask(info);
        putId2Path(info);
    }

    private void putId2Path(BaseDownloadInfo info) {
        if (info != null) {
            sId2Path.put(info.getIdentification(), info.getFilePath());
        }
    }

    public String getPathById(String identifier) {
        return sId2Path.get(identifier);
    }

    public void delPathFromId(String identifier) {
        sId2Path.remove(identifier);
    }
}
