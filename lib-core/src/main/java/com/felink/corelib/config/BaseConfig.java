package com.felink.corelib.config;

import android.os.Environment;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年05月02日 12:34.</br>
 * @update: </br>
 */

public class BaseConfig {

//    产品名：视频壁纸
//    平台：Android
//    APPID: 20000073
//    APPKEY: abd1bfd7266348f59c6f34645c2569f4
//    PSOURCE：50004

    public static String PID = "20000073";
    public static String APPKEY = "abd1bfd7266348f59c6f34645c2569f4";
    public static int APPID = 20000073;

    public static String PANDAHOME2_PKG = "com.nd.android.pandahome2";

    public static boolean LinkEncryptEnable = true;

    private static final String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Videopaper/";

    public static final String WIFI_DOWNLOAD_PATH = BASE_DIR + "WifiDownload/";

    public static final String VIDEO_SOURCE_BASE_DIR = BASE_DIR + "vp/";
    public static final String VIDEO_THIRD_PART_BASE_DIR = VIDEO_SOURCE_BASE_DIR + "thirdPart/";
    public static final String VIDEO_THIRD_PART_RES_BASE_DIR = VIDEO_THIRD_PART_BASE_DIR + "res/";
    public static final String VIDEO_THIRD_PART_CUNIT_BASE_DIR = VIDEO_THIRD_PART_BASE_DIR + "cUnit/";
    public static final String VIDEO_THIRD_PART_WALLPAPER_DIR = VIDEO_THIRD_PART_BASE_DIR + "wallpaper/";
    public static final String IMAGE_SOURCE_BASE_DIR = BASE_DIR + "iv/";
    public static final String Ad_SOURCE_BASE_DIR = BASE_DIR + "ad/";

    /**
     * 下载的视频资源目录
     */
    public static final String VIDEO_SOURCE_DIR = VIDEO_SOURCE_BASE_DIR + "res/";

    /**
     * 预览视频资源目录
     */
    public static final String VIDEO_PREVIEW_SOURCE_DIR = VIDEO_SOURCE_BASE_DIR + "preview/";

    /**
     * 下载的资源壁纸目录
     */
    public static final String VIDEO_WALLPAPER_DIR = VIDEO_SOURCE_BASE_DIR + "wallpaper/";

    /**
     * 本地视频单元配置目录
     */
    public static final String UNIT_CONFIG_DIR = VIDEO_SOURCE_BASE_DIR + "cUnit/";
    /**
     * 本地视频天天配置目录
     */
    public static final String SERIES_CONFIG_DIR = VIDEO_SOURCE_BASE_DIR + "cSeries/";
    /**
     * 视频磁盘缓存目录
     */
    public static final String VIDEO_DISK_CACHE_DIR = VIDEO_SOURCE_BASE_DIR + ".cache/";
    /**
     * 视频桌面视频播放目录
     */
    public static final String VIDEO_DISK_PLAYLIST_DIR = VIDEO_SOURCE_BASE_DIR + "playlist/";
    /**
     * 视频桌面视频播放目录(高清)
     */
    public static final String VIDEO_DISK_PLAYLIST_HD_DIR = VIDEO_SOURCE_BASE_DIR + "playlistHD/";


    /**
     * 临时资源目录
     */
    public static final String SOURCE_TEMP_DIR = BASE_DIR + "tmp/";
    /**
     * 临时视频资源
     */
    public static final String SOURCE_TEMP_VIDEO_DIR = SOURCE_TEMP_DIR + "video/";
    /**
     * 临时图片资源
     */
    public static final String SOURCE_TEMP_IMG_DIR = SOURCE_TEMP_DIR + "img/";
    public static final String SOURCE_TEMP_SHARE_IMG_DIR = SOURCE_TEMP_IMG_DIR + "share/";
    /**
     * 临时资源目录
     */
    public static final String SOURCE_TEMP_ONLINE_CACHE = SOURCE_TEMP_DIR + "onlinecache/";


    public static final String VIDEO_DOWNLOAD_STUB_URL = "videopaper";
    public static final String VIDEO_DOWNLOAD_STUB_SAVE_NAME = "videopaper";

    /**
     * 个人信息存储路径
     */
    public static final String CACHES_USER_INFO = VIDEO_SOURCE_BASE_DIR + "userinfo/";


    public static String getBaseDir() {
        return BASE_DIR;
    }

    public static String getApplicationDataPath() {
        return Environment.getDataDirectory() + "/data/" + Global.getApplicationContext().getPackageName();
    }
}
