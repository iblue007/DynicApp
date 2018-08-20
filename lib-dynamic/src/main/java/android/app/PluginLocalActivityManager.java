package android.app;

import android.content.Intent;
import android.view.Window;


import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.dynamic.bean.ActivityProcess;
import com.nd.hilauncherdev.dynamic.util.ManagerUtil;
import com.nd.hilauncherdev.dynamic.util.PluginLoaderUtil;
import com.nd.hilauncherdev.kitset.util.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class PluginLocalActivityManager extends LocalActivityManager {

	private ActivityGroup mActivityGroup;
	private String mDexpath;
	private String mPluginPackageName;
	private List<String> mActivityIds = new ArrayList<String>();

	public PluginLocalActivityManager(Activity parent, boolean singleMode, String dexPath, String pkgName) {
		super(parent, singleMode);
		this.mActivityGroup = (ActivityGroup) parent;
		this.mDexpath = dexPath;
		this.mPluginPackageName = pkgName;
	}

	@Override
	public Window startActivity(String id, Intent intent) {
		String className = PluginLoaderUtil.getStartClassName(mDexpath, intent);
		if ("".contains(className)) {
			MessageUtils.makeLongToast(mActivityGroup, R.string.dyanmic_plugin_err_activity_not_found);
			return null;
		}
		ActivityProcess activityProcess = ManagerUtil.getCurrentProcess(mActivityGroup);
		Intent newIntent;
		try {
			newIntent = ManagerUtil.assembleIntent(mActivityGroup, mDexpath, mPluginPackageName, className, intent, activityProcess);
			mActivityIds.add(id);
			return (Window) super.startActivity(id, newIntent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MessageUtils.makeLongToast(mActivityGroup, R.string.dyanmic_plugin_err_coding_wrong);
			mActivityGroup.finish();
			return null;
		}
	}

	@Override
	public void dispatchDestroy(boolean finishing) {
		for (String id : mActivityIds) {
			Activity activity = getActivity(id);
			if (null != activity) {
				try{
				activity.onDestroy();
				}catch(Exception e){
					e.printStackTrace();
				}catch(Error er){// 小米手机上会报错
					er.printStackTrace();
				}
			}
		}
	}
}