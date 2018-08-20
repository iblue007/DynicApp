package com.xxm.dynicapp;

import android.app.Application;
import android.os.Handler;

import com.felink.corelib.config.Global;

/**
 * Created by xuqunxing on 2018/8/9.
 */

public class BApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Global.setContext(this);
        Global.setHandler(new Handler());
    }
}
