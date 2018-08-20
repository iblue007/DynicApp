package com.nd.hilauncherdev.analysis;

/**
 * description: <br/>
 * author: dingdj<br/>
 * data: 2014/8/28<br/>
 */
public class AppAnalysisConstant {

    /**
     * APP 统计状态
     */
    /**
     * 应用分发通用下载源：助手
     */
    //public static final int DOWNLOAD_FROM_91ASSIST_POOL = 0;

    /**
     * 应用分发-开始下载
     */
    public static final int APP_DISTRIBUTE_STEP_START = 1;

    /**
     * 应用分发-浏览
     */
    public static final int APP_DISTRIBUTE_STEP_BROWSE = 2;

    /**
     * 应用分发-下载成功
     */
    public static final int APP_DISTRIBUTE_STEP_DOWNLOAD_SUCCESS = 4;

    /**
     * 应用分发-安装成功
     */
    public static final int APP_DISTRIBUTE_STEP_INSTALLED_SUCCESS = 5;

    /**
     * 应用分发-安装失败
     */
    public static final int APP_DISTRIBUTE_STEP_INSTALLED_FAILED = 6;

    /**
     * 应用分发-下载失败
     */
    public static final int APP_DISTRIBUTE_STEP_DOWNLOAD_FAILED = 8;

    /**
     * 应用分发-激活
     */
    public static final int APP_DISTRIBUTE_STEP_ACTIVE = 10;


    /**
     * 资源类型
     */
   /* public static final int TYPE_SOFT = 1;                 //软件
    public static final int TYPE_THEME = 2;                //主题
    public static final int TYPE_RING = 3;                 //铃声
    public static final int TYPE_WALLPAPER = 4;            //壁纸
    public static final int TYPE_THEME_FONT = 24;          //主题资源
    public static final int TYPE_RESOURCE_MODULE = 50;     //资源模块*/


    /**
     * 渠道 服务端根据上传的sp来生成对应的placeId 代表不同的位置
     * 统一请求下载包的sp
     * 11,12服务端也已经开放，客户端暂时没用到。
     */
    //public static final int SP_ASSIST_91 = 0;                   //从91助手下载的包
    // public static final int SP_LAUNCHER_CHANNEL_APP = 1;        //桌面渠道包
    public static final int SP_THEME_CORRELATION_APP = 2;       //主题相关应用（主题预览底下推荐的那些包）
    public static final int SP_THIRD_PARTY_APP = 3;             //第三方APP包（主题推荐的包放的位置）
    public static final int SP_HOTWORD_RECOMMEND_APP = 4;       //匣子热词推荐应用
    //public static final int SP_APP_STORE_RECOMMEND_APP = 5;     //AppStore推荐应用
    //public static final int SP_THEME_SKIN_PEELER_WIDGET = 6;    //主题换肤小部件
    public static final int SP_OLD_LAUNCHER_ICON_RECOMMEND = 7; //桌面icon推荐（为了兼容5.6，当前已经废弃了，最好别用这个）
    public static final int SP_OPERATIONS_CAMPAIGN = 8;         //运营活动
    public static final int SP_NEW_LAUNCHER_ICON_RECOMMEND = 9; //桌面icon推荐，由服务端配置（5.6.1开始有）
    public static final int SP_DEFAULT_CHANNEL_APP =10;         //不知道要啥的，都调用这个，这里放了所有的渠道包。
    public static final int SP_IN_APP =11;                      //Inapp 推广。
    public static final int SP_WIDGET_RECOMMEND_APP =12;        //桌面预置、匣子插件商城、新手引导页面推荐
    public static final int SP_DOCK_RECOMMEND_APP =13;          //目前只有浏览器，后续可能有拨号软件等
    public static final int SP_WEISHI_RECOMMEND_APP =14;        //进入深度清理，底部推荐下载百度手机卫士(功能推荐)
    public static final int SP_SEARCH_RECOMMEND_ONLINE_APP =15; //搜索推荐相关的在线应用
    //public static final int SP_HOT_GAME_RECOMMEND_APP =16;      //热门游戏
    //public static final int SP_HOT_SOFT_RECOMMEND_APP =17;      //热门软件
    public static final int SP_NOTIFICATION_PUSH_RECOMMEND_APP =18;      //通知栏推送
    public static final int SP_NAV_HOTWORD_RECOMMEND_APP = 19;           //导航或热词推荐浏览器
    public static final int SP_GEXINGHUA_HUNDA_RECOMMEND_APP = 20;       //个性化混搭模块
    public static final int SP_INTELLIJ_UPGRADE_RECOMMEND_APP = 21;      //智能升级
    //public static final int SP_MOVE_DESK_RECOMMEND_APP = 22;             //桌面搬家推荐
    public static final int SP_WIFI_DOWNLOAD_APP = 23;                   //wifi预下载
    // public static final int SP_FLASH_ADS_APP = 24;                       //闪屏广告
    // public static final int SP_ROOT_RECOMMEND_APP = 25;                 //Root 推荐
    // public static final int SP_MOLIQIU_RECOMMEND_APP = 26;               //魔力球推荐
    public static final int SP_JI_FENG_QIANG = 27;                       //积分墙推荐App
    // public static final int SP_INAPP_SYNCHRONOUS_RECOMMENDED_VIDEO  = 28;//inapp同步推荐视屏
    public static final int SP_GAME_HOTWORD_RECOMMEND_APP  = 29;//热门游戏同步干预app热词（会同步到客户端热词推荐）
    public static final int SP_APP_HOTWORD_RECOMMEND_APP  = 30;//应用商店同步干预app热词（会同步到客户端热词推荐）
    public static final int SP_GAME_THEME_RECOMMEND_APP  = 31;//游戏主题推荐游戏app
    public static final int SP_NO_ROOT_RECOMMEND_APP  = 32;//没root推荐root软件
    //public static final int SP_GAME_AND_APPMARKET_LOADING = 33;//游戏中心和应用商店loading推荐应用
    public static final int SP_SEM_CHANNEL_LAUNCHER_RECOMMEND_APP = 34;//sem渠道推荐软件
    public static final int SP_LIGATURE_UPGRADE_APP = 35;//产品升级捆绑推荐应用
    public static final int SP_LAUNCHER_NOTIFICATION_APP = 36;//桌面通知推送APP
    public static final int SP_APP_INSTALLED_NECESSARY_RECOMMEND = 37;//应用商店-装机必备应用推荐
    public static final int SP_GAME_INSTALLED_NECESSARY_RECOMMEND = 38;//热门游戏-装机必备游戏推荐
    public static final int SP_TOP_MENU_BOUTIQUE_RES_APP = 39;//下滑菜单精品资源APP推荐
    public static final int SP_TOP_MENU_APP_RECOMMEND = 40;//下滑菜单应用推荐
    public static final int SP_LAUNCHER_RECOMMEND_OWN_APP = 41;//桌面推荐自有应用(如：一元有宝等)
    public static final int SP_MARKET_RECOMMEND_APP_IN_DIALOG = 42; //应用商店&热门游戏弹框推荐应用
    /**
     * 由服务端来入库数据 sp保留
     */
   /* public static final int SP_LAUNCHER_UNINSTALL_RECOMMEND_APP = 98;    //桌面卸载推荐*/


}
