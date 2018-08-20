package com.felink.corelib.kitset.proxy;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import com.felink.corelib.kitset.FileUtil;
import com.felink.corelib.kitset.generic.GenericLoader;

import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 21:10.</br>
 * @update: </br>
 */

public class ProxyUtil {

    private static int sProxyEnableVer = -1;
    private static boolean sProxyEnable = true;

    /**
     * 获取重定向地址
     *
     * @param originUrl
     * @return
     * @throws Exception
     */
    public static String getRedirectUrl(String originUrl) throws Exception {
        URL url = new URL(originUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        conn.connect();
        int code = conn.getResponseCode();
        if (code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_MOVED_TEMP) {
            return conn.getHeaderField("Location");
        }
        return originUrl;
    }

    public static void closeQuietly(/*Auto*/Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean isVideoAvailable(String src) {
        MediaMetadataRetriever mmr = null;
        FileDescriptor fd = null;
        try {
            if (!FileUtil.isFileExits(src)) {
                return false;
            }
            File file = new File(src);
            FileInputStream is = new FileInputStream(file);
            mmr = new MediaMetadataRetriever();
            fd = is.getFD();
            mmr.setDataSource(fd);
            String hasVideo = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);

            if ("yes".equalsIgnoreCase(hasVideo)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }

        return false;
    }

    /**
     * public void setDataSource(String path, Map<String, String> headers)
     *
     * @param player
     * @param path
     * @param headers
     */
    public static void setDataSourceWithHeaders(MediaPlayer player, String path, Map<String, String> headers) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class clazz = MediaPlayer.class;
        Method setDataSource = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
        setDataSource.setAccessible(true);
        setDataSource.invoke(player, path, headers);
    }

    public static long[] parseRange(String range) {
        long[] ran = new long[]{-1, -1};
        if (TextUtils.isEmpty(range)) {
            return ran;
        }
        range = range.trim();
        range = range.replace("bytes=", "");

        String[] seg = range.split("-");
        if (seg != null && seg.length > 0) {
            try {
                ran[0] = Long.valueOf(seg[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (seg.length > 1) {
                try {
                    ran[1] = Long.valueOf(seg[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ran;
    }

    /**
     * 是否允许代理（默认“允许”）
     *
     * @return
     */
    public static boolean isProxyEnable() {
        GenericLoader.GenericBean bean = GenericLoader.getInstance().getNative("VPBaseConfiguration");
        if (bean != null) {
            Log.e("cxydebug", "p : " + bean.version + " - " + sProxyEnableVer);
            //版本没有更新，无需再解析
            if (bean.version > sProxyEnableVer) {
                sProxyEnableVer = bean.version;
                try {
                    JSONObject json = new JSONObject(bean.content);
                    JSONObject proxyJson = json.optJSONObject("proxy");
                    if (proxyJson != null) {
                        int switcher = proxyJson.optInt("switch", 1);
                        sProxyEnable = switcher == 1;
                    }
                } catch (Exception e) {
                }
            }
        }
        Log.e("cxydebug", "p enable = " + sProxyEnable);
        return sProxyEnable;
    }

}
