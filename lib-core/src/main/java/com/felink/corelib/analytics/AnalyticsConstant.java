package com.felink.corelib.analytics;

/**
 * Created by linliangbin on 2016/10/17.
 */

public class AnalyticsConstant {

    /**
     * 异常统计
     */
    public static final int EXCEPTION = 10000001;

    //自升级点击通知栏提醒（xx-发通知栏消息，dj-点击通知栏消息）
    public static final int UPGRADE_NOTIFI_CLICK = 10000002;
    //自升级窗口（pt-普通升级，zd-我知道了）
    public static final int UPGRADE_DIALOG_CLICK = 10000003;
    //动态壁纸切换时间（天）
    public static final int LIVE_WALLPAPER_SWITCH_INTERVAL = 10000004;
    //动态壁纸应用（series：天天；unit：单套）
    public static final int LIVE_WALLPAPER_APPLY = 10000005;
    //动态壁纸一个生命期的总时长（天）
    public static final int LIVE_WALLPAPER_ENDING_TIME = 10000006;

    //首页TAB()
//    public static final int TAB_HOME_SELECTED = 10001000;
//
//    public static final int DRAWER_NAVIGATION_MENU_CLICK = 10001001;
//
//    public static final int VIDEO_CATEGORY_SELECTED = 10001002;
//
//    public static final int VIDEO_TAB_ITEM_CLICK = 10001003;
//
//    public static final int VIDEO_PAPER_PERFORM = 10001004;
//
//    public static final int VIDEO_SERIES_PERFORM = 10001005;
//
//    public static final int VIDEO_SERIES_SETTINGS = 10001006;

    /**
     * 01个人中心
     */
    //进入个人中心
    public static final int INTO_PERSONAL_CENTER = 11001001;
    //进入我的关注列表
    public static final int INTO_MY_FOLLOW_WITH_INTEREST_LIST = 11001002;
    //进入我的粉丝列表
    public static final int INTO_MY_FOLLEROW_LIST = 11001003;
    //切换列表显示状态（s-缩略图;l-列表）
    public static final int CHANGE_VIDEO_LIST_SHOW_STATUS = 11001004;
    //进入编辑我的资料次数
    public static final int INTO_EDIT_MY_INFO_ACITIVITY = 11001005;
    //进入视频详情页
    public static final int INTO_VIDEO_DETAIL_ACTIVITY = 11001006;

    /**
     * 视频拍摄入口进入量
     *
     * @label topr：Tab右上角
     * @label sidebar：侧边栏抽屉
     */
    public static final int VIDEO_SHOOT_ENTRY = 11001007;
    /**
     * 视频的点击进入量
     *
     * @label fx：从发现列表进入
     * @label dy：从关注列表进去
     * @label tj：从推荐列表进去
     */
    public static final int VIDEO_ITEM_CLICK = 11001008;
    /**
     * 视频发布相关统计
     *
     * @label xc：本地相册（点击“下一步”时）
     * @label ps：拍摄（点击“下一步”时）
     * @label bj：视频编辑（点击“下一步”时）
     * @label cj：视频裁剪（点击“下一步”时）
     * @label fm：选择封面（点击“下一步”时）
     * @label fb：发布视频（点击“发布”时）
     */
    public static final int PUBLISH_RELATED_STATISTICS = 20001009;
    /**
     * 评论相关统计
     *
     * @label open：打开评论列表
     * @label add：新建评论
     * @label reply：回复评论
     * @label del：删除评论
     * @label report：举报评论
     */
    public static final int COMMENT_RELATED_STATISTICS = 11001010;

    /**
     * 视频缓存时长统计
     *
     * @label 从缓冲开始到开始播放，如果耗时小于2s,则以毫秒级算，如：1200ms，大于等2s时，直接以秒为最小单位
     */
    public static final int VIDEO_BUFFERING_TIME_STATISTICS = 11001011;

    /***
     * 视频详情页
     *
     * @label gz：关注用户
     * @label hbz：自动换/设置壁纸
     * @label dz：点赞
     * @label pl：评论
     * @label fspl：发送评论
     * @label jb：举报
     */
    public static final int VIDEO_DETIAL_VIEW_STATISTICS = 20001012;
    /***
     * 我的消息
     *
     * @label jr：进入我的消息
     * @label jbcf：举报、处罚
     * @label jbfk：举报反馈
     * @label pl：评论
     */
    public static final int MY_NEWS_STATISTICS = 20001013;
    /***
     * 关注
     *
     * @label grzx:个人中心
     * @label spxq:视频详情
     */
    public static final int FOLLOW_VIEW_STATISTICS = 20001014;
    /***
     * 订阅
     *
     * @label grzx:个人中心
     * @label spxq:视频详情
     */
    public static final int SCAN_VIEW_STATISTICS = 20001015;

    /**
     * 每日统计91桌面是否安装
     *
     * @label n:未安装
     * @label y:已安装
     */
    public static final int HAS_INSTALLED_PANDAHOME2 = 21980001;

    /**
     * 开屏loading广告数据打点
     *
     * @label zs：展示
     * * @label dj：点击
     */
    public static final int AD_OPEN_LOADING = 21980002;
    /**
     * 双击loading广告数据打点
     *
     * @label zs：展示
     * * @label dj：点击
     */
    public static final int AD_DOUBULE_CLICK_LOADING = 21980003;

    /**
     * 每日桌面视频播放量 打点
     */
    public static final int VIDEOPLAY_BF_LAUNCHER = 22080001;
    /**
     * 每日视频播放量 打点
     */
    public static final int VIDEOPLAY_BF_ALL = 22080002;

    /**
     * 发现模块-信息流广告点击情况
     */
    public static final int AD_INFOMATION_FLOW_FX = 22080003;
    /**
     * 发现模块 -- 首页视频信息流的浏览情况（3、5、10、15、20、25）打点
     */
    public static final int VIDEO_MAIN_FX = 22080004;

    /**
     * 搜索功能使用情况（jr-进入搜索;ss-使用搜索;rc-点击热词）
     */
    public static final int SEARCH_USE_INFO = 22800005;

    /**
     * 推荐模块 -- 首页视频信息流的浏览情况（3、5、10、15、20、25）打点
     */
    public static final int VIDEO_MAIN_TJ = 23080001;
    /**
     * 推荐模块-信息流广告点击情况
     */
    public static final int AD_INFOMATION_FLOW_TJ = 23080002;

    /**
     * 视频详情--点击下载高清壁纸
     */
    public static final int VIDEO_DETAIL_DOWN_HD = 23080003;
    /**
     * 视频详情--点击设为桌面
     */
    public static final int VIDEO_DETAIL_DOWN_WALLPAPER = 23080004;
    /**
     * 视频详情--点击头像跳转到个人首页
     */
    public static final int VIDEO_DETAIL_ICON_JUMP = 23080005;
    /**
     * 视频详情--历史在线入口点击
     */
    public static final int MY_DOWN_HISTORY = 23080007;
    /**
     * 设置 -- 点击微信公众号跳转量
     */
    public static final int SETTING_WEIXIN_GZH_JUMP = 23080009;
    /**
     * 设置 -- 点击QQ官方群
     */
    public static final int SETTING_QQ_GZH_JUMP = 23080010;


    /**
     * 在线发布功能的打点
     *
     * @label bd：本地视频
     * @label zx：在线视频
     * @label jc:进入播放教程页
     * @label djbf:播放教程
     * @label zdbf:wifi下自动播放教程
     * @label xz:开始下载
     * @label ht:后台下载
     * @label bc:保持尺寸
     * @label gb:改变尺寸
     * @label ls:点击历史
     */
    public static final int PUBLISH_INFO = 23180001;
    /**
     * 在线发布功能的打点
     *
     * @label dy：抖音
     * @label bz：B站
     * @label ks：快手
     * @label qt：快手
     */
    public static final int PUBLISH_URL_TYPE = 23180002;

    //统计动态插件版本
    public static final int DYN_PLUGIN_VERSION = 23180003;

    //桌面播放切换次数
    public static final int LAUNCHER_PLAY_CHANGE_COUNTS = 23180004;

    //桌面播放次数，包含循环播放
    public static final int LAUNCHER_PLAY_COUNTS_INCLUDE_LOOP = 23180005;

    //端内详情页的播放次数
    public static final int APP_DETAIL_PLAY_COUNTS = 23180006;

    //端内详情页的播放次数，包含循环播放
    public static final int APP_DETAIL_PLAY_COUNTS_INCLUDE_LOOP = 23180007;

    //登陆打点: succ-成功  fail-失败
    public static final int APP_ACCOUNT_LOGIN = 23180008;

    //每日统计 声音开关
    public static final int DAILY_REPORT_SOUND_SWITCH = 23180009;

    //下载统计 succ-成功 fail-失败 start
    public static final int VIDEO_DOWNLOAD = 23180010;

    //进入本地拍摄界面
    public static final int USER_CAMERA_RECORD_ACTIVITY = 23180011;
    //本地拍摄 start-开始 finish-完成
    public static final int USER_CAMERA_RECORD = 23180011;


}
