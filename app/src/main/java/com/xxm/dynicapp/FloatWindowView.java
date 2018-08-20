package com.xxm.dynicapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.felink.corelib.config.BaseConfig;
import com.nd.hilauncherdev.kitset.util.FileUtil;

import java.io.File;

/**
 * Created by xuqunxing on 2018/8/17.
 */
public class FloatWindowView extends LinearLayout {

    /**
     * 记录悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录悬浮窗的高度
     */
    public static int viewHeight;
    private Context mContext;
    private VideoView videoView;


    public FloatWindowView(final Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.float_window, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        videoView = (VideoView) findViewById(R.id.video_view);

        FileUtil.copyAssetsFile(getContext(), "test.mp4", BaseConfig.WIFI_DOWNLOAD_PATH, "test.mp4");
        final String path = BaseConfig.WIFI_DOWNLOAD_PATH + "test.mp4";
        File file = new File(path);
        if (!file.exists()) {
            Log.e("TAG", "播放路径不存在！");
            //可以加载项目中资源文件里面的默认视频

            return;
        }

        videoView.setVideoPath(path);
        videoView.setZOrderOnTop(true);
        videoView.setZOrderMediaOverlay(true);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e("TAG", "onPrepared");
                mp.start();
                mp.setLooping(true);

                Intent intentHome = new Intent(Intent.ACTION_MAIN);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentHome.addCategory(Intent.CATEGORY_HOME);
                mContext.startActivity(intentHome);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("TAG", "onCompletion");
                videoView.setVideoPath(path);
                videoView.start();

            }
        });
        videoView.start();
    }


}