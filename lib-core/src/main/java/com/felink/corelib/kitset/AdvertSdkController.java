package com.felink.corelib.kitset;

import android.content.Context;
import android.util.Log;

import com.dian91.ad.AdvertSDKManager;

import java.util.HashMap;

/**
 * Created by xuqunxing on 2018/4/27.
 */
public class AdvertSdkController {
    public static HashMap<String, AdvertSDKManager.AdvertInfo> downloadAdvertMap = new HashMap<String, AdvertSDKManager.AdvertInfo>();

    public static void submitStartDownloadEvent(Context ctx, AdvertSDKManager.AdvertInfo adverInfo, String downLoadKey){
        Log.e("======", "======submitStartDownloadEvent"+adverInfo.id);
        AdvertSDKManager.submitStartDownloadEvent(ctx, adverInfo, "");
        downloadAdvertMap.put(downLoadKey, adverInfo);
    }
    public static void submitFinishDownloadEvent(Context ctx, String identifier){
        try {
            for(String key : downloadAdvertMap.keySet()){
                if(identifier.contains(key)){
                    Log.e("======", "======submitFinishDownloadEvent"+downloadAdvertMap.get(key).id);
                    AdvertSDKManager.submitFinishDownloadEvent(ctx, downloadAdvertMap.get(key), "");
                    downloadAdvertMap.remove(key);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
