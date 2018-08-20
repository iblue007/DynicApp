package com.felink.corelib.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.felink.corelib.analytics.AnalyticsConstant;
import com.felink.corelib.analytics.HiAnalytics;
import com.felink.corelib.config.BaseConfigPreferences;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.FileUtil;
import com.felink.corelib.kitset.ScreenUtil;
import com.felink.corelib.kitset.proxy.ProxyCallback;
import com.felink.corelib.kitset.proxy.VideoProxyLoader;

import java.util.Map;

/**
 * 自行处理来电等需静音的情况
 * Created by caizhipeng on 2017/5/20.
 */

public class CustomMediaPlayer extends MediaPlayer implements VideoPlayerInterface {
    private static final String TAG = "CustomMediaPlayer";

    private boolean mVolumnEnable = false;
    private float leftVolume = 1.0f, rightVolume = 1.0f;

    private Surface mSurface;
    private SurfaceHolder surfaceHolder;
    private String mProxyUrl;

    private int viewWidth, viewHeight;
    private TextureView textureView;

    private Context context;


    private VideoSizeListener videoSizeListener;

    private boolean isOnPausePlay = false;

    //缓冲开始时间（毫秒）
    private long mBufferingStartTime = 0;

    private VideoPlayer.OnMediaStateListener mMediaStateListener;
    private BroadcastReceiver volumeChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BaseConfigPreferences.getInstance(context).isVideoWallpaperVolumnEnable()) {
                BaseConfigPreferences.getInstance(Global.getContext()).setSystemVolumeChanged(true);
                reInitVolume();
            }
        }
    };

    public CustomMediaPlayer(Context context) {
        super();
        this.context = context;
        initVolumeChangeListener();
        initMediaPlayer();
    }


    @Override
    public void setVideoSizeListener(VideoSizeListener videoSizeListener) {
        this.videoSizeListener = videoSizeListener;
    }


    private void initVolumeChangeListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        Global.getContext().registerReceiver(volumeChange, intentFilter);
    }


    public void unregister() {
        if (volumeChange != null) {
            try {
                Global.getContext().unregisterReceiver(volumeChange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isOnPausePlay() {
        return isOnPausePlay;
    }


    private void reInitVolume() {
        setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setVolume(float volume) {
        try {
            int pendLV = (int) (volume * 100);
            int pendRV = (int) (volume * 100);
            leftVolume = volume;
            rightVolume = volume;
            if (pendLV > 0 && pendRV > 0) {
                mVolumnEnable = true;
            } else {
                mVolumnEnable = false;
            }
            if (mVolumnEnable) {
                float oldVolume = VolumnControler.getInstance().getVolumeValue();
                super.setVolume(oldVolume, oldVolume);
            } else {
                super.setVolume(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            leftVolume = 1.0f;
            rightVolume = 1.0f;
        }
    }


    private void computeVideoSize() {

        int videoWidth = getVideoWidth();
        int videoHeight = getVideoHeight();
        float videoAspectRatio = videoWidth * 1.0f / videoHeight;
        viewWidth = ScreenUtil.getCurrentScreenWidth(context);
        viewHeight = ScreenUtil.getSreenRealHeight(context);
        float viewAspectRatio = viewWidth * 1.0F / viewHeight;

        float aspectDeformation = videoAspectRatio / viewAspectRatio - 1;

        if (aspectDeformation > 0) {
            viewWidth = (int) (viewHeight * videoAspectRatio);
        } else {
            viewHeight = (int) (viewWidth / videoAspectRatio);
        }

    }

    private boolean initMediaPlayer() {

        setAudioStreamType(AudioManager.STREAM_MUSIC);
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (!mVolumnEnable) {
                    mp.setVolume(0, 0);
                }
                mp.start();
            }
        });

        setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN || what == -38) {
                    if (mMediaStateListener != null) {
                        mMediaStateListener.onError(mp);
                    }
                }
                return false;
            }
        });

        setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == 3) {
                    logBufferingTime();
                    if (mMediaStateListener != null) {
                        mMediaStateListener.onRenderingStart(mp);
                    }
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    if (mMediaStateListener != null) {
                        mMediaStateListener.onBufferingStart(mp);
                    }
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    logBufferingTime();
                    if (mMediaStateListener != null) {
                        mMediaStateListener.onBufferingEnd(mp);
                    }
                }
                return false;
            }
        });
        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mMediaStateListener != null) {
                    mMediaStateListener.onCompletion(mp);
                }
            }
        });

        setOnSeekCompleteListener(new OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                if (mMediaStateListener != null) {
                    mMediaStateListener.onRepeat();
                }
            }
        });
        setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, final int width, final int height) {
                if (videoSizeListener != null) {
                    videoSizeListener.onSizeChanged(width, height);
                    return;
                }
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        computeVideoSize();
                        if (surfaceHolder != null) {
                            try {
                                surfaceHolder.setFixedSize(viewWidth, viewHeight);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else if (textureView != null && textureView instanceof AutosizeTexture) {
                            float videoAspectRatio = (height == 0 || width == 0) ? 1 : (width * 1.0f) / height;
                            ((AutosizeTexture) textureView).setAspectRatio(videoAspectRatio);
                        }
                    }
                });
            }
        });
        return true;
    }


    public void logBufferingTime() {
        //本地视频不做缓冲时长统计
        if (TextUtils.isEmpty(mProxyUrl) || FileUtil.isFileExits(mProxyUrl)) {
            return;
        }
        if (mBufferingStartTime == 0) {
            //记录起始时间
            mBufferingStartTime = System.currentTimeMillis();
            Log.e(TAG, "start logging");
        } else if (mBufferingStartTime != -1) {
            //开始播放
            long delta = System.currentTimeMillis() - mBufferingStartTime;
            if (delta < 0) {
                return;
            }
            if (delta < 2000) {
                delta = (delta + 99) / 100 * 100;
                Log.e(TAG, "buffering = " + delta + "(ms)");
                HiAnalytics.submitEvent(Global.getApplicationContext(), AnalyticsConstant.VIDEO_BUFFERING_TIME_STATISTICS, delta + "ms");
            } else {
                delta = (delta + 999) / 1000;
                Log.e(TAG, "buffering = " + delta + "(s)");
                HiAnalytics.submitEvent(Global.getApplicationContext(), AnalyticsConstant.VIDEO_BUFFERING_TIME_STATISTICS, delta + "s");
            }
            mBufferingStartTime = -1;
        }
    }


    @Override
    public void release() {
        reset();
        super.release();
        unregister();
    }


    @Override
    public void resetPlayer() {
        reset();
    }

    @Override
    public void resumePlaying() {
        try {
            isOnPausePlay = false;
            start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pausePlaying() {
        try {
            isOnPausePlay = true;
            pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void playLocalFile(TextureView textureView, String path, boolean isLooping) {
        this.textureView = textureView;
        playVideo(textureView, path, isLooping);
    }

    @Override
    public void playLocalFile(SurfaceHolder surfaceHolder, String path, boolean isLooping) {
        if (surfaceHolder != null) {
            this.surfaceHolder = surfaceHolder;
            this.mSurface = surfaceHolder.getSurface();
            playVideo(mSurface, path, isLooping);
        }
    }


    private void playVideo(TextureView textureView, String path, boolean isLooping) {
        try {
            mSurface = new Surface(textureView.getSurfaceTexture());
            playVideo(mSurface, path, isLooping);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playVideo(Surface surface, String path, boolean isLooping) {

        try {
            setDataSource(path);
            setSurface(surface);
            setLooping(isLooping);
            prepareAsync();
            logBufferingTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void playServerUrl(final TextureView textureView, final String path, final boolean isLooping) {
        this.textureView = textureView;
        VideoProxyLoader.getInstance().proxyAsync(path, new ProxyCallback() {
            @Override
            public void onPrepared(String originUrl, String proxyUrl, Map<String, String> params) {
                CustomMediaPlayer.this.mProxyUrl = proxyUrl;
                playVideo(textureView, proxyUrl, isLooping);
            }
        });
    }

    @Override
    public void setOnMediaStateListener(VideoPlayer.OnMediaStateListener listener) {
        this.mMediaStateListener = listener;
    }
}
