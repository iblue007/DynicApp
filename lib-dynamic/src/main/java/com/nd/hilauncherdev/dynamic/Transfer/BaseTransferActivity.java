package com.nd.hilauncherdev.dynamic.Transfer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.felink.corelib.config.BaseConfig;
import com.nd.hilauncherdev.datamodel.DynamicConstant;
import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.dynamic.Transfer.ITransfer.PluginState;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.framework.ViewFactory;
import com.nd.hilauncherdev.framework.view.MyphoneContainer;
import com.nd.hilauncherdev.framework.view.PercentBarView;
import com.nd.hilauncherdev.kitset.util.DynamicPluginUtil;
import com.nd.hilauncherdev.kitset.util.FileUtil;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;
import com.nd.hilauncherdev.kitset.util.ThreadUtil;
import com.nd.hilauncherdev.myphone.battery.util.SystemSettingUtil;
import com.nd.hilauncherdev.view.AviodWindowLeakedSplashWindow;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadManager;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadBroadcastExtra;
import com.nd.hilauncherdev.webconnect.downloadmanage.util.DownloadState;
import com.nd.hilauncherdev.widget.shop.util.WidgetType;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import static com.nd.hilauncherdev.widget.shop.util.WidgetType.DATA_DATA_TYPE;

public abstract class BaseTransferActivity extends Activity {
    public static final String KEY_OPEN_API = "open_api";
    //	public static final long PLUGIN_UPDATE_TIP_INTRAVEL = 7 * 24 * 60 * 60 * 1000L;
    //	public static final long PLUGIN_UPDATE_TIP_INTRAVEL = 2 * 1000L;
    public static final boolean MODE_TEST = false;
    private static final int HANDLER_REFRESH_DESC = 0;
    private static final int HANDLER_REFRESH_VIEW = 1;
    private static final int HANDLER_REFRESH_UPDATE_LAUNCHER = 2;

    private static final int MODE_DOWNLOADING = 0;
    private static final int MODE_NOT_START = 1;
    private static final int MODE_PAUSE = 2;
    private int mCurrentMode = 0;

    private MyphoneContainer container;
    private LinearLayout neterror_layout;
    private LinearLayout plugin_content;
    private View plugin_down_view;
    private View plugin_update_tip;
    private TextView pet_update_down;
    private TextView pet_update_skip;
    private View refreshView;
    private Button updateLauncherBtn;
    //	private Button button;
    //	private ImageView ivBanner;
    //	private TextView process;//50%
    private TextView processSize;//500k/1000k
    private View start_pause_ly;
    private ImageView start_pause_img;
    private TextView start_pause_txt;
    private View clear_ly;
    private ImageView clear_img;
    private TextView clear_txt;
    private PluginDownloadAnim animLayout;
    private TextView progressPercentText;
    private EditText desc;
    private PercentBarView progressBar;
    protected ITransfer mTransfer;
    //private DownloadListener mDownloadListener = new DownloadListener();
    private Context mCtx;
    /**
     * 使用过的包名，用于兼容旧包名
     */
    private String[] mUsedNames;
    private String mPkgName, mFileName, newPkgName;
    private ITransfer.PluginState mPluginState;
    private boolean isDownSuc = false;
    private boolean isDescShow = false;
    //	private boolean isAnimSuc = false;
    private boolean isConnected = false;
    private boolean isWifi = false;
    private boolean isForceUpdate = false;
    private boolean isShowForceUpdateDialog = false;
    private Handler handler = new Handler();
    /**
     * 插件标题
     */
    private String mTitle = "";
    /**
     * 下载管理中显示的名称
     */
    private String mModule = "";
    /**
     * 插件介绍
     */
    private String[] mDesc = null;
    private int mDescPos = 0;
    private boolean sdcardExist = true;
    //	/**
    //	 * 加载过程显示的文字介绍
    //	 */
    //	private String[] datas;
    //	/**
    //	 * 是否需要打印机效果
    //	 */
    //	private boolean isAnimNeed = false;
    private Intent mIntent;
    //	private DownloadServerServiceConnection mDownloadServiceConnection;
    private boolean mDeedAutoDown = true;
    private String totalSize = "";
    private String downloadSize = "";
    private int progress = 0;
    private DownloadListener mDownloadListener = new DownloadListener();
    private PluginUpgradeInfo updateInfo;
    /**
     * 自当前进程启动以来，加载过的插件<包名，插件本地路径>
     */
    private static Map<String, String> needRestart = new HashMap<String, String>();

    private int mTryCount = 0;
    private int lowestPluginRunningVersionCode = Integer.MIN_VALUE;//最低插件运行版本 

    private void testMode() {
        mPluginState = ITransfer.PluginState.NEED_UPGRADE;
        isConnected = true;
        isWifi = false;
    }

    private BroadcastReceiver downloadProgressReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                int state = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_STATE, DownloadState.STATE_NONE);
                totalSize = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_TOTAL_SIZE);
                if (null == totalSize) {
                    totalSize = "0.0";
                }
                downloadSize = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_DOWNLOAD_SIZE);
                if (state == DownloadState.STATE_FAILED) {
                    //show net error
                    h.sendEmptyMessage(HANDLER_REFRESH_VIEW);
                } else {
                    String id = intent.getStringExtra(DownloadBroadcastExtra.EXTRA_IDENTIFICATION);
                    if (null != mPkgName && null != id && id.equals(mPkgName)) {
                        progress = intent.getIntExtra(DownloadBroadcastExtra.EXTRA_PROGRESS, 0);
                        if (mCurrentMode == MODE_DOWNLOADING) {
                           // mDownloadListener.onRefreshState(context, progress);
                        }
                    }
                }
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        if (!"".equals(mPkgName)) {
            DownloadManager.getInstance(this).controlSilent23GTask(mPkgName, false);
        }
        h.removeMessages(HANDLER_REFRESH_DESC);
        h.removeMessages(HANDLER_REFRESH_VIEW);
        h.removeMessages(HANDLER_REFRESH_UPDATE_LAUNCHER);
        try {
            unregisterReceiver(downloadProgressReceiver);
        } catch (Exception e) {
            // ^_^
        }
        if (animLayout != null) {
            animLayout.stopAnim();
        }
//		if (null != mDownloadServiceConnection) {
//			mDownloadServiceConnection.unBindDownloadService();
//		}
    }

    ;

    /**
     * 插件存在服务端，是否需要进入页面自动下载
     *
     * @Function: com.nd.hilauncherdev.myphone.common.BaseTransferActivity.isAutoDown
     * @Description:
     * @version:v1.0
     * @author:linyt
     * @date:2014年9月2日 下午3:06:46
     */
//	protected void isAutoDown(boolean flag) {
//		mDeedAutoDown = flag;
//	}
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this;
        sdcardExist = TelephoneUtil.isSdcardExist();
        if (!sdcardExist) {
            Toast.makeText(this, R.string.hint_sdcard_unavailable_msg, Toast.LENGTH_LONG).show();
            finish();
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            container = new MyphoneContainer(this);

            beforeComponentInit();
            setContentView(container);

            registerReceiver(downloadProgressReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_STATE));
            View layout = View.inflate(this, R.layout.plugin_loader_guide_activity, null);
            String title = "";
            container.initContainer(title, layout, MyphoneContainer.DEFALUT_THEME);
            container.setBottomBgVisibility(View.GONE);
            container.setGoBackListener(new OnClickListener() {
                public void onClick(View arg0) {
                    finish();
                }
            });
            plugin_content = (LinearLayout) findViewById(R.id.plugin_content);
            neterror_layout = (LinearLayout) findViewById(R.id.neterror_layout);
            plugin_down_view = findViewById(R.id.plugin_down_view);
            plugin_update_tip = findViewById(R.id.plugin_update_tip);
            animLayout = (PluginDownloadAnim) findViewById(R.id.plugin_download_anim);
            pet_update_down = (TextView) findViewById(R.id.pet_update_down);
            pet_update_skip = (TextView) findViewById(R.id.pet_update_skip);
            pet_update_down.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    desc.setVisibility(View.VISIBLE);
                    plugin_down_view.setVisibility(View.VISIBLE);
                    plugin_update_tip.setVisibility(View.GONE);
                    downloadPlugin();
                }
            });
            pet_update_skip.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isForceUpdate) {
                        //需要强制升级，弹出对话框提示
                        //升级--继续  不升级--finish
                        ViewFactory.getAlertDialog(mCtx, -1, mCtx.getString(R.string.download_error_apk_title), mCtx.getString(R.string.download_notify_apk_upgrade_content), mCtx.getString(R.string.plugin_update_right_now), mCtx.getString(R.string.download_notify_apk_upgrade_stop), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                desc.setVisibility(View.VISIBLE);
                                plugin_down_view.setVisibility(View.VISIBLE);
                                plugin_update_tip.setVisibility(View.GONE);
                                downloadPlugin();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                forward();
                            }
                        }).show();
                    } else {
                        forward();
                    }
                }
            });
            neterror_layout = (LinearLayout) findViewById(R.id.neterror_layout);
            refreshView = findViewById(R.id.framework_viewfactory_refresh_btn);
            refreshView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isConnected = SystemSettingUtil.isNetworkAvailable(mCtx);
                    if (isConnected) {
                        neterror_layout.setVisibility(View.GONE);
                        plugin_content.setVisibility(View.VISIBLE);
                        doDefaultTransferInit(mTransfer, mPkgName, mFileName, mUsedNames);
                        dealWithPluginState();
                    }
                }
            });

            desc = (EditText) findViewById(R.id.desc);
            updateLauncherBtn = (Button) findViewById(R.id.button_update_launcher);
            updateLauncherBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AviodWindowLeakedSplashWindow(mCtx, mCtx.getString(R.string.soft_update_setting_title), mCtx.getString(R.string.soft_update_checking), new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onUpgradeApp();
                        }
                    }, null, true);
                }
            });

            //			process = (TextView) findViewById(R.id.process);
            processSize = (TextView) findViewById(R.id.processSize);
            start_pause_ly = findViewById(R.id.start_pause_ly);
            start_pause_img = (ImageView) findViewById(R.id.start_pause_img);
            start_pause_txt = (TextView) findViewById(R.id.start_pause_txt);

            start_pause_ly.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrentMode == MODE_DOWNLOADING) {
                        changeMode(MODE_PAUSE);
                        DownloadManager.getInstance(BaseTransferActivity.this).pauseSilentTask(mPkgName, true);
                    } else if (mCurrentMode == MODE_PAUSE) {
                        changeMode(MODE_DOWNLOADING);
                        DownloadManager.getInstance(BaseTransferActivity.this).continuteSilentTask(mPkgName, true);
                    } else if (mCurrentMode == MODE_NOT_START) {
                        changeMode(MODE_DOWNLOADING);
                        startDownload();
                    }
                }
            });
            clear_ly = findViewById(R.id.clear_ly);
            clear_img = (ImageView) findViewById(R.id.clear_img);
            clear_txt = (TextView) findViewById(R.id.clear_txt);
            //Log.e("asd", "1=========== " + PluginContext.isPluginAppInfoExist(CommonGlobal.PLUGIN_DIR + mFileName));
            clear_ly.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isForceUpdate) {
                        //需要强制升级，弹出对话框提示
                        //升级--继续  不升级--finish
                        ViewFactory.getAlertDialog(mCtx, -1, mCtx.getString(R.string.download_error_apk_title), mCtx.getString(R.string.download_notify_apk_upgrade_content), mCtx.getString(R.string.plugin_update_right_now), mCtx.getString(R.string.download_notify_apk_upgrade_stop), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
                    } else {
                        changeMode(MODE_NOT_START);
                        DownloadManager.getInstance(BaseTransferActivity.this).cancelSilentTask(mPkgName, true);
                        if (mPluginState == ITransfer.PluginState.NEED_UPGRADE && isConnected && isWifi) {
                            forward();
                        }
                    }
                }
            });

            progressBar = (PercentBarView) findViewById(R.id.progressBar);
            progressBar.setProgressiveFactor(20);
            progressBar.setForeground(R.drawable.plugin_loader_progress_fg);
            progressBar.setBackground(R.drawable.plugin_loader_progress_bg);
            progressPercentText = (TextView) findViewById(R.id.progressBar_text);
            afterComponentInit();
            //doStartUp();
        }
    }

    /**
     * <p>
     * 所有通用初始化步骤完成后, 将调用此方法.
     * <p/>
     * <p>
     * <b> 注意</b>: 子类对view 的操作(如button.setText("abc")等 )请务必在此方法中实现!
     * 不要在onCreate方法中操作. <br/>
     * 否则在没有sd卡的情况下view的引用为空将抛出空指针异常
     * </p>
     *
     * @author Yu.F, 2013-12-10
     */
    public abstract void afterComponentInit();

    /**
     * 此方法建议子类用于设置PluginRunningTransfer实体
     *
     * @return
     * @Function: com.nd.hilauncherdev.myphone.common.BaseTransferActivity.beforeComponentInit
     * @Description:
     * @version:v1.0
     * @author:linyt
     * @date:2014年8月7日 下午4:21:43
     */
    public void beforeComponentInit() {

    }

    public static String getPluginFileName(String mPkgName) {
        //不是本次进程启动后第一次启动该插件，则取最近一次加载使用的插件文件名
        if (needRestart != null && needRestart.containsKey(mPkgName)) {
            String cacheFileName = needRestart.get(mPkgName);
            if (!TextUtils.isEmpty(cacheFileName)) {
                return cacheFileName;
            }
        }

        //本次进程启动后第一次启动该插件，则获取当前目录下版本号最大的插件
        if (needRestart != null && !needRestart.containsKey(mPkgName)) {
            String newestFile = PluginLoaderUtil.getCurrentNewestPluginFilename(mPkgName);
            if (!TextUtils.isEmpty(newestFile)) {
                return newestFile;
            }
        }
        return "";
    }

    /**
     * 说明: 提供默认的, 统一的跳转行为 给子类调用, 若子类需要定义自己的跳转行为, 可以参照此流程定义行为
     *
     * @param pkgName  动态插件包名
     * @param fileName 动态插件文件名,包名+后缀.
     * @Title: doDefaultTransferInit
     * @author lytjackson@gmail.com
     * @date 2014-3-31
     */
    protected final void doDefaultTransferInit(final String pkgName, final String fileName) {
        doDefaultTransferInit(pkgName, fileName, null);
    }

    protected final void doDefaultTransferInit(final String pkgName, final String fileName, String[] usedNames) {
        doDefaultTransferInit(null, pkgName, fileName, usedNames);
    }

    /**
     * 说明: 提供默认的, 统一的跳转行为 给子类调用, 若子类需要定义自己的跳转行为, 可以参照此流程定义行为
     *
     * @param pluginRunningTransfer 动态插件行为处理接口,传null则使用默认行为--存放于assets下的内置
     * @param pkgName               动态插件包名
     * @param fileName              动态插件文件名,包名+后缀.
     * @param usedNames             动态插件文件包名，用于更换包名的插件使用
     * @Title: doDefaultTransferInit
     * @author lytjackson@gmail.com
     * @date 2014-3-31
     */
    protected final void doDefaultTransferInit(ITransfer pluginRunningTransfer, final String pkgName, final String fileName, String[] usedNames) {
        isConnected = SystemSettingUtil.isNetworkAvailable(this);
        isWifi = SystemSettingUtil.isWifi(mCtx);
        this.mPkgName = pkgName;
        this.mUsedNames = usedNames;
        this.mFileName = fileName;
        if (null == mTransfer) {
            if (null == pluginRunningTransfer) {
                mTransfer = new PluginRunningTransfer(this);
            } else {
                mTransfer = pluginRunningTransfer;
            }
        }
        updateInfo = PluginUpgrader.getInstance().getPluginUpgradeInfo(pkgName);
        if (updateInfo != null && updateInfo.getNeedRestart() == PluginUpgradeInfo.NEED_RESTART) {
            DynamicPluginUtil.addToNeedRestart(mPkgName);
        } else {
            DynamicPluginUtil.removeFromNeedRestart(mPkgName);
        }


        String getFileName = getPluginFileName(mPkgName);
        if (!TextUtils.isEmpty(getFileName)) {
            mFileName = getFileName;
        }

        mPluginState = mTransfer.getPluginState(pkgName, this.mFileName, mUsedNames, updateInfo, needRestart.containsKey(pkgName));
        if (Integer.MIN_VALUE != lowestPluginRunningVersionCode) {//强制升级的特殊处理
            int localPluginVersionCode = PluginTransferUtil.getInstallPluginVersion(this, DynamicConstant.PLUGIN_DIR, this.mFileName);
            if (localPluginVersionCode != 0 && lowestPluginRunningVersionCode > localPluginVersionCode) {
                isForceUpdate = true;
                mPluginState = PluginTransferUtil.getStateForUpdatePluginByWifiDownload(mCtx, pkgName, this.mFileName,
                        needRestart.containsKey(pkgName), lowestPluginRunningVersionCode);
            }
        }
        if (MODE_TEST) {
            testMode();
        }
        mTransfer.setPluginTransferListener(getDefaultPluginTransferListener());
        mIntent = getIntent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);

        switch (mPluginState) {
            case NORMAL:
                desc.setText(R.string.plugin_transfer_desc1);
                return;
            case NEED_DOWNLOAD:
                desc.setText(R.string.plugin_transfer_desc1);
                break;
            case NEED_UPGRADE:
            case OUT_OF_SYNC_FOR_OUTTER:
            case OUT_OF_SYNC_FOR_INNER:
                if (isForceUpdate) {
                    desc.setText(R.string.plugin_must_update_desc);
                } else {
                    desc.setText(R.string.plugin_update_desc);
                }
                break;
            case NEED_UPGRADE_LAUNCHER:
                desc.setText(R.string.plugin_transfer_desc4);
                break;
            case ERROR:
                Toast.makeText(BaseTransferActivity.this, getString(R.string.plugin_transfer_error), Toast.LENGTH_LONG).show();
                return;
            default:
                break;
        }
        if (null != usedNames && usedNames.length > 0) {
            for (int i = 0; i < usedNames.length; i++) {
                DynamicPluginUtil.clearPluginDir(mCtx, usedNames[i]);
            }
        }
    }


    /**
     * 生成比当前版本序号+1 的插件文件名
     *
     * @param pluginFileName 当前插件文件名
     * @return
     */
    public static String generatePluginNameIncreaseVer(String pluginFileName) {
        int currentVer = PluginLoaderUtil.getPluginVersionFromName(pluginFileName);
        if (currentVer == 0) {
            int lastIndex = pluginFileName.lastIndexOf(".");
            return pluginFileName.substring(0, lastIndex) + ".1" + pluginFileName.substring(lastIndex);
        } else {
            int lastIndex = pluginFileName.lastIndexOf(".");
            String fileNameWithoutType = pluginFileName.substring(0, lastIndex);
            int secondLastIndex = fileNameWithoutType.lastIndexOf(".");
            currentVer++;
            // 测试使用
            return pluginFileName.substring(0, secondLastIndex) + "." + 1 + pluginFileName.substring(lastIndex);
//            return pluginFileName.substring(0, secondLastIndex) + "." + currentVer + pluginFileName.substring(lastIndex);
        }
    }


    /**
     * 删除失效的插件包 和 DEX文件
     *
     * @param pkg
     * @param usingPluginFile
     */
    public static void deleteUnusePluginFile(final String pkg, final String usingPluginFile, final Context mCtx) {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                try {
                    //删除插件jar 包
                    File pluginDir = new File(DynamicConstant.PLUGIN_DIR);
                    if (pluginDir.exists()) {
                        File pluginFiles[] = pluginDir.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File file, String s) {
                                if (!TextUtils.isEmpty(s) && s.contains(pkg)) {
                                    return true;
                                }
                                return false;
                            }
                        });
                        for (int i = 0; i < pluginFiles.length; i++) {
                            File currentPluginFile = pluginFiles[i];
                            if (currentPluginFile != null) {
                                String currentPluginFileName = currentPluginFile.getName();
                                if (!TextUtils.isEmpty(currentPluginFileName) &&
                                        !currentPluginFileName.equals(usingPluginFile)) {
                                    //删除jar 包
                                    currentPluginFile.delete();
                                }
                            }
                        }
                    }


                    //删除dex文件夹
                    File dexDir = new File(DynamicPluginUtil.getRootApkFile(mCtx));
                    if (dexDir.exists()) {
                        File dexFolders[] = dexDir.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File file, String s) {
                                if (!TextUtils.isEmpty(s) && s.contains(pkg)) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                        for (int i = 0; i < dexFolders.length; i++) {
                            File currentDex = dexFolders[i];
                            if (!currentDex.getName().equals(PluginLoaderUtil.getPureNameWithVer(usingPluginFile))) {
                                FileUtil.delFolder(DynamicPluginUtil.getRootApkFile(mCtx) + "/" + currentDex.getName());
                            }
                        }
                    }
                    
                    //安卓8.0以上多做一步操作
                    if (Build.VERSION.SDK_INT >= 26) {
                        String cpu = Build.CPU_ABI.toLowerCase();
                        if (TextUtils.isEmpty(cpu)) {
                            cpu = "armeabi";
                        }
                        if (cpu.startsWith("arm")) {
                            //arm分为好几个但是统一都会生成在arm目录下，所以要删除
                            cpu = "arm";
                            deleteUnusePluginFile26(pkg, usingPluginFile, mCtx, cpu);
                        }
                        //按cpu全称再删除一次
                        deleteUnusePluginFile26(pkg, usingPluginFile, mCtx, cpu);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 删除失效的插件包 和 DEX文件
     * @param pkg
     * @param usingPluginFile
     */
    public static void deleteUnusePluginFile26(final String pkg, final String usingPluginFile, final Context mCtx, String cpu) {
        if (Build.VERSION.SDK_INT < 26) return;
        //TODO:xuqunxing
        File dexDir = new File(DATA_DATA_TYPE.getBaseDir() + "/oat/" + cpu);
        if (dexDir.exists()) {
            File dexFolders[] = dexDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    if (!TextUtils.isEmpty(s) && s.contains(pkg)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            for (int i = 0; i < dexFolders.length; i++) {
                File currentDex = dexFolders[i];
                String PureNameWithVer=PluginLoaderUtil.getPureNameWithVer(usingPluginFile);
                String name=PluginLoaderUtil.getPureNameWithVer(currentDex.getName());
                if (!name.equals(PureNameWithVer)) {
                    Log.e("=====","======bbb:"+currentDex.getPath());
                    FileUtil.delFile(currentDex.getPath());
                }
            }
        }
    }

    private void changeMode(int mode) {
        if (MODE_DOWNLOADING == mode) {
            //正在下载
            start_pause_ly.setVisibility(View.VISIBLE);
            start_pause_img.setImageResource(R.drawable.plugin_qiyong_btn_pause);
            start_pause_txt.setText(R.string.plugin_down_stop);
            clear_ly.setEnabled(true);
            clear_ly.setVisibility(View.VISIBLE);
            animLayout.startAnim();
        } else if (MODE_NOT_START == mode) {
            //清除后还未点击开始
            start_pause_ly.setVisibility(View.VISIBLE);
            start_pause_img.setImageResource(R.drawable.plugin_qiyong_btn_start);
            start_pause_txt.setText(R.string.plugin_down_start);
            clear_ly.setEnabled(false);
            progress = 0;
            //			process.setText("0%");
            processSize.setText("0.0B/0");
            progressBar.setProgress(progress);
            progressBar.setVisibility(View.INVISIBLE);
        } else if (MODE_PAUSE == mode) {
            //暂停
            start_pause_ly.setVisibility(View.VISIBLE);
            start_pause_img.setImageResource(R.drawable.plugin_qiyong_btn_down);
            start_pause_txt.setText(R.string.plugin_update_right_now);
            clear_ly.setEnabled(true);
            clear_ly.setVisibility(View.VISIBLE);
        }
        mCurrentMode = mode;
    }

    @Override
    protected void onResume() {
        super.onResume();
        dealWithPluginState();
    }

    private void dealWithPluginState() {
        if (mPluginState == PluginState.NEED_UPGRADE_LAUNCHER) {
            h.sendEmptyMessage(HANDLER_REFRESH_UPDATE_LAUNCHER);
        } else if (mPluginState == PluginState.NEED_DOWNLOAD) {
            // 新用户
//            if (!isConnected) {
//                //没有网络,无法使用
//                h.sendEmptyMessage(HANDLER_REFRESH_VIEW);
//            } else {
//                if (isWifi) {
                    downloadPlugin();
//                } else {
//                    changeMode(MODE_NOT_START);
//                }
//            }
        } else if (mPluginState == PluginState.NEED_UPGRADE) {
            // 老用户
            if (isConnected) {
                if (isWifi) {
                    downloadPlugin();
                } else {
                    //					// 比较当前时间与桌面安装时间，7天内，不提示更新
                    //					long current = System.currentTimeMillis();
                    //					if (current - ConfigPreferences.getInstance().getFirstLaunchTime() > PLUGIN_UPDATE_TIP_INTRAVEL) {
                    //						//展示更新页面
                    //						plugin_down_view.setVisibility(View.GONE);
                    //						desc.setVisibility(View.GONE);
                    //						plugin_update_tip.setVisibility(View.VISIBLE);
                    //					} else {
                    //						forward();
                    //					}
                    //展示更新页面
                    plugin_down_view.setVisibility(View.GONE);
                    desc.setVisibility(View.GONE);
                    plugin_update_tip.setVisibility(View.VISIBLE);
                }
            } else {
                if (isForceUpdate) {
                    h.sendEmptyMessage(HANDLER_REFRESH_VIEW);
                } else {
                    forward();
                }
            }
        } else {
            forward();
        }
    }

    private void forward() {
        if (needRestart != null && needRestart.containsKey(mPkgName)) {
            mTransfer.transfer(PluginState.NORMAL, mPkgName, mFileName, mIntent);
        } else {
            mTransfer.transfer(mPluginState, mPkgName, mFileName, mIntent);
        }
        PluginTransferUtil.statPluginVersion(mCtx, mFileName, mPkgName);
        if (needRestart != null) {
            Log.e("=====","======mPkgName:"+mPkgName+"--mFileName:"+mFileName);
            needRestart.put(mPkgName, mFileName);
        }
        if (!DynamicPluginUtil.isNeedRestart(mPkgName)) {
            deleteUnusePluginFile(mPkgName, this.mFileName, mCtx);
        }
        finish();
    }

    /**
     * 进入下载流程
     * <p>
     * 1 刷新文字
     * 2 改变按钮状态
     * 3开启下载线程
     *
     * @Function: com.nd.hilauncherdev.myphone.common.BaseTransferActivity.downloadPlugin
     * @Description:
     * @version:v1.0
     * @author:linyt
     * @date:2014年11月27日 下午4:20:45
     */
    private void downloadPlugin() {
        h.sendEmptyMessageDelayed(HANDLER_REFRESH_DESC, 1000L);
        changeMode(MODE_DOWNLOADING);
    //    mDownloadListener.onRefreshState(mCtx, 0);
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                startDownload();
            }
        });
    }

    private void startDownload() {
//        String url = PluginTransferUtil.getPluginDownloadUrl(mCtx, updateInfo);
//        if (StringUtil.isEmpty(url))
//            return;
//
//		BaseDownloadInfo dlInfo = new BaseDownloadInfo(mPkgName, FileType.FILE_DYNAMIC_APK.getId(), url, getDownloadName(mPkgName), BaseConfig.WIFI_DOWNLOAD_PATH, mFileName, "");
//		HashMap<String, String> extras = new HashMap<String, String>();
//		extras.put(DynamicApkFileHelper.EXTRAS_WIDGET_TYPE, "" + WidgetType.getIntValue(WidgetType.MYPHONE_TYPE));
//		extras.put(DynamicApkFileHelper.EXTRAS_WIDGET_POS_TYPE, "" + WidgetPositionType.getIntValue(WidgetPositionType.ONLINE_WIFI_TYPE));
//		dlInfo.setAdditionInfo(extras);
//		dlInfo.setPrioritySize(BaseDownloadInfo.PRIORITY_HITH_1);
//		DownloadManager.getInstance(this).addSilent23GTask(dlInfo, true);
        try {
            //InputStream inputStream = getResources().getAssets().open("com.xxm.dynicpath.jar");
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
                    boolean copyAssetsFile = FileUtil.copyAssetsFile(getApplicationContext(), "com.xxm.dynicpath.jar", BaseConfig.WIFI_DOWNLOAD_PATH, "com.xxm.dynicpath.jar");
                    if(copyAssetsFile){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                doStartUp();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean needDownload() {
        return mPluginState == PluginState.NEED_DOWNLOAD || mPluginState == PluginState.NEED_UPGRADE;
    }

    private Handler h = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_REFRESH_DESC:
                    if (!isDownSuc && null != desc) {
                        if (!isDescShow) {
                            switch (mPluginState) {
                                case NORMAL:
                                case NEED_DOWNLOAD:
                                    desc.setText(R.string.plugin_transfer_desc1);
                                    break;
                                case NEED_UPGRADE:
                                case OUT_OF_SYNC_FOR_OUTTER:
                                case OUT_OF_SYNC_FOR_INNER:
                                    desc.setText(R.string.plugin_transfer_desc3);
                                    break;
                                default:
                                    desc.setText(R.string.plugin_transfer_desc1);
                                    break;
                            }
                            isDescShow = true;
                        } else {
                            if (null != mDesc) {
                                if (mDescPos >= mDesc.length) {
                                    mDescPos = 0;
                                }
                                desc.setText(mDesc[mDescPos++]);
                                if (mDescPos >= mDesc.length) {
                                    isDescShow = false;
                                }
                            }
                        }
                        h.sendEmptyMessageDelayed(HANDLER_REFRESH_DESC, 5000L);
                    }
                    break;
                case HANDLER_REFRESH_VIEW:
                    plugin_content.setVisibility(View.GONE);
                    neterror_layout.setVisibility(View.VISIBLE);
                    h.removeMessages(HANDLER_REFRESH_DESC);
                    break;
                case HANDLER_REFRESH_UPDATE_LAUNCHER:
                    plugin_down_view.setVisibility(View.GONE);
                    plugin_update_tip.setVisibility(View.GONE);
                    updateLauncherBtn.setVisibility(View.VISIBLE);
                    h.removeMessages(HANDLER_REFRESH_UPDATE_LAUNCHER);
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (isForceUpdate && isShowForceUpdateDialog) {
            System.exit(0);
            return;
        }
        super.onBackPressed();
        finish();
    }

    private String getDownloadName(final String pkgName) {
        return "".equals(mTitle) ? pkgName : "".equals(mModule) ? mTitle : mModule;
    }

    protected final ITransfer.PluginTransferListener getDefaultPluginTransferListener() {
        /**
         * 插件在assert目录下时使用默认transfer
         */
        if (!needDownload()) {
            return new ITransfer.PluginTransferListener() {
                @Override
                public void onPreparing(Context context) {
//					if (progressBar.getVisibility() != View.VISIBLE) {
//						progressBar.setVisibility(View.VISIBLE);
//					}
                    if (progressPercentText.getVisibility() != View.VISIBLE) {
                        progressPercentText.setVisibility(View.VISIBLE);
                    }
                    //					if (process.getVisibility() != View.VISIBLE) {
                    //						process.setVisibility(View.VISIBLE);
                    //					}
//					if (processSize.getVisibility() != View.VISIBLE) {
//						processSize.setVisibility(View.VISIBLE);
//					}
                    //					process.setText("40%");
                    progressPercentText.setText("40%");
                    progressBar.setProgress(40);
                }

                @Override
                public void onInstalling(Context context) {
                    //					process.setText("60%");
                    progressPercentText.setText("60%");
                    progressBar.setProgress(60);
                }

                @Override
                public void onRunning(Context context) {
                    //					process.setText("100%");
                    progressPercentText.setText("100%");
                    progressBar.setProgress(100);
                }

                @Override
                public void onTransferError(Context context, int code) {
                    Toast.makeText(context, getString(R.string.plugin_transfer_error), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onInstallSuc(Context context) {
                    //					process.setText("80%");
                    progressPercentText.setText("80%");
                    progressBar.setProgress(80);
                }

                @Override
                public void onRefreshState(Context context, int progress) {

                }
            };
        } else {
            return mDownloadListener;
        }
    }

//    class DownloadListener implements ITransfer.PluginTransferListener {
//
//        @Override
//        public void onPreparing(Context context) {
////			if (progressBar.getVisibility() != View.VISIBLE) {
////				progressBar.setVisibility(View.VISIBLE);
////			}
//            if (progressPercentText.getVisibility() != View.VISIBLE) {
//                progressPercentText.setVisibility(View.VISIBLE);
//            }
//            //			if (process.getVisibility() != View.VISIBLE) {
//            //				process.setVisibility(View.VISIBLE);
//            //			}
////			if (processSize.getVisibility() != View.VISIBLE) {
////				processSize.setVisibility(View.VISIBLE);
////			}
//            progressBar.setProgress(100);
//        }
//
//        @Override
//        public void onInstalling(Context context) {
//
//        }
//
//        @Override
//        public void onInstallSuc(Context context) {
//
//        }
//
//        @Override
//        public void onRunning(Context context) {
//
//        }
//
//        @Override
//        public void onTransferError(Context context, int code) {
//        }
//
//        @Override
//        public void onRefreshState(final Context context, int progress) {
//            if (progress <= 0) {
//                return;
//            }
//            if (animLayout != null) {
//                animLayout.setVisibility(View.VISIBLE);
////				animLayout.moveStone(progress);
//            }
////			if (progressBar.getVisibility() != View.VISIBLE) {
////				progressBar.setVisibility(View.VISIBLE);
////			}
//            if (progressPercentText.getVisibility() != View.VISIBLE) {
//                progressPercentText.setVisibility(View.VISIBLE);
//            }
//            //			if (process.getVisibility() != View.VISIBLE) {
//            //				process.setVisibility(View.VISIBLE);
//            //			}
//            if (processSize.getVisibility() != View.VISIBLE && !TextUtils.isEmpty(downloadSize) && !TextUtils.isEmpty(totalSize)) {
//                processSize.setVisibility(View.VISIBLE);
//            }
//            if (progress <= 99) {
//                //				process.setText(progress + "%");
//                processSize.setText(" (" + downloadSize + "/" + totalSize + ")");
//                progressBar.setProgress(progress);
//                progressPercentText.setText(progress + "%");
//            } else if (progress == 100) {
//                processSize.setText(" (" + downloadSize + "/" + downloadSize + ")");
//                progressBar.setProgress(progress);
//                progressPercentText.setText(progress + "%");
//                animLayout.stopAnim();
//                isDownSuc = true;
//                /** 延迟进入下一步 避免造成动画结束卡顿 */
//                //TODO:xuqunxing
//                h.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            PackageManager pm = getPackageManager();
//                            PackageInfo info = pm.getPackageArchiveInfo(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName, 0);
//                            if (info == null || TextUtils.isEmpty(info.packageName)) {
//                                if (mTryCount <= 2) {
//                                    CommonDialog.Builder result = new CommonDialog.Builder(context);
//                                    result.setTitle(context.getString(R.string.download_error_apk_title)).setMessage(context.getString(R.string.download_error_apk_content)).setPositiveButton(R.string.common_button_redownload, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            mTryCount++;
//                                            dialog.dismiss();
//                                            changeMode(MODE_NOT_START);
//                                            FileUtil.delFile(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName);
//                                            DownloadManager.getInstance(BaseTransferActivity.this).cancelSilentTask(mPkgName, true);
//                                            downloadPlugin();
//                                        }
//                                    });
//                                    result.create().show();
//                                } else {
//                                    ViewFactory.getAlertDialog(context, -1, context.getString(R.string.download_error_apk_title), context.getString(R.string.download_error_apk_content), context.getString(R.string.common_button_redownload), context.getString(R.string.download_error_apk_btn_install), new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            changeMode(MODE_NOT_START);
//                                            FileUtil.delFile(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName);
//                                            DownloadManager.getInstance(BaseTransferActivity.this).cancelSilentTask(mPkgName, true);
//                                            downloadPlugin();
//                                        }
//                                    }, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            mTryCount = 0;
//                                            doStartUp();
//                                        }
//                                    }).show();
//                                }
//                            } else {
//                                doStartUp();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 700);
//
//            }
//        }
//    }

    /**
     * 处理插件升级
     * 分成需要提示重启 和 不需要提示重启两种方式
     */
    private void doStartUp() {
        Log.e("======","======doStartUp");
        if (!DynamicPluginUtil.isNeedRestart(mPkgName)) {
            //不需要提示重启的方式
            /**
             * 取消原先的判断文件的方式，
             * 改为判断内存变量是否存在作为是否加载过的标志
             */
            //升级安装
            //TODO:xuqnxing  第一步  把下载好的jar文件复制到指定目录下，然后删除这个目录下的这个文件
            String newPluinFileName = generatePluginNameIncreaseVer(mFileName);
            FileUtil.copy(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName, WidgetType.MYPHONE_TYPE.getBaseDir() + newPluinFileName);
            FileUtil.renameFile(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName, DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName + "rename");
            FileUtil.delFile(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName + "rename");
            if(com.felink.corelib.kitset.FileUtil.isFileExits("/data/data/com.xxm.dynicapp/plugin/com.xxm.dynicpath.1.jar")){
                Log.e("======","======插件存在");
            }
            //设置插件可以使用，并且启动插件
            mTransfer.transfer(PluginState.OUTTER_PLUGIN, mPkgName, newPluinFileName, mIntent);
            if (null != needRestart) {
                Log.e("=====","======mPkgName11111111:"+mPkgName+"--mFileName:"+mFileName);
                needRestart.put(mPkgName, newPluinFileName);
            }
            PluginTransferUtil.statPluginVersion(mCtx, mFileName, mPkgName);
            deleteUnusePluginFile(mPkgName, newPluinFileName, mCtx);
            finish();

        } else {
            //需要提示重启的方式
            if (DynamicPluginUtil.isPluginJarExisted(this, DynamicConstant.PLUGIN_DIR, mPkgName)) {
                showRestart();
            } else {
                //进行安装
                FileUtil.moveFile(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName, WidgetType.MYPHONE_TYPE.getBaseDir() + mFileName);
                mTransfer.transfer(ITransfer.PluginState.OUTTER_PLUGIN, mPkgName, mFileName, mIntent);
                PluginTransferUtil.statPluginVersion(mCtx, mFileName, mPkgName);
                if (needRestart != null) {
                    Log.e("=====","======2222222222mPkgName:"+mPkgName+"--mFileName:"+mFileName);
                    needRestart.put(mPkgName, mFileName);
                }
                finish();
            }
        }
    }


    class DownloadListener implements ITransfer.PluginTransferListener {

        @Override
        public void onPreparing(Context context) {
//			if (progressBar.getVisibility() != View.VISIBLE) {
//				progressBar.setVisibility(View.VISIBLE);
//			}
            if (progressPercentText.getVisibility() != View.VISIBLE) {
                progressPercentText.setVisibility(View.VISIBLE);
            }
            //			if (process.getVisibility() != View.VISIBLE) {
            //				process.setVisibility(View.VISIBLE);
            //			}
//			if (processSize.getVisibility() != View.VISIBLE) {
//				processSize.setVisibility(View.VISIBLE);
//			}
            progressBar.setProgress(100);
        }

        @Override
        public void onInstalling(Context context) {

        }

        @Override
        public void onInstallSuc(Context context) {

        }

        @Override
        public void onRunning(Context context) {

        }

        @Override
        public void onTransferError(Context context, int code) {
           // Log.e("======","======onTransferError:"+code);
        }

        @Override
        public void onRefreshState(final Context context, int progress) {

//            if (progress <= 0) {
//                return;
//            }
//            if (animLayout != null) {
//                animLayout.setVisibility(View.VISIBLE);
////				animLayout.moveStone(progress);
//            }
////			if (progressBar.getVisibility() != View.VISIBLE) {
////				progressBar.setVisibility(View.VISIBLE);
////			}
//            if (progressPercentText.getVisibility() != View.VISIBLE) {
//                progressPercentText.setVisibility(View.VISIBLE);
//            }
//            //			if (process.getVisibility() != View.VISIBLE) {
//            //				process.setVisibility(View.VISIBLE);
//            //			}
//            if (processSize.getVisibility() != View.VISIBLE && !TextUtils.isEmpty(downloadSize) && !TextUtils.isEmpty(totalSize)) {
//                processSize.setVisibility(View.VISIBLE);
//            }
//            if (progress <= 99) {
//                //				process.setText(progress + "%");
//                processSize.setText(" (" + downloadSize + "/" + totalSize + ")");
//                progressBar.setProgress(progress);
//                progressPercentText.setText(progress + "%");
//            } else if (progress == 100) {
//                processSize.setText(" (" + downloadSize + "/" + downloadSize + ")");
//                progressBar.setProgress(progress);
//                progressPercentText.setText(progress + "%");
//                animLayout.stopAnim();
//                isDownSuc = true;
//                /** 延迟进入下一步 避免造成动画结束卡顿 */
//                h.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            PackageManager pm = getPackageManager();
//                            PackageInfo info = pm.getPackageArchiveInfo(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName, 0);
//                            if (info == null || TextUtils.isEmpty(info.packageName)) {
//                                if (mTryCount <= 2) {
//                                    CommonDialog.Builder result = new CommonDialog.Builder(context);
//                                    result.setTitle(context.getString(R.string.download_error_apk_title)).setMessage(context.getString(R.string.download_error_apk_content)).setPositiveButton(R.string.common_button_redownload, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            mTryCount++;
//                                            dialog.dismiss();
//                                            changeMode(MODE_NOT_START);
//                                            FileUtil.delFile(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName);
//                                            DownloadManager.getInstance(BaseTransferActivity.this).cancelSilentTask(mPkgName, true);
//                                            downloadPlugin();
//                                        }
//                                    });
//                                    result.create().show();
//                                } else {
//                                    ViewFactory.getAlertDialog(context, -1, context.getString(R.string.download_error_apk_title), context.getString(R.string.download_error_apk_content), context.getString(R.string.common_button_redownload), context.getString(R.string.download_error_apk_btn_install), new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            changeMode(MODE_NOT_START);
//                                            FileUtil.delFile(DynamicConstant.WIFI_DOWNLOAD_PATH + mFileName);
//                                            DownloadManager.getInstance(BaseTransferActivity.this).cancelSilentTask(mPkgName, true);
//                                            downloadPlugin();
//                                        }
//                                    }, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            mTryCount = 0;
//                                            doStartUp();
//                                        }
//                                    }).show();
//                                }
//                            } else {
//                                doStartUp();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 700);

//            }
        }
    }


    private void showRestart() {
        if (null != needRestart) {
            Log.e("=====","======333333333333mPkgName:"+mPkgName+"--mFileName:"+mFileName);
            needRestart.put(mPkgName, mFileName);
        }
        /**
         * 魔力球自更新
         */
        //		if("com.baidu.launcher.petfloat".equals(mPkgName)){
        //			return;
        //		}
        if (isForceUpdate) {
            isShowForceUpdateDialog = true;
            ViewFactory.getAlertDialog(mCtx, getString(R.string.common_tip), getString(R.string.plugin_update_restart_tip), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    System.exit(0);
                }
            }, true).show();
        } else {
            ViewFactory.getAlertDialogEx(mCtx, -1, getString(R.string.common_tip), getString(R.string.plugin_update_restart_tip), getString(R.string.plugin_update_restart), getString(R.string.plugin_update_skip), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //FileUtil.moveFile(CommonGlobal.WIFI_DOWNLOAD_PATH + mFileName, WidgetType.MYPHONE_TYPE.getBaseDir() + mFileName);
                    System.exit(0);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    forward();
                }
            }).show();
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        container.setTitle(mTitle);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    public void setModule(int moduleId) {
        mModule = getString(moduleId);
    }

    public void setDesc(int descId) {
        setDesc(new int[]{descId});
    }

    public void setDesc(int[] descIds) {
        if (null != descIds && descIds.length > 0) {
            mDesc = new String[descIds.length];
            for (int i = 0; i < descIds.length; i++) {
                mDesc[i] = getString(descIds[i]);
            }
        }
    }

//	public boolean isForceUpdate() {
//		return isForceUpdate;
//	}

    public void setForceUpdate(boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    /**
     * 最小支持使用版本(请在doDefaultTransferInit之前调用)
     * <p>Title: setLowestPluginRunningVersionCode</p>
     * <p>Description: </p>
     *
     * @param versionCode
     * @author maolinnan_350804
     */
    public void setLowestPluginRunningVersionCode(int versionCode) {
        this.lowestPluginRunningVersionCode = versionCode;
    }
    
    //TODO:zhq 处理升级宿主APP的流程
    public abstract void onUpgradeApp();
}
