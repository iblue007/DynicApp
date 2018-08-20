package com.xxm.dynicapp.floatt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xxm.dynicapp.MainActivity;

/**
 * Created by xuqunxing on 2018/8/17.
 */
public class FloatWindowService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyWindowManager.createWindow(MainActivity.getInstance());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
