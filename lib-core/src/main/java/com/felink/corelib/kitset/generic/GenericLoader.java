package com.felink.corelib.kitset.generic;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.felink.corelib.net.base.HttpCommon;

import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Response;

/**
 * @Description: 万能配置加载器</br>
 * @author: cxy </br>
 * @date: 2017年07月06日 15:08.</br>
 * @update: </br>
 * @tutorial <ul>
 * <li>初始化：GenericLoader.getInstance().init(context, configuration);</li>
 * </ul>
 * <p>
 * <h5>调用：</h5>
 * <ul>
 * <li>同步请求：{@link #execute(Context, String, boolean)}</li>
 * <li>异步请求：{@link #enqueue(Context, String, boolean, IGenericCallback)}</li>
 * <li>获取本地信息：{@link #getNative(String)}</li>
 * </ul>
 */

public class GenericLoader {

    private static final GenericLoader sInstance = new GenericLoader();

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private GenericConfiguration mConfiguration;
    private ExecutorService mExecuteService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    private IGenericPersist mPersist;
    private boolean hasInitialized = false;

    private GenericLoader() {
    }

    public static GenericLoader getInstance() {
        return sInstance;
    }

    public void init(Context context, GenericConfiguration configuration) {
        this.mConfiguration = configuration;
        mPersist = new GenericPersist(context);
        hasInitialized = true;
    }

    public boolean hasInitialized() {
        return hasInitialized;
    }

    /**
     * 同步
     *
     * @param context
     * @param pName         万能配置参数名称
     * @param verifyVersion 是否校验配置版本
     * @return
     */
    public GenericBean execute(Context context, final String pName, final boolean verifyVersion) {
        final GenericBean old = mPersist.get(pName);
        String url;
        if (verifyVersion) {
            url = mConfiguration.getGenerator().genWithVer(context, pName, old.version, mConfiguration.getPid(), mConfiguration.getPlatform());
        } else {
            url = mConfiguration.getGenerator().genIgnoreVer(context, pName, mConfiguration.getPid(), mConfiguration.getPlatform());
        }

        GenericTask task = new GenericTask(url);
        try {
            final GenericBean bean = task.call();
            if (bean != null) {
                if (bean.resultCode == 0) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPersist.put(pName, bean.version, bean.content);
                        }
                    });
                }
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步
     *
     * @param context
     * @param pName         万能配置参数
     * @param verifyVersion 是否校验配置版本
     * @param callback      回调
     */
    public void enqueue(Context context, final String pName, boolean verifyVersion, final IGenericCallback callback) {
        final GenericBean old = mPersist.get(pName);
        String url;
        if (verifyVersion) {
            url = mConfiguration.getGenerator().genWithVer(context, pName, old.version, mConfiguration.getPid(), mConfiguration.getPlatform());
        } else {
            url = mConfiguration.getGenerator().genIgnoreVer(context, pName, mConfiguration.getPid(), mConfiguration.getPlatform());
        }

        GenericTask task = new GenericTask(url);
        try {
            Future<GenericBean> result = mExecuteService.submit(task);
            if (result != null) {

                final GenericBean bean = result.get();
                if (bean != null) {
                    if (bean.resultCode == 0) {
                        if (callback != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mPersist.put(pName, bean.version, bean.content);
                                    callback.onCompleted(pName, old.version, bean.version, bean.content);
                                }
                            });
                        }
                        return;
                    } else {
                        if (callback != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailed(bean.resultCode, pName, old.version);
                                }
                            });
                        }
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //异常情况
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailed(-100, pName, old.version);
                }
            });
        }
    }

    public GenericBean getNative(String pName) {
        return mPersist.get(pName);
    }

    public static class GenericTask implements Callable<GenericBean> {

        private String url;

        public GenericTask(String url) {
            this.url = url;
        }

        @Override
        public GenericBean call() throws Exception {

            HttpCommon httpCommon = new HttpCommon(url);
            Response response = httpCommon.getResponseGet(null);

            GenericBean bean = new GenericBean();
            if (response != null) {
                bean.resultCode = response.code();
                if (response.isSuccessful()) {
                    if (bean.resultCode == 200) {
                        bean.resultCode = 0;
                        String ver = response.header("Ver");
                        bean.content = response.body().string();
                        if (!TextUtils.isEmpty(ver)) {
                            bean.version = Integer.valueOf(ver);
                        } else {
                            JSONObject jsonObject = new JSONObject(bean.content);
                            bean.version = jsonObject.optInt("version", 0);
                        }
                    }
                }
            } else {
                //未知错误
                bean.resultCode = -100;
            }

            return bean;
        }
    }

    public static class GenericBean {
        public String content;
        public int version = -1;
        public int resultCode = 0;
    }
}
