package com.nd.hilauncherdev.webconnect.downloadmanage.model;

import android.text.TextUtils;

import com.felink.corelib.analytics.AnalyticsConstant;
import com.felink.corelib.analytics.HiAnalytics;
import com.felink.corelib.analytics.VideoDownloadReport;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.DownloadUtil;

public class DownloadCallback extends AbstractDownloadCallback {

    @Override
    public void onBeginDownload(BaseDownloadInfo downloadInfo) {
        if (downloadInfo != null) {
            if (!TextUtils.isEmpty(downloadInfo.getAdditionInfo().toString())) {
                String videoFromType1 = DownloadUtil.getVideoFromType(downloadInfo.getAdditionInfo().toString());
                String videoId = DownloadUtil.getVideoId(downloadInfo.getAdditionInfo().toString());
                if (!TextUtils.isEmpty(videoFromType1) && !TextUtils.isEmpty(videoId)) {
                    VideoDownloadReport.submitDownloadStart(videoId + "", videoFromType1 + "");
                }
            }
            HiAnalytics.submitEvent(Global.getContext(), AnalyticsConstant.VIDEO_DOWNLOAD,"start");
            return;
        }

    }

    @Override
    public void onDownloadCompleted(final BaseDownloadInfo downloadInfo, boolean fileExist) {
        if (downloadInfo != null) {
            if (!TextUtils.isEmpty(downloadInfo.getAdditionInfo().toString())) {
                String videoFromType1 = DownloadUtil.getVideoFromType(downloadInfo.getAdditionInfo().toString());
                String videoId = DownloadUtil.getVideoId(downloadInfo.getAdditionInfo().toString());
                if (!TextUtils.isEmpty(videoFromType1) && !TextUtils.isEmpty(videoId)) {
                    VideoDownloadReport.submitDownloadSuccess(videoId + "", videoFromType1 + "");
                }
            }
            HiAnalytics.submitEvent(Global.getContext(), AnalyticsConstant.VIDEO_DOWNLOAD,"succ");
            return;
        }
    }

    @Override
    public void onDownloadFailed(BaseDownloadInfo downloadInfo, Exception e) {
        if (downloadInfo != null) {
            if (!TextUtils.isEmpty(downloadInfo.getAdditionInfo().toString())) {
                String videoFromType1 = DownloadUtil.getVideoFromType(downloadInfo.getAdditionInfo().toString());
                String videoId = DownloadUtil.getVideoId(downloadInfo.getAdditionInfo().toString());
                if (!TextUtils.isEmpty(videoFromType1) && !TextUtils.isEmpty(videoId)) {
                    VideoDownloadReport.submitDownloadFail(videoId + "", videoFromType1 + "");
                }
            }
            HiAnalytics.submitEvent(Global.getContext(), AnalyticsConstant.VIDEO_DOWNLOAD,"fail");
            return;
        }
    }


    @Override
    public void onDownloadWorking(BaseDownloadInfo downloadInfo) {

    }

    @Override
    public void onHttpReqeust(BaseDownloadInfo downloadInfo, int requestType) {

    }

}
