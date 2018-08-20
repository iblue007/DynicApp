package com.xxm.dynicapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xxm.dynicapp.floatt.FloatWindowService;
import com.xxm.dynicapp.hook.HookActivity;

public class MainActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static int REQUEST_PERMISSION_CODE = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        TextView hookTv = (TextView) findViewById(R.id.activity_hook_tv);
        TextView dynicTv = (TextView) findViewById(R.id.activity_dynic_tv);
        TextView floatTv = (TextView) findViewById(R.id.activity_dynic_float);
        TextView ajjlTv = (TextView) findViewById(R.id.activity_ajjl_click);

//        File file = new File("/data/data/com.xxm.dynicapp/");
//        if(file.exists()){
//            Log.e("======","======file_exits");
//        }
//        FileUtil.copyFolder("/data/data/com.xxm.dynicapp/", BaseConfig.WIFI_DOWNLOAD_PATH+"test");
//        FileUtil.copy("/data/data/com.xxm.dynicapp/", BaseConfig.WIFI_DOWNLOAD_PATH+"test");

        hookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HookActivity.class));
            }
        });
        dynicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoadActivity.class));
            }
        });
        floatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                startService(intent);
            }
        });

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        //Settings.canDrawOverlays(this)  检查是否有悬浮窗权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(!Settings.canDrawOverlays(this)){
//                //没有悬浮窗权限,跳转申请
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                startActivity(intent);
//            }
//        }


    }

    private static MainActivity instance;
    public static synchronized MainActivity getInstance() {
        return instance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivityClick", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }


}
