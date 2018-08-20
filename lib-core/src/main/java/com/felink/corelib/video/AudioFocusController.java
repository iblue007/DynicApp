package com.felink.corelib.video;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.felink.corelib.config.BaseConfigPreferences;
import com.felink.corelib.event.EventBus;
import com.felink.corelib.event.EventSubscriber;
import com.felink.corelib.event.PublishEvent;

import static android.media.AudioManager.AUDIOFOCUS_REQUEST_FAILED;

/**
 * 音量焦点监听控制
 * Created by linliangbin on 2018/5/25 15:38.
 */

public class AudioFocusController implements EventSubscriber {
    private static final String TAG = "VideoLaucherPlayer";

    VideoPlayerInterface videoPlayerInterface;
    private boolean isAudioOccupied = false;
    private Context context;
    private AudioManager mAudioManager;
    private boolean hasRegisteredAudioFocuse = false;
    private boolean engineVisible = true;
    private int lastAudioFocus = AudioManager.AUDIOFOCUS_GAIN;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            lastAudioFocus = focusChange;
            Log.e(TAG, "a focus = " + focusChange);
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN
                    || focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                    || focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK) {
                switchVolumn(true);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //失去焦点，stop
                switchVolumn(false);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                //失去瞬时焦点，pause
                switchVolumn(false);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                //音量变小
                switchVolumn(false);
            }
        }
    };

    public AudioFocusController(Context context) {
        this.context = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        EventBus.getInstance().subScribe(PublishEvent.EVENT_SOUND_SWITCHER_CHANGED, this);
        EventBus.getInstance().subScribe(PublishEvent.EVENT_ADVICE_ABANDON_AUDIO_FOCUS, this);
        EventBus.getInstance().subScribe(PublishEvent.EVENT_ADVICE_REQUEST_AUDIO_FOCUS, this);
        EventBus.getInstance().subScribe(PublishEvent.EVENT_TRY_REQUEST_AUDIO_FOCUS, this);
    }

    public void setEngineVisible(boolean engineVisible) {
        this.engineVisible = engineVisible;

    }

    public void setVideoPlayerInterface(CustomMediaPlayer videoPlayerInterface) {
        this.videoPlayerInterface = videoPlayerInterface;
    }

    /**
     * {@hide}
     *
     * @return
     */
    private int abandonAudioFocus() {
        if (mAudioManager != null) {
            hasRegisteredAudioFocuse = false;
            return mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
        return AUDIOFOCUS_REQUEST_FAILED;
    }

    public int requestAudioFocus() {
        if (mAudioManager != null) {
            int granted = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            Log.e(TAG, "r focus = " + granted);
            hasRegisteredAudioFocuse = true;
            if (granted == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                lastAudioFocus = granted;
            }
            return granted;
        }
        return AUDIOFOCUS_REQUEST_FAILED;
    }

    public void onResume() {
        boolean volumnOpen = BaseConfigPreferences.getInstance(context).isVideoWallpaperVolumnEnable();
        //音频被占用时，不可开声音
        if (!isAudioOccupied && volumnOpen && !isRegisteredAudioFocus()) {
            int result = requestAudioFocus();
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                switchVolumn(true);
            }
        }else if(!volumnOpen){
            switchVolumn(false);
        }
    }

    public boolean isRegisteredAudioFocus() {
        return hasRegisteredAudioFocuse;
    }


    public boolean isAudioFocused() {
        if (lastAudioFocus == AudioManager.AUDIOFOCUS_GAIN
                || lastAudioFocus == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                || lastAudioFocus == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK) {
            return true;
        }
        return false;
    }


    public void onDestory() {
        abandonAudioFocus();
        EventBus.getInstance().unSubScribe(PublishEvent.EVENT_SOUND_SWITCHER_CHANGED, this);
        EventBus.getInstance().unSubScribe(PublishEvent.EVENT_ADVICE_ABANDON_AUDIO_FOCUS, this);
        EventBus.getInstance().unSubScribe(PublishEvent.EVENT_ADVICE_REQUEST_AUDIO_FOCUS, this);
        EventBus.getInstance().unSubScribe(PublishEvent.EVENT_TRY_REQUEST_AUDIO_FOCUS, this);
    }

    private void syncVolumn() {
        if (BaseConfigPreferences.getInstance(context).isVideoWallpaperVolumnEnable()) {
            setVolume(1.0f, 1.0f);

        } else {
            setVolume(0, 0);
        }
    }

    public boolean isAvailablePlayer() {
        return videoPlayerInterface != null;
    }


    public void switchVolumn(boolean on) {
        try {
            if (on) {
                boolean volumnOpen = BaseConfigPreferences.getInstance(context).isVideoWallpaperVolumnEnable();
                //获取焦点，restart
                float volumn = volumnOpen ? 1f : 0f;
                if (isAvailablePlayer()) {
                    setVolume(volumn, volumn);
                }
            } else {
                if (isAvailablePlayer()) {
                    setVolume(0f, 0f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setVolume(float left, float right) {
        if (isAvailablePlayer()) {
            //在音频失去焦点的情况下，设置高音量无效
            if (!isAudioFocused()) {
                int pendLV = (int) (left * 100);
                int pendRV = (int) (right * 100);
                if (pendLV > 0 || pendRV > 0) {
                    return;
                }
            }
            videoPlayerInterface.setVolume(left);
        }
    }

    @Override
    public void dealEvent(String eventKey, Bundle bundle) {
        if (PublishEvent.EVENT_SOUND_SWITCHER_CHANGED.equals(eventKey)) {
            syncVolumn();
        } else if (PublishEvent.EVENT_ADVICE_ABANDON_AUDIO_FOCUS.equals(eventKey)) {
            isAudioOccupied = true;
            switchVolumn(false);
        } else if (PublishEvent.EVENT_ADVICE_REQUEST_AUDIO_FOCUS.equals(eventKey)) {
            isAudioOccupied = false;
            if (engineVisible) {
                switchVolumn(true);
            }
        } else if (PublishEvent.EVENT_TRY_REQUEST_AUDIO_FOCUS.equals(eventKey)) {
            //尝试去获取焦点
            if (!isAudioOccupied) {
                requestAudioFocus();
                switchVolumn(true);
            }
        }
    }


}
