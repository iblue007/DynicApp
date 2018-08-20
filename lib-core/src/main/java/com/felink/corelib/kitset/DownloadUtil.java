package com.felink.corelib.kitset;

import android.text.TextUtils;

import com.felink.corelib.config.BaseConfig;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xuqunxing on 2018/5/23.
 */
public class DownloadUtil {

    public static String getDownFileNameNew(String vid,String identifer) {
        try {
            return vid +"_"+identifer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDownFilePath(String vid,String identifer) {
        try {
            return BaseConfig.VIDEO_DISK_PLAYLIST_HD_DIR + vid +"_"+identifer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDownFileName(String downloadUrl) {
        try {
            return DigestUtil.md5Hex(downloadUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDownloadIdentification(String fileName, String videoId) {
        return fileName + "@@" + videoId;
    }

    public static String getDownFileNameFromIdentification(String Identification) {
        try {
            String[] split = Identification.split("@@");
            String fileName = split[0];
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @desc 从下载ID 中获取视频ID
     * @author linliangbin
     * @time 2018/6/4 15:13
     */
    public static String getVideoIdFromIdentification(String Identification) {
        try {
            String[] split = Identification.split("@@");
            return split[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getVideoId(String addtion) {
        try {
            if (!TextUtils.isEmpty(addtion)) {
                JSONObject responseObj = new JSONObject(addtion);
                String videoId = responseObj.optString("videoId");
                return videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVideoFromType(String addtion) {
        try {
            if (!TextUtils.isEmpty(addtion)) {
                JSONObject responseObj = new JSONObject(addtion);
                String videoFromType = responseObj.optString("videoFromType");
                return videoFromType;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getAdditionalInfo(int type, String videoId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("videoFromType", String.valueOf(type));
            jsonObject.put("videoId", videoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 文件是否已经下载到本地
     */
    public static boolean isFileDownloaded(String fileName) {
        if (FileUtil.isFileExits(BaseConfig.VIDEO_DISK_PLAYLIST_HD_DIR + fileName)) {
            return true;
        }
        return false;
    }

}
