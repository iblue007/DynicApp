package com.felink.corelib.analytics;

import android.content.Context;

import com.baidu91.tm.analytics.TMAnalytics;
import com.felink.corelib.config.BaseConfig;
import com.felink.corelib.kitset.ChannelUtil;

/**
 * CV统计提交类
 * <p>Title: CvAnalysis</p>
 * <p>Description: </p>
 * <p>Company: ND</p>
 * @author    MaoLinnan
 * @date       2015年11月23日
 */
public class CvAnalysis {
	private CvAnalysis(){};
	
	/**
	 * 初始化CV统计系统
	 * <p>Title: startUp</p>
	 * <p>Description: </p>
	 * @param context
	 * @author maolinnan_350804
	 */
	public static void init(Context context){
		TMAnalytics.init(context, BaseConfig.APPID, ChannelUtil.getChannel(context));
	}
	
	/**
	 * 提交Activity进入
	 * <p>Title: submitPageStartEvent</p>
	 * <p>Description: </p>
	 * @param ctx
	 * @param pageId	页面id
	 * @author maolinnan_350804
	 */
	public static void submitPageStartEvent(Context ctx,int pageId){
		//Log.e("======","======submitPageStartEvent-pageId:"+pageId);
		TMAnalytics.submitPageStartEvent(ctx, pageId);
	}
	
	/**
	 * 提交Activity退出
	 * <p>Title: submitPageEndEvent</p>
	 * <p>Description: </p>
	 * @param ctx
	 * @param pageId	页面id
	 * @author maolinnan_350804
	 */
	public static void submitPageEndEvent(Context ctx,int pageId){
		//Log.e("======","======submitPageEndEvent-pageId:"+pageId);
		TMAnalytics.submitPageEndEvent(ctx, pageId);
	}
	
	/**
	 * 提交展示事件
	 * @param ctx
	 * @param pageId	页面id
	 * @param posId		位置id
	 * @param resId		资源id
	 * @param resType	资源类型
	 * @param sourceId	广告来源id
	 */
	public static void submitShowEvent(Context ctx,int pageId,int posId,int resId,int resType, int sourceId){
		if (resId == 0){//广告资源位为0的时候不进行上报
			return;
		}
		TMAnalytics.submitShowEvent(ctx, pageId, posId, resId, resType, sourceId);
	}
	
	/**
	 * 提交展示事件
	 * <p>Title: submitShowEvent</p>
	 * <p>Description: </p>
	 * @param ctx
	 * @param pageId	页面id
	 * @param posId		位置id
	 * @param resId		资源id
	 * @param resType	资源类型
	 * @param resType	资源类型
	 * @author maolinnan_350804
	 */
	public static void submitShowEvent(Context ctx,int pageId,int posId,int resId,int resType){
		if (resId == 0){//广告资源位为0的时候不进行上报
			return;
		}
		TMAnalytics.submitShowEvent(ctx, pageId, posId, resId, resType);
	}
	
	/**
	 * 提交点击事件
	 * @param ctx
	 * @param pageId	页面id
	 * @param posId		位置id
	 * @param resId		资源id
	 * @param resType	资源类型
	 * @param sourceId	广告来源id
	 */
	public static void submitClickEvent(Context ctx,int pageId,int posId,int resId,int resType, int sourceId){
		if (resId == 0){//广告资源位为0的时候不进行上报
			return;
		}
		TMAnalytics.submitClickEvent(ctx, pageId, posId, resId, resType, sourceId);
	}
	
	/**
	 * 提交点击事件
	 * <p>Title: submitClickEvent</p>
	 * <p>Description: </p>
	 * @param ctx
	 * @param pageId	页面id
	 * @param posId		位置id
	 * @param resId		资源id
	 * @param resType	资源类型
	 * @author maolinnan_350804
	 */
	public static void submitClickEvent(Context ctx,int pageId,int posId,int resId,int resType){
		if (resId == 0){//广告资源位为0的时候不进行上报
			return;
		}
		TMAnalytics.submitClickEvent(ctx, pageId, posId, resId, resType);
	}
	
	/**
	 * 提交CPC统计
	 * <p>Title: submitCpcAnalytisConstant</p>
	 * <p>Description: </p>
	 * @param ctx
	 * @param posId
	 * @param resId
	 * @param resType
	 * @param sourceId
	 * @author maolinnan_350804
	 */
	public static void submitCpcAnalytisConstant(Context ctx,int posId,int resId,int resType, int sourceId){
		TMAnalytics.submitCpcClickEvent(ctx, posId,resId, resType, sourceId);
	}
}
