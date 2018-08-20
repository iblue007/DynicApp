package com.felink.corelib.webview;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dian91.ad.AdvertSDKManager;
import com.dian91.ad.AdvertSDKManager.AdvertInfo;
import com.felink.corelib.R;
import com.felink.corelib.config.BaseConfig;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.AdvertSdkController;
import com.felink.corelib.kitset.ApkInstaller;
import com.felink.corelib.kitset.MessageUtils;
import com.felink.corelib.kitset.StatusBarUtil;
import com.felink.corelib.kitset.SystemUtil;
import com.felink.corelib.kitset.TelephoneUtil;
import com.felink.corelib.kitset.ThreadUtil;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.AbstractDownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Description: 内置webview的Activity
 * Author: guojianyun_dian91 
 * Date: 2016年1月6日 下午6:58:51
 */
public class AdvertSDKBrowserActivity extends Activity implements OnClickListener {

	public static final String EXTRA_URL = "url";
	public static final String EXTRA_TITLE = "title";
	public static final String ADVERTITEM = "advertitem";
	public static HashMap<String, AdvertInfo> downloadAdvertMap = new HashMap<String, AdvertSDKManager.AdvertInfo>();
	/**
	 * 706之前的版本没有传递该参数，因此没有下载的打点统计
	 */
	public static final String EXTRA_AD = "ad";
	public AdvertInfo advertItem = null;
	public static Intent callBackIntent = null;
	private WebView mWebView;
	private String url;
	private String titleExtra;
	private TextView titleTv;
	private View progressBarContainer;
	private LinearLayout noNetwork;// 无网络界面
	private boolean hasSetTitle = false;
	private String from = JDSdkToolForCommon.FROM_POSITION_LOADING;

	private LinearLayout retreat, advance, home, openBrowser;// 前进，后退，home和打开其他浏览器
    private ImageView retreatImage, advanceImage, homeImage, openBrowserImage;// 控制键对应的image

	private CustomWebViewClient mWebViewClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_advert_sdk_browser);
		StatusBarUtil.from(this)
				.setLightStatusBar(true)
				.process();

		titleExtra = getIntent().getStringExtra(EXTRA_TITLE);
		url = getIntent().getStringExtra(EXTRA_URL);
		from = getIntent().getStringExtra(JDSdkToolForCommon.INTENT_FROM_TAG);
		advertItem = getIntent().getParcelableExtra(ADVERTITEM);
		if(TextUtils.isEmpty(from)){
			from = JDSdkToolForCommon.FROM_POSITION_LOADING;
		}
		String adString = getIntent().getStringExtra(EXTRA_AD);
		if(TextUtils.isEmpty(url)){
			finish();
		}

		if(JDSdkToolForCommon.isUrlViaJDSdk(url)){//如果是京东url调用
			JDSdkToolForCommon.startJdSDKWithRawUrl(this, url,from);
			finish();
		}
		if(!TextUtils.isEmpty(adString)){
			initAdBeanFromString(adString);
		}

		findViewById(R.id.top_pannel_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startCallBackIntent();
			}
		});
		titleTv = (TextView)findViewById(R.id.top_panel_title);
		if(!TextUtils.isEmpty(titleExtra)){
			hasSetTitle = true;
			titleTv.setText(titleExtra);
		}

		mWebViewClient = new CustomWebViewClient(this);

		noNetwork = (LinearLayout) findViewById(R.id.advert_sdk_browser_nonetwork);
		mWebView = (WebView)findViewById(R.id.ad_webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);//打开JS
		webSettings.setAllowFileAccess(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//
		webSettings.setSupportZoom(true);//支持拉伸
		webSettings.setBuiltInZoomControls(true);//加入拉伸工具条
		webSettings.setUseWideViewPort(true);//
		webSettings.setSupportMultipleWindows(false);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setGeolocationEnabled(true);
		webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
		webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        mWebView.setWebViewClient(mWebViewClient);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.browser_web_progressbar);
        progressBarContainer = findViewById(R.id.wait_layout);
        mWebView.setWebChromeClient(new WebChromeClient() {
        	@Override
            public void onProgressChanged(WebView view, int newProgress) {
//        		int value = Math.min(100, newProgress + 15);
//        		progressBar.setProgress(value);
//        		if (value == 100) {
//        			progressBarContainer.setVisibility(View.GONE);
//        		}
                if (progressBar != null) {
                    if (newProgress != 100) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        if (advanceImage != null) {
                            if (mWebView.canGoForward()) {
                                advanceImage.setImageResource(R.drawable.webview_right_button);
                            } else {
                                advanceImage.setImageResource(R.drawable.webview_right_button_unclick);
                            }
                        }
                    }
                }

			}
        	 @Override
             public void onReceivedTitle(WebView view, String title) {
        		 if(!TextUtils.isEmpty(title) && !hasSetTitle){
        			 hasSetTitle = true;
        			 titleTv.setText(title);
        		 }
        	 }
        });

		mWebView.setDownloadListener(new android.webkit.DownloadListener() {
			@Override
			public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				if(TelephoneUtil.isWifiEnable(AdvertSDKBrowserActivity.this)){
					startDownloadService(url);
				} else {
					ViewFactory.getAlertDialog(AdvertSDKBrowserActivity.this, getString(R.string.download_delete_title),
							getString(R.string.download_not_wifi_alert), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									startDownloadService(url);
								}
							}, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									startCallBackIntent();
								}
							}).show();
				}
			}

			private void startDownloadService(final String url) {
				String identification = "download_" +System.currentTimeMillis();
				String title = null;
				if(advertItem != null){
					AdvertSDKController.submitStartDownloadEvent(getApplicationContext(), advertItem, identification);
					if(!TextUtils.isEmpty(advertItem.name))
						title = advertItem.name;
				}

				if(TextUtils.isEmpty(title) || "null".equalsIgnoreCase(title)){
					if(titleTv != null && !TextUtils.isEmpty(titleTv.getText())){
						title = titleTv.getText().toString().trim();
					}else {
						title = "来自网页_" +System.currentTimeMillis();
					}
				}
				File file = new File(BaseConfig.Ad_SOURCE_BASE_DIR);
				if(!file.exists()){
					file.mkdirs();
				}
				long currentTimeMillis = System.currentTimeMillis();
				String type =  currentTimeMillis+"";
				final String fileName = title+"_Ad";
				final String fileNameAPK = title+"_Ad.apk";
				if(advertItem != null){
					type =  advertItem.id+"";
				}

				final String identifi = title+"_Ad"+type;
				File file1 = new File(BaseConfig.Ad_SOURCE_BASE_DIR + fileNameAPK);
				if(file1.exists()){
					ApkInstaller.installApplicationNormal(getApplicationContext(),file1);
					finish();
					return ;
				}

				final String finalTitle = title;
				DownloadManager.getInstance(Global.getApplicationContext()).getNormalDownloadTask(identifi, new AbstractDownloadManager.ResultCallback() {
					@Override
					public void getResult(Object o) {
						if(o == null){
							download(identifi,finalTitle,fileNameAPK,fileName);
						}else if (o != null && o instanceof BaseDownloadInfo) {
							BaseDownloadInfo info = (BaseDownloadInfo) o;
							int state = info.getState();
							if (state == DownloadState.STATE_WAITING || state == DownloadState.STATE_START || state == DownloadState.STATE_DOWNLOADING) {
								 MessageUtils.showOnlyToast(Global.getApplicationContext(),getString(R.string.download_ad_action_downing));
								Global.runInMainThread(new Runnable() {
									@Override
									public void run() {
										finish();
									}
								});
							}else if(!info.fileExists()){
								download(identifi,finalTitle,fileNameAPK,fileName);
							}
						}
					}
				});
			}
		});

		// 工具栏
		retreat = (LinearLayout) findViewById(R.id.advert_sdk_browser_retreat);
        advance = (LinearLayout) findViewById(R.id.advert_sdk_browser_advance);
        home = (LinearLayout) findViewById(R.id.advert_sdk_browser_home);
        openBrowser = (LinearLayout) findViewById(R.id.advert_sdk_browser_open_browser);
        retreatImage = (ImageView) findViewById(R.id.advert_sdk_browser_retreat_image);
        advanceImage = (ImageView) findViewById(R.id.advert_sdk_browser_advance_image);
        homeImage = (ImageView) findViewById(R.id.advert_sdk_browser_home_image);
        openBrowserImage = (ImageView) findViewById(R.id.advert_sdk_browser_open_browser_image);
        retreat.setOnClickListener(this);
        advance.setOnClickListener(this);
        home.setOnClickListener(this);
        openBrowser.setOnClickListener(this);

        mWebView.loadUrl(url);
	}

	private void download(String identifi, String finalTitle, String fileNameAPK, final String fileName){
		File file1 = new File(BaseConfig.Ad_SOURCE_BASE_DIR + fileNameAPK +".temp");
		if(file1.exists()){
			MessageUtils.showOnlyToast(Global.getApplicationContext(),getString(R.string.download_ad_action_downing));
			Global.runInMainThread(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			});
			return ;
		}
		final BaseDownloadInfo downloadInfo = new BaseDownloadInfo(identifi,
				BaseDownloadInfo.FILE_TYPE_APK,url, finalTitle, BaseConfig.Ad_SOURCE_BASE_DIR, fileNameAPK, null);
		MessageUtils.showOnlyToast(getApplicationContext(),getString(R.string.download_ad_action_beging)+ finalTitle);
		ThreadUtil.executeMore(new Runnable() {
			@Override
			public void run() {
				DownloadManager.getInstance(Global.getContext()).addNormalTask(downloadInfo, null);
				if(advertItem != null){
					AdvertSdkController.submitStartDownloadEvent(getApplicationContext(),advertItem,fileName);
				}
				Global.runInMainThread(new Runnable() {
					@Override
					public void run() {
						finish();
					}
				});
			}
		});
	}
	@Override
    protected void onResume() {
        // 检测是否有网络
        checkNetwork();
        super.onResume();
    }

	/**
     * 检测网络是否可用
     */
    private void checkNetwork() {
        if (!TelephoneUtil.isNetworkAvailable(this)) {
            noNetwork.removeAllViews();
            ViewFactory.getNomalErrInfoView(this, noNetwork, ViewFactory.NET_BREAK_VIEW);
            noNetwork.setVisibility(View.VISIBLE);
            progressBarContainer.setVisibility(View.GONE);
        } else {
            noNetwork.setVisibility(View.GONE);
        }
    }

	@Override
	public void onBackPressed() {
		if(mWebView != null && mWebView.canGoBack()){
			mWebView.goBack();
		}else{
			startCallBackIntent();
		}
	}

	private void startCallBackIntent() {
		if(callBackIntent != null){
			startActivity(callBackIntent);
			callBackIntent = null;
		}
		finish();
	}

	private void initAdBeanFromString(String adString){
		try {
			advertItem = null;
			AdvertInfo advertInfo = new AdvertInfo();
			JSONObject jo = new JSONObject(adString);
			advertInfo.id = jo.optInt("id");
			advertInfo.showId = jo.optString("showId");
			advertInfo.eventId = jo.optString("eventId");
			advertInfo.pos = jo.optInt("pos");
			advertInfo.name = jo.optString("name");
			advertInfo.height = jo.optInt("height");
			advertInfo.width = jo.optInt("width");
			advertInfo.picUrl = jo.optString("picUrl");
			advertInfo.h5Url = jo.optString("h5Url");
			advertInfo.h5Data = jo.optString("h5Data");
			advertInfo.linkUrl = jo.optString("linkUrl");
			advertInfo.actionIntent = jo.optString("actionIntent");
			advertInfo.splashTime = jo.optLong("splashTime");
			advertInfo.endTime = jo.optString("endTime");
			advertInfo.sourceId = jo.optInt("sourceId");
			advertInfo.desc = jo.optString("desc");
			advertInfo.type = jo.optInt("type");
			advertInfo.showUrl = jo.optString("showUrl");
			advertInfo.clickUrl = jo.optString("clickUrl");
			advertInfo.trackUrl = jo.optString("trackUrl");
			advertInfo.passBack = jo.optString("passBack");
			advertInfo.opt = jo.optString("opt");
			advertInfo.inmobiContextCode = jo.optString("inmobiContextCode");
			advertInfo.inmobiShowAction = jo.optString("inmobiShowAction");
			advertInfo.inmobiClickAction = jo.optString("inmobiClickAction");
			advertItem = advertInfo;
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
        checkNetwork();
        if (!TelephoneUtil.isNetworkAvailable(this)) {
            return;
        }
        if (viewId == retreat.getId()) {// 后退
            retreatImage.performClick();
            onBackPressed();
        } else if (viewId == advance.getId()) {// 前进
            advanceImage.performClick();
            if (mWebView.canGoForward()) {
            	mWebView.goForward();
            }
        } else if (viewId == home.getId()) {// 主页
            homeImage.performClick();
            mWebView.loadUrl(url);
        } else if (viewId == openBrowser.getId()) {// 打开其他浏览器
            openBrowserImage.performClick();
            SystemUtil.openPage(this, mWebView.getOriginalUrl());
        }
	}

	@Override
	protected void onPause() {
		mWebViewClient.onPause();
		super.onPause();
	}
}
