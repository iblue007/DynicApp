package com.felink.corelib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.felink.corelib.R;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.MessageUtils;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.AbstractDownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadPresenter;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.IDownloadStateView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 下载状态按钮</br>
 * @author: cxy </br>
 * @date: 2017年05月05日 17:49.</br>
 * @update: </br>
 */

public class DownloadStateButton extends FrameLayout implements IDownloadStateView, View.OnClickListener {

    //下载中
    public static final int STATE_DOWNLOADING = 0;
    //下载暂停
    public static final int STATE_PAUSE = 1;
    //下载取消
    public static final int STATE_CANCEL = 2;
    //下载完成
    public static final int STATE_FINISHED = 3;
    //等待下载
    public static final int STATE_WAITING = 4;
    //none
    public static final int STATE_NONE = 6;
    //下载失败
    public static final int STATE_FAILED = 7;
    //下载开始
    public static final int STATE_START = 8;

    //初始状态
    public static final int STATE_PENDING = -1;
    //初始化失败
    public static final int STATE_INIT_FAILED = -2;
    //准备完成
    public static final int STATE_PREPARED = -3;
    //解析中
    public static final int STATE_TRANSLATING = -4;
    //解析失败
    public static final int STATE_TRANS_FAILED = -5;
    //结束
    public static final int STATE_FINAL = -6;

    private int mCurrentState = STATE_PENDING;

    private TextView dlHandle;
    private ProgressBar dlProgress;

    private String mTargetUri;
    private String mIdentifier;

    private boolean isPrepared = false;
    private DownloadPresenter mPresenter;
    private OnDownloadStateChangedListener mOnDownloadStateChangedListener;
    private OnDownloadStateClickListener mOnDownloadStateClickListener;

    private BaseDownloadInfo mDownloadInfo;
    private ArrayList<String> mDownloadIdentifierSet = new ArrayList<String>();

    protected SparseArray<CharSequence> mStateMapping = new SparseArray<CharSequence>();

    public DownloadStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPresenter = new DownloadPresenter(this);
        initData();
        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenter.unregister(Global.getApplicationContext());
    }

    private void registerReceiver() {
        mPresenter.register(Global.getApplicationContext(), new DownloadPresenter.IdentifyFilter() {
            @Override
            public boolean filter(String identify) {
                if (mDownloadIdentifierSet.contains(identify)) {
                    return true;
                }
                return false;
            }
        });
    }

    protected void initData() {
        mStateMapping.put(STATE_FINISHED, "100%");
        mStateMapping.put(STATE_FINAL, "设为壁纸");
        mStateMapping.put(STATE_WAITING, "等待下载...");
        mStateMapping.put(STATE_START, "0%");
        mStateMapping.put(STATE_PAUSE, "继续");
        mStateMapping.put(STATE_FAILED, "重新下载");
        mStateMapping.put(STATE_PENDING, "初始化...");
        mStateMapping.put(STATE_PREPARED, "下载高清");
        mStateMapping.put(STATE_TRANSLATING, "解析中...");
        mStateMapping.put(STATE_TRANS_FAILED, "重试");
        mStateMapping.put(STATE_INIT_FAILED, "重试");
    }

    private void initView() {
        inflate(getContext(), R.layout.view_download_state, this);
        dlProgress = (ProgressBar) findViewById(R.id.dl_progress);
        dlHandle = (TextView) findViewById(R.id.dl_handle);
        super.setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        //super.setOnClickListener(l);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        //super.setOnLongClickListener(l);
    }

    public void prepare(BaseDownloadInfo task) {
        prepare(task, null);
    }

    public void prepare(BaseDownloadInfo info, List<BaseDownloadInfo> sub) {
        isPrepared = false;
        update(STATE_PENDING);
        if (info == null) {
            return;
        }
        String mainId = info.getIdentification();
        String mainUrl = info.getDownloadUrl();
        if (TextUtils.isEmpty(mainId) || TextUtils.isEmpty(mainUrl)) {
            update(STATE_INIT_FAILED);
            MessageUtils.showOnlyToast(getContext(), "初始化数据失败！");
            return;
        }

        //清空
        ArrayList<BaseDownloadInfo> queue = new ArrayList<BaseDownloadInfo>();
        mDownloadIdentifierSet.clear();

        mDownloadInfo = info;
        mIdentifier = mainId;
        mTargetUri = mainUrl;
        mDownloadIdentifierSet.add(mIdentifier);

        if (sub != null && !sub.isEmpty()) {
            for (int i = 0, len = sub.size(); i < len; i++) {
                BaseDownloadInfo task = sub.get(i);
                String identifier = task.getIdentification();
                String url = task.getDownloadUrl();

                if (TextUtils.isEmpty(identifier) || TextUtils.isEmpty(url)) {
                    continue;
                }
                mDownloadIdentifierSet.add(identifier);
                queue.add(task);
            }
        }

        if (!queue.isEmpty()) {
            info.setSubBaseDownloadInfoS(queue);
        }

        registerReceiver();
        DownloadManager.getInstance(Global.getApplicationContext()).getNormalDownloadTask(mIdentifier, new AbstractDownloadManager.ResultCallback() {
            @Override
            public void getResult(Object o) {
                if (o instanceof BaseDownloadInfo) {
                    BaseDownloadInfo info = (BaseDownloadInfo) o;
                    int state = info.getState();
                    if (state != STATE_TRANS_FAILED || state != STATE_TRANSLATING || state != STATE_FINAL) {
                        if (state == STATE_FINISHED) {
                            update(STATE_TRANSLATING);
                        } else {
                            update(state);
                        }
                    }
                }
            }
        });
        isPrepared = true;
        update(STATE_PREPARED);
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public boolean isDownloading() {
        return mCurrentState != STATE_PENDING
                && mCurrentState != STATE_NONE
                && mCurrentState != STATE_PREPARED
                && mCurrentState != STATE_FINAL
                && mCurrentState != STATE_FINISHED
                && mCurrentState != STATE_FAILED
                && mCurrentState != STATE_TRANSLATING
                && mCurrentState != STATE_TRANS_FAILED
                && mCurrentState != STATE_INIT_FAILED
                && mCurrentState != STATE_FINISHED;
    }

    public void pause() {
        if (!isPrepared) {
            return;
        }
        DownloadManager.getInstance(Global.getApplicationContext()).pauseNormalTask(mIdentifier, null);
    }

    @Override
    public void onClick(View v) {
        if (!isPrepared && mCurrentState != STATE_INIT_FAILED) {
            MessageUtils.showOnlyToast(getContext(), "初始化数据失败！");
            return;
        }
        boolean consume = false;
        if (mOnDownloadStateClickListener != null) {
            consume = mOnDownloadStateClickListener.onClick(mCurrentState);
        }
        if (!consume) {
            performState();
        }
    }

    private void performState() {
        if (mCurrentState == STATE_FAILED || mCurrentState == STATE_NONE || mCurrentState == STATE_PREPARED) {//失败、未开始、准备完成->重新下载
            mPresenter.addTask(Global.getApplicationContext(), mDownloadInfo);
        } else if (mCurrentState == STATE_DOWNLOADING) {//正在下载->暂停
            DownloadManager.getInstance(Global.getApplicationContext()).pauseNormalTask(mIdentifier, null);
        } else if (mCurrentState == STATE_PAUSE) {//暂停->继续
            DownloadManager.getInstance(Global.getApplicationContext()).continueNormalTask(mIdentifier, null);
        } else if (mCurrentState == STATE_TRANSLATING) {//下载完成->解析安装
            update(mCurrentState);
        }
    }

    private void updateState(int state, CharSequence desc) {
        if (mCurrentState == state && mCurrentState != STATE_DOWNLOADING && mCurrentState != STATE_TRANSLATING) {
            return;
        }

        mCurrentState = state;
        if (mOnDownloadStateChangedListener != null) {
            mOnDownloadStateChangedListener.onChanged(mIdentifier, mTargetUri, mCurrentState);
        }
        if (desc != null) {
            dlHandle.setText(desc);
        }
    }

    public void update(int state, CharSequence desc) {
        if (state != STATE_DOWNLOADING) {
            mStateMapping.put(state, desc);
        }
        updateState(state, desc);
    }

    public void update(int state) {
        updateState(state, mStateMapping.get(state));
    }

    @Override
    public void onDLWaitting(String id, String downloadUrl) {
        update(STATE_WAITING);
    }

    @Override
    public void onDLStart(String id, String downloadUrl) {
        update(STATE_START);
    }

    @Override
    public void onDLCancel(String id, String downloadUrl) {
        update(STATE_CANCEL);
    }

    @Override
    public void onDLPause(String id, String downloadUrl) {
        update(STATE_PAUSE);
    }

    @Override
    public void onDLFailed(String id, String downloadUrl) {
        update(STATE_FAILED);
    }

    @Override
    public void onDLFinished(String id, String downloadUrl) {
        dlProgress.setProgress(100);
        update(STATE_FINISHED);
        update(STATE_TRANSLATING);
    }

    @Override
    public void onDLDownloading(int progress, String id, String downloadUrl) {
        update(STATE_DOWNLOADING, progress + "%");
        dlProgress.setProgress(progress);
    }

    public void setOnDownloadStateChangedListener(OnDownloadStateChangedListener listener) {
        mOnDownloadStateChangedListener = listener;
    }

    public void setOnDownloadStateClickListener(OnDownloadStateClickListener listener) {
        mOnDownloadStateClickListener = listener;
    }

    public interface OnDownloadStateClickListener {
        boolean onClick(int state);
    }

    public interface OnDownloadStateChangedListener {
        boolean onChanged(String identifier, String url, int state);
    }
}
