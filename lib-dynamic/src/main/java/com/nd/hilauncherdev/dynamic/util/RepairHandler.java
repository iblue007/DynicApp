package com.nd.hilauncherdev.dynamic.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.nd.hilauncherdev.dynamic.R;


public class RepairHandler extends Handler {

	private Context mContext;
	public static final int REPAIR_START = 1;
	public static final int REPAIR_RESULT = 2;
	public static final int REPAIR_OUTER_PLUGIN = 3;
	public static final int REPAIR_SUC_KILLED = 4;

	public RepairHandler(Context ctx, Looper looper) {
		super(looper);
		this.mContext = ctx;
	}

	public void handleMessage(Message msg) {
		int what = msg.what;
		switch (what) {
		case REPAIR_START:
			Toast.makeText(mContext, R.string.dyanmic_plugin_repair, Toast.LENGTH_SHORT).show();
			break;
		case REPAIR_OUTER_PLUGIN:
			Toast.makeText(mContext, R.string.dyanmic_plugin_repair_redownload, Toast.LENGTH_LONG).show();
			break;
		case REPAIR_RESULT:
			boolean result = (Boolean) msg.obj;
			if (result) {
				Toast.makeText(mContext, R.string.dyanmic_plugin_repair_suc, Toast.LENGTH_LONG).show();
			}
			this.sendEmptyMessageDelayed(REPAIR_SUC_KILLED, 2000);
			break;
		case REPAIR_SUC_KILLED:
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		default:
			break;
		}
	};
}
