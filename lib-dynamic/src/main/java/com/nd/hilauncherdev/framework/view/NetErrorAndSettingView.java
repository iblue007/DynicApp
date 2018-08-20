package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nd.hilauncherdev.dynamic.R;
public class NetErrorAndSettingView extends LinearLayout {

	Button netSettingBtn;
	Button refreshBtn;
	TextView framework_viewfactory_err_textview;
	
	public NetErrorAndSettingView(Context context) {
		this(context,null);
	}

	public NetErrorAndSettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.theme_shop_v6_theme_neterror_setting, this, true);
		initView();
	}
	
	private void initView() {
		
		refreshBtn = (Button) findViewById(R.id.framework_viewfactory_refresh_btn); 
		netSettingBtn = (Button) findViewById(R.id.framework_viewfactory_err_btn); 
		netSettingBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
					//Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(getContext(), R.string.frame_viewfacotry_show_netsetting_err, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		framework_viewfactory_err_textview = (TextView) findViewById(R.id.framework_viewfactory_err_textview);
		framework_viewfactory_err_textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					//判断是用户未登陆错误点击后自动登陆
					if (framework_viewfactory_err_textview.getText().toString().contains("-8")){
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setRefeshClickListening(OnClickListener clickListener){
		refreshBtn.setOnClickListener(clickListener);
	}
}
