package com.felink.corelib.analytics;

import com.felink.corelib.kitset.ThreadUtil;
import com.felink.corelib.net.VideopaperHttpRequestParam;
import com.felink.corelib.net.base.HttpCommon;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 主题下载路径统计
 * <p>Title: ThemeDownloadPathAnalysis</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 *
 * @author MaoLinnan
 * @date 2014年12月22日
 */
public class VideoDownloadPathAnalysis {
    public static int SP_UNKNOWN = 99;//未知来源

    public static int SP_RECOMMEND = 1;//精选
    public static int SP_FIND = 2;//发现
    public static int SP_FOLLOW = 3;//关注
    public static int SP_SORT_STARS = 4;//分类-影视明星
    public static int SP_SORT_WEBSITE = 5;//分类-网络红人
    public static int SP_SORT_CARTOON = 6;//分类-卡通动漫
    public static int SP_SORT_SCENERY = 7;//分类-风景静物
    public static int SP_SORT_ANIMAL = 8;//分类-动物萌宠
    public static int SP_SORT_OTHER = 9;//分类-其他
    public static int SP_SEARCH = 10;//搜索

    /**
     * 统计SP全局变量
     * 每次进入一个路径开端的时候赋值
     */
    private static int SP = SP_UNKNOWN;

    public static void setVideoDownloadPathAnalysisSP(int AnalysisSP) {
        VideoDownloadPathAnalysis.SP = AnalysisSP;
    }

    public static int getVideoDownloadPathAnalysisSP() {
        return SP;
    }

    /**
     * 数值和4041接口有对应关系
     */
    public static int FLAG_DOWNLOAD_Video = 1;//开始下载
    public static int FLAG_DOWNLOAD_SUCCESS = 4;//下载成功
    public static int FLAG_INSTALL_SUCCESS = 5;//安装成功
    public static int FLAG_INSTALL_FAIL = 6;//安装失败
    public static int FLAG_DOWNLOAD_FAIL = 8;//下载失败
    public static int FLAG_CLICK_PAY = 10;//点击付费
    public static int FLAG_PAY_SUCCESS = 14;//付费成功
    public static int FLAG_PAY_FAIL = 15;//付费失败

    /**
     * 资源类型(主题=2)
     */
    public final static int RES_TYPE_THEME = 2;
    /**
     * 资源类型(编辑推荐主题系列=36)
     */
    public final static int RES_TYPE_THEME_SERIES = 36;
    /**
     * 资源类型(用户推荐主题系列=61)
     */
    public final static int RES_TYPE_RECOMMEND_CUSTOM_THEME_SERIES = 61;
    /**
     * 资源类型(风格=62)
     */
    public final static int RES_TYPE_STYLE = 62;
    /**
     * 资源类型(单个视频壁纸=71)
     */
    public final static int RES_TYPE_VIDEO_PAPER = 71;
    /**
     * 资源类型(系列视频壁纸=73)
     */
    public final static int RES_TYPE_SERIES_VIDEO_PAPER = 73;

    /**
     * 4041统计主题属性(免费主题不传，服务端默认为0)
     */
    public static int RES_ATTRIBUTES_FREE = 0;
    /**
     * 4041统计主题属性(彩虹主题)
     */
    public static int RES_ATTRIBUTES_RAINBOW = 1;
    /**
     * 4041统计主题属性(付费主题)
     */
    public static int RES_ATTRIBUTES_CHARGE = 2;
    /**
     * 4041统计主题属性(试用主题)
     */
    public static int RES_ATTRIBUTES_TRIAL = 4;

    private static String THEME_DISTRIBUTE_ANALYSIS_PATH = "http://pandahome.ifjing.com/action.ashx/ThemeAction/";

    /**
     * 统计的PostionType
     */
    public static int POSTION_TYPE_DEFAULT = 0;//默认值
    public static int POSTION_TYPE_SPECIAL = 1;//专辑
    /*public static int POSTION_TYPE_CLASSIFY = 2;//分类
    public static int POSTION_TYPE_RECOMMENDED_TOPICS = 3;//推荐专题*/
    public static int POSTION_TYPE_V8_PERSONAL_TAG = 4;//V8个性化标签
    private static int postionType = POSTION_TYPE_DEFAULT;

    public static void setVideoDownloadPathAnalysisPostionType(int postionType) {
        VideoDownloadPathAnalysis.postionType = postionType;
    }

    public static int getVideoDownloadPathAnalysisPostionType() {
        return postionType;
    }

    /**
     * 统计的PostionTypeId,用来传递专辑/分类/推荐专题的id
     */
    private static int postionTypeId = POSTION_TYPE_DEFAULT;

    public static void setVideoDownloadPathAnalysisPostionTypeId(int postionTypeId) {
        VideoDownloadPathAnalysis.postionTypeId = postionTypeId;
    }

    public static int getThemeDownloadPathAnalysisPostionTypeId() {
        return postionTypeId;
    }

    /**
     * 发送主题下载统计sp
     * <p>Title: sendVideoDownloadPathAnalysisSP</p>
     * <p>Description: </p>
     * serThemeId 服务端下发的原始id
     * StatType 当前状态类型
     * resAttributes 主题属性 0-免费主题(客户端不传，服务端默认为0); 1-彩虹主题; 2-付费主题; 4试用主题
     *
     * @author maolinnan_350804
     */
    public static void sendVideoDownloadPathAnalysisSP(final String serThemeId, final String themeSp, final int StatType, final int postionType, final int postionTypeId, final int resAttributes) {
        sendVideoDownloadPathAnalysisSP(serThemeId, VideoDownloadPathAnalysis.RES_TYPE_THEME, themeSp, StatType, postionType, postionTypeId, resAttributes);
    }

    /**
     * 发送主题下载统计sp
     * <p>Title: sendThemeDownloadPathAnalysisSP</p>
     * <p>Description: </p>
     * serThemeId 服务端下发的原始id
     * StatType 当前状态类型
     * resAttributes 主题属性 0-免费主题(客户端不传，服务端默认为0); 1-彩虹主题; 2-付费主题; 4试用主题
     *
     * @author maolinnan_350804
     */
    public static void sendVideoDownloadPathAnalysisSP(final String serThemeId, final int resType, final String themeSp, final int StatType, final int postionType, final int postionTypeId, final int resAttributes) {
//		Log.e("个性化资源统计", "当前统计主题id="+serThemeId+";统计SP="+themeSp+";统计类型="+StatType);
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                int acitonCode = 4041;
                String jsonParams = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ResId", Long.parseLong(serThemeId));
                    jsonObject.put("ResType", resType);
                    jsonObject.put("Position", Integer.parseInt(themeSp));
                    jsonObject.put("StatType", StatType);
                    if (RES_ATTRIBUTES_FREE != resAttributes) {
                        jsonObject.put("ResAttributes", resAttributes);
                    }
                    if (postionType != POSTION_TYPE_DEFAULT && postionTypeId != POSTION_TYPE_DEFAULT) {
                        jsonObject.put("PostionType", postionType);
                        jsonObject.put("PostionTypeID", postionTypeId);
                    }
                    jsonParams = jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HashMap<String, String> paramsMap = new HashMap<String, String>();
                VideopaperHttpRequestParam.addGlobalLauncherRequestValue(paramsMap, jsonParams);
                HttpCommon httpCommon = new HttpCommon(THEME_DISTRIBUTE_ANALYSIS_PATH + acitonCode);
                httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
//				if(csResult.getResultCode() == 0){
//					Log.e("个性化资源统计", "该统计已经向服务端发送");
//				}
            }
        });
    }

    /**
     * 发送主题下载统计sp,专门用来统计下载失败的
     * <p>Title: sendThemeDownloadPathAnalysisSP</p>
     * <p>Description: </p>
     *
     * @param serThemeId
     * @param themeSp
     * @param downloadFaileUrl 下载地址
     * @param netinfo          网络状态
     * @param postionType
     * @param postionTypeId
     * @param resAttributes
     * @author maolinnan_350804
     */
    public static void sendVideoDownloadPathAnalysisSP(final String serThemeId, final String themeSp, final String downloadFaileUrl, final String netinfo, final int postionType, final int postionTypeId, final int resAttributes) {
        sendVideoDownloadPathAnalysisSP(serThemeId, 2, themeSp, downloadFaileUrl, netinfo, postionType, postionTypeId, resAttributes);
    }

    /**
     * 发送主题下载统计sp,专门用来统计下载失败的
     * <p>Title: sendThemeDownloadPathAnalysisSP</p>
     * <p>Description: </p>
     *
     * @param serThemeId
     * @param resType          资源类型，普通主题为2，主题系列为36
     * @param themeSp
     * @param downloadFaileUrl 下载地址
     * @param netinfo          网络状态
     * @param postionType
     * @param postionTypeId
     * @param resAttributes
     * @author maolinnan_350804
     */
    public static void sendVideoDownloadPathAnalysisSP(final String serThemeId, final int resType, final String themeSp, final String downloadFaileUrl, final String netinfo, final int postionType, final int postionTypeId, final int resAttributes) {
//		Log.e("个性化资源统计", "当前统计主题id="+serThemeId+";统计SP="+themeSp+";统计类型="+StatType);
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                int acitonCode = 4041;
                String jsonParams = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ResId", Long.parseLong(serThemeId));
                    jsonObject.put("ResType", resType);
                    jsonObject.put("Position", Integer.parseInt(themeSp));
                    jsonObject.put("StatType", FLAG_DOWNLOAD_FAIL);
                    jsonObject.put("downloadurl", encodeAttrValue(downloadFaileUrl));
                    jsonObject.put("netinfo", encodeAttrValue(netinfo));
                    if (RES_ATTRIBUTES_FREE != resAttributes) {
                        jsonObject.put("ResAttributes", resAttributes);
                    }
                    if (postionType != POSTION_TYPE_DEFAULT && postionTypeId != POSTION_TYPE_DEFAULT) {
                        jsonObject.put("PostionType", postionType);
                        jsonObject.put("PostionTypeID", postionTypeId);
                    }
                    jsonParams = jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HashMap<String, String> paramsMap = new HashMap<String, String>();
                VideopaperHttpRequestParam.addGlobalLauncherRequestValue(paramsMap, jsonParams);
                HttpCommon httpCommon = new HttpCommon(THEME_DISTRIBUTE_ANALYSIS_PATH + acitonCode);
                httpCommon.getResponseAsCsResultPost(paramsMap, jsonParams);
//				if(csResult.getResultCode() == 0){
//					Log.e("个性化资源统计", "该统计已经向服务端发送");
//				}
            }
        });
    }

    /**
     * 对参数进行转码
     * <p>Title: encodeAttrValue</p>
     * <p>Description: </p>
     *
     * @param value
     * @return
     * @author maolinnan_350804
     */
    private static String encodeAttrValue(String value) {
        String returnValue = "";
        try {
            value = URLEncoder.encode(value + "", "UTF-8");
            returnValue = value.replaceAll("\\+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
