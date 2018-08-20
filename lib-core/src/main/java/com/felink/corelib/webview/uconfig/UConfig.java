package com.felink.corelib.webview.uconfig;

import android.content.Context;
import android.text.TextUtils;

import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.ThreadUtil;
import com.felink.corelib.webview.GZipHttpUtil;

import org.json.JSONObject;

/**
 * Description: 万能配置通用类</br>
 * Author: cxy
 * Date: 2017/2/20.
 */

public class UConfig {

    private UConfigHelper helper;

    private static UConfig obj;

    private UConfig(){
        initialise(Global.getApplicationContext());
    }

    public static UConfig get(){
        if(obj == null){
            synchronized (UConfig.class){
                if(obj == null){
                    obj = new UConfig();
                }
            }
        }
        return obj;
    }

    private final void initialise(Context context) {
        helper = new UConfigHelper(context);
    }

    public final String getUrl(String configName) {
        return getUrl(configName, true);
    }

    public final String getUrl(String configName, boolean verifyVer) {
        return helper.getBaseUrl(configName, verifyVer);
    }

    public final int getVersion(String configName) {
        return helper.getVersion(configName);
    }

    public final void markDone(String configName) {
        markDone(configName, helper.getVersion(configName) + 1);
    }

    public final void markDone(String configName, int newVer) {
        final int oldVer = helper.getVersion(configName);
        if (newVer > oldVer) {
            helper.setVersion(configName, newVer);
        }
    }

    public final String getContent(String configName) {
        return helper.getContent(configName);
    }

    public final void commit(String configName, String content) {
        helper.setContent(configName, content);
    }

    public final <T> T getContent(String configName, ContentConverter<T> converter) {
        final String src = getContent(configName);
        if (converter != null) {
            return converter.convert(src);
        }
        return null;
    }

    public final void easyFetchAsync(final String configName, final boolean verifyVer) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                easyFetchSync(configName, verifyVer);
            }
        });
    }

    public final String easyFetchSync(final String configName, final boolean verifyVer) {
        String url = getUrl(configName, verifyVer);
        String response = GZipHttpUtil.get(url);
        if (TextUtils.isEmpty(response)) {
            return response;
        }
        JSONObject json = null;
        try {
            json = new JSONObject(response);
            int oldver = getVersion(configName);
            int newver = json.optInt("version", oldver);
            commit(configName, response);
            if (newver > oldver) {
                markDone(configName, newver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
