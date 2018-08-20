package com.felink.corelib.kitset;

/**
 * Created by linliangbin on 2016/10/13.
 */

public class URLs {


    /**
     * 异常上报接口
     *
     */
    //TODO llbeing 确定异常上报是否使用桌面接口
    public static final String PANDAHOME_BASE_URL = "http://pandahome.ifjing.com/";
    public static final String EXCEPTION_UPLOAD_URL = PANDAHOME_BASE_URL + "action.ashx/commonaction/4";

    public static final String PANDAHOME_HOST = "pandahome.ifjing.com";

    public static final String PANDAHOME_VIDEO_PARSE_URL = PANDAHOME_BASE_URL + "pic.ashx/thirdvideourl";
    public static final String THIRD_PARTY_LINKS_URL = PANDAHOME_BASE_URL + "action.ashx/commonaction/14";
    public static final String THIRD_PARTY_LINKS_PARSE_URL = PANDAHOME_BASE_URL + "action.ashx/commonaction/15";

    /**
     * 动态插件升级配置接口
     */
    public static final String DYN_PLUGIN_UPGRADE_URL = PANDAHOME_BASE_URL + "action.ashx/distributeaction/5016";
}
