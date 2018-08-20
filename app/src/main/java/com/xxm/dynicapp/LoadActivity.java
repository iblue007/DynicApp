package com.xxm.dynicapp;

import com.nd.hilauncherdev.dynamic.Transfer.BaseTransferActivity;
import com.nd.hilauncherdev.dynamic.util.PluginConstant;

/**
 * Created by xuqunxing on 2018/6/25.
 */

public class LoadActivity extends BaseTransferActivity {

    public static final String PKG_NAME = "com.xxm.dynicpath";
    public static final String FILE_NAME = PKG_NAME + ".jar";
    public static final String CLASS_NAME = "com.xxm.dynicpath.MainActivity";

    @Override
    public void afterComponentInit() {
        getIntent().putExtra(PluginConstant.EXTRA_MAIN_CLASS_NAME, CLASS_NAME);
        //点击调用之后的第一步
        doDefaultTransferInit(PKG_NAME, FILE_NAME);
        setTitle("");
    }

    @Override
    public void onUpgradeApp() {

    }
}
