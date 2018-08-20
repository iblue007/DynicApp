package com.felink.corelib.net;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年05月04日 12:55.</br>
 * @update: </br>
 */

public class ApiCodes {

    /**
     * 获取分类列表
     */
    public static final String CODE_GET_CATEGORY_LIST = "4011";
    /**
     * 获取资源详情
     */
    public static final String CODE_GET_RESOURCE_DETAIL = "4022";
    /**
     * 获取分类下的标签列表
     */
    public static final String CODE_GET_CATEGORY_TAG_LIST = "4031";
    /**
     * 获取标签对应的数据列表
     */
    public static final String CODE_GET_LIST_BY_TAG = "4032";
    /**
     * 获取视频壁纸系列资源详情
     */
    public static final String CODE_GET_SERIES_RESOURCE_DETAIL = "4047";
    /**
     * 获取用户ID下的资源列表
     */
    public static final String CODE_GET_RESOURCES_BY_USERID = "4075";
    /**
     * 获取个性化发现列表
     */
    public static final String CODE_GET_NOVEL_RESOURCES = "4078";
    /**
     * 根据标签获取资源
     */
    public static final String CODE_GET_RESOURCES_BY_TAG = "4079";
    /**
     * 获取标签列表
     */
    public static final String CODE_GET_TAGS = "4080";
    /**
     * 获取话题 数据列表(最新、最热)
     */
    public static final String CODE_GET_TOPIC_DATA_TAGS = "4097";
    /**
     * 获取话题标签列表
     */
    public static final String CODE_GET_TOPIC_TAGS = "4098";
    /**153.	获取单个话题详细信息*/
    public static final String CODE_GET_TOPIC_DETAIL_TAGS = "4099";
    /**获取用户奖励金信息*/
    public static final int CODE_GET_USER_REWARD_INFO_TAGS = 6026;
    /***获取用户的奖励金列表（6027)*/
    public static final int CODE_GET_AWARD_LIST_TAGS = 6027;
    /***用户奖励金提现记录*/
    public static final String CODE_GET_PRESENT_RECORD_TAGS = "6028";
    /***用户奖励金提现*/
    public static final String CODE_GET_REWARD_INCASH_TAGS = "6029";
    /***提现配置*/
    public static final String CODE_GET_REWARD_INCASH_CONFIG_TAGS = "6030";

    public static final class Po {
        /**
         * 获取我的关注/订阅的用户ID列表
         */
        public static final String CODE_GET_MY_ATTENTION_SUBSCRIPTION_USERID_LIST = "1075";

        /**
         * 获取评论列表
         */
        public static final String CODE_GET_COMMENT_LIST = "1021";
        /**
         * 添加评论
         */
        public static final String CODE_ADD_COMMENT = "1022";
        /**
         * 删除评论
         */
        public static final String CODE_DELETE_COMMENT = "1023";

        /**
         * 获取用户相关权限列表
         */
        public static final String CODE_GET_POWER_LIST_FOR_USERS = "1076";
        /**
         * 置换评论权限开关
         */
        public static final String CODE_SWITCH_COMMENT_ACCESS = "1080";
    }
}
