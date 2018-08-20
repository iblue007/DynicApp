package com.felink.corelib.analytics;

/**
 * CV统计常量
 * 编号和内容找黄千里拿，切勿自行添加
 * <p>Title: CvAnalysisConstant</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 *
 * @author MaoLinnan
 * @date 2015年11月23日
 */
public class CvAnalysisConstant {
    /***********************************
     * resType常量
     *******************************************/
    public static final int RESTYPE_ADS = 1;//广告
    public static final int RESTYPE_APP = 2;//应用
    public static final int RESTYPE_THEME = 3;//主题
    public static final int RESTYPE_RING = 4;//铃声
    public static final int RESTYPE_WALLPAPER = 5;//壁纸
    public static final int RESTYPE_FONT = 6;//字体
    public static final int RESTYPE_THEME_SERIES = 7;//桌面主题系列
    public static final int RESTYPE_PASTER = 8;//桌面贴纸
    public static final int RESTYPE_MITO = 9;//美图作品
    public static final int RESTYPE_POTO = 10;//Po图
    public static final int RESTYPE_THEME_MODULE = 11;//主题模块资源
    public static final int RESTYPE_LINKS = 12;//网址链接
    public static final int RESTYPE_CUSTOM_TIPS = 13;//自定义标签
    public static final int RESTYPE_CARDS = 14;//卡片
    public static final int RESTYPE_VIDEO = 21;//视频
    public static final int RESTYPE_OTHER = 100;//其他，不能为空

    /***********************************CV点位*****************************************/
    /**
     * 视频浏览量
     */
    //个人中心
    public static final int PERSONAL_CENTER_PAGEID = 98010001;
    //视频详情页
    public static final int VIDEO_DETAIL_PAGEID = 98020001;
    //桌面视频播放页
    public static final int LAUNCHER_VIDEO_PLAY_PAGEID = 98030001;
    //桌面loading页广告
    public static final int LAUNCHER_LOADING_AD_PAGEID = 98030002;
    public static final int LAUNCHER_LOADING_AD_POSITIONID_SINGLE = 98030003;//桌面图标点击进入
    public static final int LAUNCHER_LOADING_AD_POSITIONID_DOUBLE = 98030004;//桌面壁纸双击进入
    public static final int LAUNCHER_FX_AD_ANALYTIC = 98030005;//发现页面广告统计
    public static final int LAUNCHER_FX_AD_ANALYTIC_SHOW_CLICK = 98030006;//数据列表广告展示和点击
    public static final int LAUNCHER_TJ_AD_ANALYTIC = 98030007;//精选（推荐）页面广告统计
    public static final int LAUNCHER_TJ_AD_ANALYTIC_SHOW_CLICK = 98030008;//数据列表广告展示和点击

    public static final int LAUNCHER_RECOMMEND_IN_NUMBER = 98030009;//精选TAB进入量
    public static final int LAUNCHER_FIND_IN_NUMBER = 98030010;//发现TAB进入量
    public static final int LAUNCHER_FOLLOW_IN_NUMBER = 98030011;//关注TAB进入量
    public static final int LAUNCHER_CATEGORY_IN_NUMBER = 98030012;//分类TAB进入量
    public static final int LAUNCHER_VIDEO_DETAIL_IN_NUMBER = 98030013;//视频详情页进入量
    //客户端视频播放量
    public static final int VIDEO_DETAIL_PLAY_PAGEID = 98030014;

    // 客户端进入量
    public static final int VIDEO_APP_PAGE = 98030015;

    //客户端内容曝光量
    public static final int VIDEO_APP_RESOUCE_EXPOSED_PAGE = 98030016;

    //客户端使用时长统计
    public static final int VIDEO_APP_LIFE_TIME_PAGE = 98030017;




}
