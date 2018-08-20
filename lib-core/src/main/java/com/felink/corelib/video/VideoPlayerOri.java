package com.felink.corelib.video;
import com.felink.corelib.kitset.proxy.VideoProxyLoader;

/**
 * @Description: 简易视频播放工具类</br>
 * @author: zhouhq
 * @date: 2018年06月12日 19:41.</br>
 * @update: </br>
 */

public class VideoPlayerOri extends VideoPlayer {
    static VideoPlayer own;

    public synchronized static VideoPlayer getInstance2() {
        if (own == null) {
            own = new VideoPlayerOri();
        }
        return own;
    }

    public VideoPlayerOri() {
        super(true);
    }
    @Override
    public synchronized void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        context = null;
        mTextureView = null;
        mDataSource = null;
        own = null;

        VideoProxyLoader.getInstance().postPause();
    }

}
