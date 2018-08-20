package com.felink.corelib.analytics;

import android.text.TextUtils;
import android.util.Log;

/**
 * 视频下载统计工具类
 * Created by linliangbin on 2017/7/4 11:06.
 */

public class VideoDownloadReport {

    /**
     * @desc 上报单个视频的统计
     * @author linliangbin
     * @time 2017/7/4 11:06
     */
    public static void submitDownloadStart(String resId,String themeSp) {
        submitDownloadStatistic(resId, VideoDownloadPathAnalysis.FLAG_DOWNLOAD_Video,themeSp);
    }

    public static void submitDownloadSuccess(String resId,String themeSp) {
        submitDownloadStatistic(resId, VideoDownloadPathAnalysis.FLAG_DOWNLOAD_SUCCESS,themeSp);
    }

    public static void submitDownloadFail(String resId,String themeSp) {
        submitDownloadStatistic(resId, VideoDownloadPathAnalysis.FLAG_DOWNLOAD_FAIL,themeSp);
    }

    public static void submitInstallSuccess(String resId,String themeSp) {
        submitDownloadStatistic(resId, VideoDownloadPathAnalysis.FLAG_INSTALL_SUCCESS,themeSp);
    }

    public static void submitInstallFail(String resId,String themeSp) {
        submitDownloadStatistic(resId, VideoDownloadPathAnalysis.FLAG_INSTALL_FAIL,themeSp);
    }


    public static void submitDownloadStatistic(String resId, int state,String themeSp) {
        try {
            if(themeSp.equals("0") || TextUtils.isEmpty(themeSp)){
                themeSp = "99";
            }
            Log.e("======","======submitDownloadStatistic-themeSp:"+themeSp+"---state:"+state+"--id："+resId);
            VideoDownloadPathAnalysis.sendVideoDownloadPathAnalysisSP(
                    resId,
                    VideoDownloadPathAnalysis.RES_TYPE_VIDEO_PAPER,
                    themeSp,//VideoDownloadPathAnalysis.SP_UNKNOWN + ""
                    state,
                    VideoDownloadPathAnalysis.POSTION_TYPE_DEFAULT,
                    VideoDownloadPathAnalysis.POSTION_TYPE_DEFAULT,
                    VideoDownloadPathAnalysis.RES_ATTRIBUTES_FREE
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
