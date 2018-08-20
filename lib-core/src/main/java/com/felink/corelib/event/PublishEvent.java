package com.felink.corelib.event;

/**
 * Description: </br>
 * Author: chenxy
 * Date: 2016/10/25
 */
public class PublishEvent {

    /**
     * 声音开关改变
     */
    public static final String EVENT_SOUND_SWITCHER_CHANGED = "event_sound_switcher_changed";

    /**
     * 预览视频声音开关
     */
    public static final String EVENT_SOUND_PREVIEW_SWITCHER_CHANGED = "event_sound_switcher_changed";

    /**
     * 应用视频壁纸
     */
    public static final String EVENT_APPLY_VIDEOPAPER = "event_apply_videopaper";
    /**
     * 播放顺序改变
     */
    public static final String EVENT_PLAYLIST_CHANGE = "event_playlist_change";
    /**
     * 本地视频删除
     */
    public static final String EVENT_NATIVE_DELETE = "event_native_delete";
    /**
     * 下载完成
     */
    public static final String EVENT_NEW_DOWNLOAD_TASK_FINISHED = "event_download_task_finished";
    /**
     * 建议关闭音频焦点
     */
    public static final String EVENT_ADVICE_ABANDON_AUDIO_FOCUS = "event_advice_abandon_audio_focus";
    public static final String EVENT_ADVICE_REQUEST_AUDIO_FOCUS = "event_advice_request_audio_focus";
    /**
     * 尝试获取音频焦点
     */
    public static final String EVENT_TRY_REQUEST_AUDIO_FOCUS = "event_try_request_audio_focus";

    /**
     * 视频详情修改视频状态通知个人中心
     */
    public static final String EVENT_SET_VIDEO_STATUS = "event_set_video_status";

    /**
     * 点赞
     */
    public static final String EVENT_UPVOTE = "event_upvote";
    /**
     * 取消点赞
     */
    public static final String EVENT_UNUPVOTE = "event_unupvote";
    /**
     * 关注
     */
    public static final String EVENT_FOLLOWER = "event_follower";
    /**
     * 取消关注
     */
    public static final String EVENT_DESTROY = "event_destroy";
    /**
     * 上报视频浏览量
     */
    public static final String EVENT_SUBMIT_SCAN_COUNT = "event_submit_scan_count";

    /**
     * 设置静态壁纸
     */
    public static final String EVENT_SET_STATIC_WALLPAPER= "event_set_static_wallpaper";
}
