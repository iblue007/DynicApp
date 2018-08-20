package com.felink.corelib.webview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.felink.corelib.R;
import com.felink.corelib.config.BaseConfigPreferences;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.StringUtil;
import com.felink.corelib.kitset.SystemUtil;
import com.felink.corelib.kitset.TelephoneUtil;
import com.felink.corelib.kitset.ThreadUtil;
import com.felink.corelib.kitset.once.Once;
import com.felink.corelib.kitset.once.TagList;
import com.felink.corelib.webview.uconfig.ContentConverter;
import com.felink.corelib.webview.uconfig.UConfig;
import com.felink.corelib.webview.uconfig.UConfigList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 通用逻辑处理的webviewclient</br>
 * Author: cxy
 * Date: 2017/2/17.
 */

public class CustomWebViewClient extends WebViewClient {

    private static final String TAG = "CustomWebViewClient";
    public static final String KEY_WEB_BASED_INTENT_SCHEME_OPT = "key_web_based_intent_schemes_opt";
    public static final String KEY_WEB_BASED_INTENT_SCHEME_PROMPT = "key_web_based_intent_schemes_prompt";


    /**
     * 是否拦截下载
     */
    private boolean needInterruptDownload = false;

    public void setNeedInterruptDownload(boolean needInterruptDownload) {
        this.needInterruptDownload = needInterruptDownload;
    }

    /**
     * 动作分发
     */
    public interface OnActionDispatch {
        void startDownload(String downloadUrl);
    }

    static class WebScheme {
        String packageName;
        String scheme;
        boolean askable = false;//是否需要询问弹窗（跳转第三方协议）
    }

    private OnActionDispatch mActionDispatch;

    private Dialog mDialogForAskOpenThirdApp;
    private Dialog mDialogForAskDownloadApp;

    private String mDownloadUrl;
    private Intent mOpenIntent;
    private Context context;

    private static List<WebScheme> mSchemes = new ArrayList<WebScheme>();
    private static int mSchemeOpt = 0;
    // 当opt = 1时，该值生效，表示是否做弹窗提示，默认不弹窗
    private static boolean mPrompt = false;

    private String handleOnPageStartedUrl = null;

    public CustomWebViewClient(final Context context) {
        this.context = context.getApplicationContext();
        loadSchemeData();
        mDialogForAskOpenThirdApp = ViewFactory.getAlertDialog(context, "即将打开外部应用",
                "外部应用不受91桌面安全保护，可能产生恶意行为，是否继续打开？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SystemUtil.startActivitySafely(context, mOpenIntent);
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        mDialogForAskDownloadApp = ViewFactory.getAlertDialog(context, context.getString(R.string.download_delete_title),
                "是否立即下载该应用？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (mActionDispatch != null) {
                            mActionDispatch.startDownload(mDownloadUrl);
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        initSchemeConfig(context);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 在onPageStarted已经加载过的，不再加载
        if (!TextUtils.isEmpty(handleOnPageStartedUrl) && handleOnPageStartedUrl.equals(url)) {
            handleOnPageStartedUrl = null;
            return true;
        }
        if (filterScheme(view.getContext(), url)) {
            return true;
        }
        return false;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
        /**解决系统webview HTTPS 证书问题时显示界面空白问题 */
        try {
            handler.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        handleOnPageStartedUrl = null;
        if (url != null) {
            if (filterScheme(view.getContext(), url)) {
                handleOnPageStartedUrl = url;
            }
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (errorCode == WebViewClient.ERROR_UNSUPPORTED_SCHEME || errorCode == WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME) {
            if (mSchemeOpt == 1 || mSchemeOpt == 0) {
                String data = "<div>" +
                        "<b>正在跳转到：</b></br>" +
                        "<a href='" + failingUrl + "'>" + failingUrl + "</a></br>" +
                        "</div>";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }
            return;
        }
    }

    private boolean filterScheme(Context ctx, String url) {
        //以.apk结尾的URL都直接跳转到下载管理中下载
        if (url.endsWith(".apk") && needInterruptDownload) {
            mDownloadUrl = url;
            if (!mDialogForAskDownloadApp.isShowing()) {
                mDialogForAskDownloadApp.show();
            }
            return true;
        }

        //所有以http:或https:开头的URL都直接返回false，让系统Webview去处理
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return false;
        } else {//非http:或https:开头的URL且为合法的Intent URI，那么都默认不处理
            try {
                Uri uri = Uri.parse(url);
                if ("alipays".equals(uri.getScheme()) || "weixin".equals(uri.getScheme())) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    SystemUtil.startActivitySafely(ctx, intent);
                    return true;
                } else {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    // avoid based-intent scheme uri attack
                    intent.setComponent(null);
//                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    mOpenIntent = intent;
                    if (intent != null) {
                        //桌面本身的协议，不需要询问（白名单）
                        if ("zm91txwd20141231".equals(uri.getScheme()) || "zm91txwd20141231service".equals(uri.getScheme())) {
                            SystemUtil.startActivitySafely(ctx, intent);
                        } else {
                            boolean valided = false;//协议验证是否通过
                            boolean shouldPrompt = false;//是否弹窗提示
                            if (mSchemeOpt == 0) {//对协议有限制，白名单为Data数组,协议头在白名单中且对应的应用已经安装时提示，否则不提示
                                WebScheme ws = checkScheme(uri);
                                if (ws != null && TelephoneUtil.isApkInstalled(context, ws.packageName)) {
                                    valided = true;
                                    shouldPrompt = ws.askable;
                                } else {
                                    valided = false;
                                }
                            } else if (mSchemeOpt == 1) {//无协议限制，可以过滤所有协议
                                valided = true;
                                shouldPrompt = mPrompt;
                            } else if (mSchemeOpt == 2) {//对所有协议都做限制，也就是屏蔽所有协议
                                valided = false;
                            }
                            Log.e(TAG, "filterScheme: " + valided + " : "+ shouldPrompt);
                            if (valided) {
                                if (shouldPrompt) {
                                    if (!mDialogForAskOpenThirdApp.isShowing()) {
                                        mDialogForAskOpenThirdApp.show();
                                    }
                                } else {
                                    SystemUtil.startActivitySafely(context, mOpenIntent);
                                }
                            }
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setOnActionDispatch(OnActionDispatch actionDispatch) {
        this.mActionDispatch = actionDispatch;
    }

    public void onPause() {
        if (mDialogForAskOpenThirdApp != null) {
            mDialogForAskOpenThirdApp.dismiss();
        }
        if (mDialogForAskDownloadApp != null) {
            mDialogForAskDownloadApp.dismiss();
        }
    }

    /**
     * @desc 返回对应scheme 的包名
     * 返回空表示无任何匹配的scheme
     * @author linliangbin
     * @time 2017/2/24 14:02
     */
    private WebScheme checkScheme(Uri uri) {
        if (uri == null) {
            return null;
        }

        try {
            final String scheme = uri.getScheme();
            Log.d(TAG, scheme);
            if (!TextUtils.isEmpty(scheme) && mSchemes != null && !mSchemes.isEmpty()) {
                for (int i = 0, len = mSchemes.size(); i < len; i++) {
                    WebScheme tempScheme = mSchemes.get(i);
                    String match = tempScheme.scheme;
                    if (scheme.equals(match)) {
                        return tempScheme;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initSchemeConfig(Context context) {
        // 获取OPT
        try {
            SharedPreferences sp = context.getSharedPreferences(BaseConfigPreferences.NAME, Context.MODE_PRIVATE);
            if (sp != null) {
                mSchemeOpt = sp.getInt(KEY_WEB_BASED_INTENT_SCHEME_OPT, 0);
                mPrompt = sp.getInt(KEY_WEB_BASED_INTENT_SCHEME_PROMPT, 0) == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "getSchemes: " + mSchemeOpt);
        Log.d(TAG, "getSchemes: " + mPrompt);

        // 获取待匹配的scheme列表
        if (mSchemes != null && !mSchemes.isEmpty()) {
            return;
        }

        final List<WebScheme> schemes = UConfig.get().getContent(UConfigList.WEB_SUPPORTED_SCHEMES, new ContentConverter<List<WebScheme>>() {
            @Override
            public List<WebScheme> convert(String src) {
                List<WebScheme> list = new ArrayList<WebScheme>();
                try {
                    if (!TextUtils.isEmpty(src)) {
                        JSONArray jsonArray = new JSONArray(src);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0, len = jsonArray.length(); i < len; i++) {
                                JSONObject ele = jsonArray.optJSONObject(i);
                                WebScheme webScheme = new WebScheme();
                                webScheme.packageName = ele.optString("packageName");
                                webScheme.scheme = ele.optString("scheme");
                                webScheme.askable = ele.optInt("prompt", 0) == 1;
                                if (!TextUtils.isEmpty(webScheme.packageName) && !TextUtils.isEmpty(webScheme.scheme)) {
                                    list.add(webScheme);
                                }
                            }
                        }
                        return list;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        mSchemes.clear();
        if (schemes != null) {
            mSchemes.addAll(schemes);
        }
    }

    /**
     * 拉取协议配置，每4小时更新一次
     */
    public static void loadSchemeData() {
        Log.d(TAG, "before " + Once.lastDone(TagList.TAG_WEB_BASED_INTENT_SCHEMES));
        if (!Once.beenDone(4L * 60L * 60L * 1000L, TagList.TAG_WEB_BASED_INTENT_SCHEMES)) {
            ThreadUtil.executeMore(taskRequestSupportedSchemes);
        }
    }

    /**
     * 强制拉取协议配置
     */
    public static void forceFetchSchemeDate() {
        ThreadUtil.executeMore(taskRequestSupportedSchemes);
    }

    /**
     * 获取webview支持过滤的scheme
     */
    private static Runnable taskRequestSupportedSchemes = new Runnable() {

        @Override
        public void run() {
            try {
                Log.d(TAG, "at time ");
                String url = UConfig.get().getUrl(UConfigList.WEB_SUPPORTED_SCHEMES);
                String responseJson = GZipHttpUtil.get(url);

                //请求未成功，直接返回响应码(3xx 内容未改变)
                if (!TextUtils.isEmpty(responseJson) && TextUtils.isDigitsOnly(responseJson)) {
                    if (responseJson.startsWith("3")) {//3xx 配置内容未改变，视为请求成功
                        Once.markDone(TagList.TAG_WEB_BASED_INTENT_SCHEMES);
                        return;
                    } else {//非304的请求出错，都视为网络问题
                        return;
                    }
                }

                if (TextUtils.isEmpty(responseJson)) {
                    Once.markDone(TagList.TAG_WEB_BASED_INTENT_SCHEMES);
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(StringUtil.removeBomHeader(responseJson));
                    //opt
                    int opt = jsonObject.optInt("opt", 0);
                    int askable = jsonObject.optInt("prompt", 0);//默认不询问
                    final SharedPreferences sp = Global.getApplicationContext().getSharedPreferences(BaseConfigPreferences.NAME, 4);
                    if (sp != null) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(KEY_WEB_BASED_INTENT_SCHEME_OPT, opt);
                        editor.putInt(KEY_WEB_BASED_INTENT_SCHEME_PROMPT, askable);
                        editor.commit();
                    }

                    //version
//                        int ver = jsonObject.optInt("version");
                    int ver = 0;

                    //DATA
                    JSONArray jsonArray = jsonObject.optJSONArray("Data");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        UConfig.get().commit(UConfigList.WEB_SUPPORTED_SCHEMES, jsonArray.toString());
                    } else {
                        UConfig.get().commit(UConfigList.WEB_SUPPORTED_SCHEMES, "");
                    }
                    UConfig.get().markDone(UConfigList.WEB_SUPPORTED_SCHEMES, ver);

                    initSchemeConfig(Global.getApplicationContext());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    Once.markDone(TagList.TAG_WEB_BASED_INTENT_SCHEMES);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
