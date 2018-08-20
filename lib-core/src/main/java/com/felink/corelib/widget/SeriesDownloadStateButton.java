package com.felink.corelib.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by linliangbin on 2017/5/22 20:34.
 */

public class SeriesDownloadStateButton extends DownloadStateButton {

    public SeriesDownloadStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initData() {

        mStateMapping.put(STATE_FINISHED, "100%");
        mStateMapping.put(STATE_FINAL, "设为壁纸");
        mStateMapping.put(STATE_WAITING, "等待下载...");
        mStateMapping.put(STATE_START, "0%");
        mStateMapping.put(STATE_PAUSE, "继续");
        mStateMapping.put(STATE_FAILED, "重新下载");
        mStateMapping.put(STATE_PENDING, "初始化...");
        mStateMapping.put(STATE_PREPARED, "下载全套");
        mStateMapping.put(STATE_TRANSLATING, "解析中...");
        mStateMapping.put(STATE_TRANS_FAILED, "重试");
        mStateMapping.put(STATE_INIT_FAILED, "重试");
    }
}
