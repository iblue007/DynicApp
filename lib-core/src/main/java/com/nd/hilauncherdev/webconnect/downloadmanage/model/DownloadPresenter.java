package com.nd.hilauncherdev.webconnect.downloadmanage.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.felink.corelib.kitset.ThreadUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadBroadcastExtra;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

/**
 * Description: </br>
 * Author: cxy
 * Date: 2017/1/12.
 */

public class DownloadPresenter {

    private IDownloadStateView iView;

    private DownloadReceiver mReceiver;

    private IdentifyFilter mFilter;

    public interface IdentifyFilter {
        boolean filter(String identify);
    }

    public DownloadPresenter(IDownloadStateView view) {
        this.iView = view;
    }

    public void addTask(final Context context, final String identification, final int fileType, final String downloadUrl, final String title, final String savedDir, final String savedName, final String iconPath) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                BaseDownloadInfo baseDownloadInfo = new BaseDownloadInfo(identification, fileType, downloadUrl, title, savedDir, savedName, iconPath);
                DownloadManager.getInstance(context).addNormalTask(baseDownloadInfo, null);
            }
        });
    }

    public void addTask(final Context context, final BaseDownloadInfo baseDownloadInfo) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                DownloadManager.getInstance(context).addNormalTask(baseDownloadInfo, null);
            }
        });
    }

    public void register(Context context, IdentifyFilter filter) {
        try {
            if (mReceiver == null) {
                mReceiver = new DownloadReceiver(iView, filter);
            }

            mFilter = filter;

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_STATE);
            context.registerReceiver(mReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregister(Context context) {
        try {
            if (mReceiver != null) {
                context.unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class DownloadReceiver extends BroadcastReceiver {

        private IDownloadStateView hookView;
        private IdentifyFilter hookFilter;

        public DownloadReceiver(IDownloadStateView hook, IdentifyFilter filter) {
            hookView = hook;
            hookFilter = filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            String id = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_IDENTIFICATION);
          //  Log.e("========","========id:"+id+"--hookFilter:"+hookFilter);
            if (hookFilter != null && !hookFilter.filter(id)) {
                return;
            }
            int progress = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_PROGRESS, 0);
            int state = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_STATE, DownloadState.STATE_NONE);
            String url = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_DOWNLOAD_URL);

            if (hookView != null) {
                if (state == DownloadState.STATE_WAITING) {
                    hookView.onDLWaitting(id, url);
                } else if (state == DownloadState.STATE_START) {
                    hookView.onDLStart(id, url);
                } else if (state == DownloadState.STATE_PAUSE) {
                    hookView.onDLPause(id, url);
                } else if (state == DownloadState.STATE_CANCLE) {
                    hookView.onDLCancel(id, url);
                } else if (state == DownloadState.STATE_FAILED) {
                    hookView.onDLFailed(id, url);
                } else if (state == DownloadState.STATE_FINISHED) {
                    hookView.onDLFinished(id, url);
                } else if (state == DownloadState.STATE_DOWNLOADING) {
                    hookView.onDLDownloading(progress, id, url);
                }
            }
        }
    }
}
