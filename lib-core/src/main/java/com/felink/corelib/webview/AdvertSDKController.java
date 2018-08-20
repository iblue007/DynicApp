package com.felink.corelib.webview;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.dian91.ad.AdvertSDKManager;
import com.dian91.ad.AdvertSDKManager.AdvertInfo;
import com.dian91.ad.AdvertSDKManager.CountdownCallBack;
import com.felink.corelib.kitset.TelephoneUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadBroadcastExtra;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 封装了sdk控制和下载监听
 * Author: guojianyun_dian91 
 * Date: 2016年1月6日 下午6:55:03
 */
public class AdvertSDKController {

	public static HashMap<String, AdvertInfo> downloadAdvertMap = new HashMap<String, AdvertInfo>();
	private static BroadcastReceiver mProgressReceiver;
	
	/**
	 * Description: 应用初始化时使用，注意如果有多个进程，每个进程初始化时需要调用
	 * Author: guojianyun_dian91 
	 * Date: 2016年1月6日 下午6:54:33
	 * @param context
	 */
	public static void init(Context context){
		if(mProgressReceiver == null){
			mProgressReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context context, Intent intent) {
					String url =  intent.getStringExtra(DownloadBroadcastExtra.EXTRA_DOWNLOAD_URL);
					String id =  intent.getStringExtra(DownloadBroadcastExtra.EXTRA_IDENTIFICATION);
					int state = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_STATE, DownloadState.STATE_NONE);
					if(state == DownloadState.STATE_FINISHED){
						for(String key : downloadAdvertMap.keySet()){
							if((id != null && id.contains(key)) || (url != null && url.contains(key))){
								//Log.e("xxxxxxxxxxxxxxxxxxxxxxx", "submitFinishDownloadEvent");
								AdvertSDKManager.submitFinishDownloadEvent(context, downloadAdvertMap.get(key), "");
								downloadAdvertMap.remove(key);
								break;
							}
						}
					}
				}
			};
			IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_STATE);
			context.registerReceiver(mProgressReceiver, filter);
		}
	}
	
	
	/**
	 * Description: 获取广告信息(从9004接口获取)
	 * Author: guojianyun_dian91 
	 * Date: 2015年12月31日 下午4:30:23
	 * @param ctx
	 * @param pos 广告投放位置，多个用逗号隔开例如0,1,2，位置值请对照广告位置标识 
	 * @return
	 */
	public static List<AdvertInfo> getAdvertInfos(Context ctx, String pos) {
		return AdvertSDKManager.getAdvertInfos(ctx, pos);
	}
	
	/**
	 * Description: 获取广告信息(从9004接口获取)
	 * Author: guojianyun_dian91 
	 * Date: 2015年12月29日 下午3:53:32
	 * @param ctx
	 * @param pos 广告投放位置，多个用逗号隔开例如0,1,2，位置值请对照广告位置标识 
	 * @param width 广告位宽度
	 * @param height 广告位高度
	 * @return
	 */
//	public static List<AdvertInfo> getAdvertInfos(Context ctx, String pos, int width, int height) {
//		return AdvertSDKManager.getAdvertInfos(ctx, pos, width, height);
//	}
	
	/**
	 * Description: 展示广告时调用
	 * Author: guojianyun_dian91 
	 * Date: 2015年12月31日 下午4:31:28
	 * @param act
	 * @param adverInfo
	 */
	public static void submitShowEvent(final Context act, final Handler mHandler, final AdvertInfo adverInfo){
		//Log.e("xxxxxxxxxxxxxxxxxxxxxxx", "submitShowEvent");
		AdvertSDKManager.submitShowEvent(act, mHandler, adverInfo);
	}
	
	/**
	 * Description: 点击广告时调用
	 * Author: guojianyun_dian91 
	 * Date: 2015年12月31日 下午4:31:48
	 * @param act
	 * @param adverInfo
	 */
	public static void submitClickEvent(final Context act, final Handler mHandler, final AdvertInfo adverInfo){
		//Log.e("xxxxxxxxxxxxxxxxxxxxxxx", "submitClickEvent");
		AdvertSDKManager.submitClickEvent(act, mHandler, adverInfo);
	}
	
	/**
	 * Description: 开始下载App时调用
	 * Author: guojianyun_dian91 
	 * Date: 2015年12月31日 下午4:33:33
	 * @param ctx
	 * @param adverInfo
	 * @param downLoadKey 可以唯一标示该下载
	 */
	public static void submitStartDownloadEvent(Context ctx, AdvertInfo adverInfo, String downLoadKey){
		//Log.e("xxxxxxxxxxxxxxxxxxxxxxx", "submitStartDownloadEvent");
		AdvertSDKManager.submitStartDownloadEvent(ctx, adverInfo, "");
		downloadAdvertMap.put(downLoadKey, adverInfo);
	}
	
	public static void submitECShowURL(final Context ctx, final int modelID, final int resourceID){
		//Log.e("xxxxxxxxxxxxxxxxxxxxxxx", "submitECShowURL");
		if(TelephoneUtil.isNetworkAvailable(ctx)){
			AdvertSDKManager.submitECShowURL(ctx, modelID, resourceID);
		}
	}

	public static void submitExposureURL(final Context ctx, AdvertInfo adverInfo){
		//Log.e("xxxxxxxxxxxxxxxxxxxxxxx", "submitExposureURL");
		if(TelephoneUtil.isNetworkAvailable(ctx)){
			AdvertSDKManager.submitExposureEvent(ctx, adverInfo);
		}
	}
	
	/**
	 * Description: 增加广告标识
	 * Author: guojianyun 
	 * Date: 2016年9月5日 下午3:02:09
	 * @param ctx
	 * @param mParent
	 * @param adverInfo
	 */
	public static void addAdvertLogoView(Context ctx, FrameLayout mParent, final AdvertInfo adverInfo){
		AdvertSDKManager.addAdvertLogoView(ctx, mParent, adverInfo);
	}
	
	/**
	 * Description: 增加广告标识与倒计时
	 * 				生成第三方广告logo(位于左上角) + "广告"view + 倒数3s view(位于右上角)
	 * Author: guojianyun 
	 * Date: 2016年9月5日 下午3:15:38
	 * @param ctx
	 * @param mParent
	 * @param adverInfo
	 * @param mCallback
	 */
	public static void addAdvertLogoAndCountdownView(Context ctx, FrameLayout mParent, final AdvertInfo adverInfo, CountdownCallBack mCallback){
		AdvertSDKManager.addAdvertLogoAndCountdownView(ctx, mParent, adverInfo, mCallback);
	}

	/**
	 * 生成第三方广告logo(位于左上角) + "跳过"view + 倒数3s view(位于右上角) + "广告"view(位于右下角)
	 * @param ctx
	 * @param mParent
	 * @param adverInfo
	 * @param adTextViewMarginBottom
	 * @param mCountdownCallBack
	 * @param mSkipCallback
	 */
	public static void addAdvertLogoAndCountdownViewV2(Context ctx, FrameLayout mParent, final AdvertInfo adverInfo, int adTextViewMarginBottom,
													   CountdownCallBack mCountdownCallBack, AdvertSDKManager.SkipCallBack mSkipCallback){
		AdvertSDKManager.addAdvertLogoAndCountdownViewV2(ctx, mParent, adverInfo, adTextViewMarginBottom, mCountdownCallBack, mSkipCallback);
	}
	
	/**
	 * Description: 初始化WebView设置
	 * Author: guojianyun_dian91 
	 * Date: 2016年1月6日 下午6:56:12
	 * @param act
	 * @param mWebView
	 * @param advertItem
	 */
	public static void initWebView(final Activity act, WebView mWebView, final AdvertInfo advertItem, final Intent callBackIntent){
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setSupportZoom(true);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				submitClickEvent(act, null, advertItem);
				startAdSdkBrowserActivity(act,"", advertItem, url, callBackIntent);
				act.finish();
				return true;
			}
		});
	}
	
	/**
	 * Description: 调用内置webview的Activity
	 * Author: guojianyun_dian91 
	 * Date: 2016年1月6日 下午6:55:30
	 * @param advertItem
	 * @param url
	 */
	public static void startAdSdkBrowserActivity(Context ctx, String title,AdvertInfo advertItem, String url, Intent callBackIntent) {
		if(ctx == null)
			return;
		//AdvertSDKBrowserActivity.advertItem = advertItem;
		AdvertSDKBrowserActivity.callBackIntent = callBackIntent;
		Intent intent = new Intent();
		intent.setClass(ctx, AdvertSDKBrowserActivity.class);
		intent.putExtra(AdvertSDKBrowserActivity.EXTRA_URL, url);
		intent.putExtra(AdvertSDKBrowserActivity.EXTRA_TITLE, title);
		Bundle bundle = new Bundle();
		bundle.putSerializable(AdvertSDKBrowserActivity.ADVERTITEM, advertItem);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
	}
	
	/**
	 * Description: 调用内置webview的Activity(桌面进程)
	 * Author: caizp 
	 * Date: 2016年3月16日 下午5:35:30
	 * @param act
	 * @param advertItem
	 * @param url
	 */
//	public static void startAdSdkBrowserActivityForLauncherProcess(Context ctx, AdvertInfo advertItem, String url, Intent callBackIntent) {
//		if(ctx == null)
//			return;
//		AdvertSDKBrowserActivityForLauncherProcess.advertItem = advertItem;
//		AdvertSDKBrowserActivityForLauncherProcess.callBackIntent = callBackIntent;
//		Intent intent = new Intent();
//		intent.setClass(ctx, AdvertSDKBrowserActivityForLauncherProcess.class);
//		intent.putExtra(AdvertSDKBrowserActivity.EXTRA_URL, url);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		ctx.startActivity(intent);
//	}
	
}
