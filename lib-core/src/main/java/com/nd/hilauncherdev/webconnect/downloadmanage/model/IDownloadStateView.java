package com.nd.hilauncherdev.webconnect.downloadmanage.model;

/**
 * Description: </br>
 * Author: cxy
 * Date: 2017/1/12.
 */

public interface IDownloadStateView {

    void onDLWaitting(String id, String downloadUrl);

    void onDLStart(String id, String downloadUrl);

    void onDLCancel(String id, String downloadUrl);

    void onDLPause(String id, String downloadUrl);

    void onDLFailed(String id, String downloadUrl);

    void onDLFinished(String id, String downloadUrl);

    void onDLDownloading(int progress, String id, String downloadUrl);
}
