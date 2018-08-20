package com.felink.corelib.kitset;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * apk包安装应用工具类
 */
public class ApkInstaller {

	/**
	 * 具备静默安装的应用安装方法
	 * @param context
	 * @param apkFile
	 */
	public static void installApplicationShouldSilent(final Context context, final File apkFile) {
		if (TelephoneUtil.hasRootPermission())// 有root权限
		{
			// 是否开启静默安装
//			boolean isSilentInstall = isSilentInstallable(context);
//			if (isSilentInstall) {
				// 已开启静默安装，使用静默安装
//				installAppInThread(context, apkFile);
//			} else
				// 未开启静默安装，则采用普通安装
				installApplicationNormal(context, apkFile);

		} else {
			installApplicationNormal(context, apkFile);
		}

	}// end installApplicationShouldSilent

//	private static void installAppInThread(Context context, File apkFile) {
//		DownloadManager.getInstance(context).installAppSilent(apkFile);
//	}

	/**
	 * 判断apk文件是否能安装
	 * @param context
	 * @param apkFile APK文件的路径
	 * @return boolean
	 */
	public static boolean checkApkIfValidity(Context context, String apkFile) {
		if (StringUtil.isEmpty(apkFile))
			return false;
		final PackageManager pm = context.getPackageManager();
		final android.content.pm.PackageInfo pInfo = pm.getPackageArchiveInfo(apkFile, 0);
		if (pInfo == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 安装应用程序,普通安装方式
	 * @param ctx
	 * @param mainFile
	 * @return boolean
	 */
	public static boolean installApplicationNormal(Context ctx, File mainFile) {
		try {
			Uri data = Uri.fromFile(mainFile);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(data, "application/vnd.android.package-archive");
			ctx.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
