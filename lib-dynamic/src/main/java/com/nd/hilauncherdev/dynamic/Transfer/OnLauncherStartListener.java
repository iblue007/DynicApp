package com.nd.hilauncherdev.dynamic.Transfer;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/16.
 */

public interface OnLauncherStartListener {
    int TYPE_EVERY_TIME = 0;
    int TYPE_ONCE_A_DAY = 1;
    int TYPE_ONCE_A_DAY_NOT_NETWORK = 2;

    void onLauncherStart(Context var1);

    int getType();
}