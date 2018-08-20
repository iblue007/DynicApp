package com.felink.corelib.kitset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.felink.corelib.config.BaseConfig;
import com.nd.analytics.NdAnalytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 获取手机相关参数的工具类
 */
public class TelephoneUtil {
	private static final String TAG = "TelephoneUtil";

	/**
	 * 统计SDK新增接口，机型，增加前缀
	 */
	public static final String CONST_STRING_PREFIX_MAC = "HIM_";

	/**
	 * 区别分支版本升级统计标志值
	 */
	public static final String CONST_STRING_PREFIX_IMEI = "HIE_";

	/**
	 * 统计SDK新增接口开关，true 打开，false关闭
	 */
	public static boolean switchPrefix = false;

    /**
     * MIUI版本号
     */
    private static String MIUI_VERSION;

    /**
     * 是否读取MIUI
     */
    private static boolean IS_READ_MIUI = false ;

	/**
	 * 取得IMEI号
	 * @param ctx
	 * @return String
	 */
	public static String getIMEI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
		if (tm == null)
			return "";
		String result = null;
		try{
			result = tm.getDeviceId();
		}catch (Exception e) {
			e.printStackTrace();
		}
		if (result == null)
			return "";
		
		return result;
	}

	/**
	 * 取得IMSI号
	 * @param ctx
	 * @return String
	 */
	public static String getIMSI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
		if (tm == null)
			return "";
		
		String result = tm.getSubscriberId();
		if (result == null)
			return "";

		return result;
	}
	


	/**
	 * 获取手机型号，若开启前缀开关，会有"HiLauncherM_"前缀 如 HiLauncherM_milestone
	 *                                         否则为milestone<br>
	 *                                         判断机型时候，建议使用 contains 方法比对
	 * @return String
	 */
	public static String getMachineName() {
		/**
		 * 开关打开，添加后缀
		 */
		if (switchPrefix) {
			return CONST_STRING_PREFIX_MAC + Build.MODEL;
		} else {
			return Build.MODEL;
		}
	}

	/**
	 * 获取字符串型的固件版本，如1.5、1.6、2.1
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String getFirmWareVersion() {
		final String version_3 = "1.5";
		final String version_4 = "1.6";
		final String version_5 = "2.0";
		final String version_6 = "2.0.1";
		final String version_7 = "2.1";
		final String version_8 = "2.2";
		final String version_9 = "2.3";
		final String version_10 = "2.3.3";
		final String version_11 = "3.0";
		final String version_12 = "3.1";
		final String version_13 = "3.2";
		final String version_14 = "4.0";
		final String version_15 = "4.0.3";
		final String version_16 = "4.1.1";
		final String version_17 = "4.2";
		final String version_18 = "4.3";
		final String version_19 = "4.4";
		final String version_20 = "4.4W";
		final String version_21 = "5.0";
		final String version_22 = "5.1";
		final String version_23 = "6.0";
		String versionName = "";
		try {
			// android.os.Build.VERSION.SDK_INT Since: API Level 4
			// int version = android.os.Build.VERSION.SDK_INT;
			int version = Integer.parseInt(Build.VERSION.SDK);
			switch (version) {
			case 3:
				versionName = version_3;
				break;
			case 4:
				versionName = version_4;
				break;
			case 5:
				versionName = version_5;
				break;
			case 6:
				versionName = version_6;
				break;
			case 7:
				versionName = version_7;
				break;
			case 8:
				versionName = version_8;
				break;
			case 9:
				versionName = version_9;
				break;
			case 10:
				versionName = version_10;
				break;
			case 11:
				versionName = version_11;
				break;
			case 12:
				versionName = version_12;
				break;
			case 13:
				versionName = version_13;
				break;
			case 14:
				versionName = version_14;
				break;
			case 15:
				versionName = version_15;
				break;
			case 16:
				versionName = version_16;
				break;
			case 17:
				versionName = version_17;
				break;
				case 18:
					versionName = version_18;
					break;
				case 19:
					versionName = version_19;
					break;
				case 20:
					versionName = version_20;
					break;
				case 21:
					versionName = version_21;
					break;
				case 22:
					versionName = version_22;
					break;
				case 23:
					versionName = version_23;
					break;
			default:
				versionName = version_23;
			}
		} catch (Exception e) {
			versionName = version_23;
		}
		return versionName;
	}

	/**
	 * 获取软件版本名称
	 */
	public static String getVersionName(Context ctx) {
		return getVersionName(ctx, ctx.getPackageName());
	}

	/**
	 * 获取versionName
	 * @param context
	 * @param packageName
	 * @return String
	 */
	public static String getVersionName(Context context, String packageName) {
		String versionName = "";
		try {
			PackageInfo packageinfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			versionName = packageinfo.versionName;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return versionName;
	}

	/**
	 * 获取软件版本号 code
	 * @param ctx
	 * @return int
	 */
	public static int getVersionCode(Context ctx) {
		return getVersionCode(ctx, ctx.getPackageName());
	}

	/**
	 * 获取软件版本号 code
	 * @param ctx
	 * @param packageName
	 * @return int
	 */
	public static int getVersionCode(Context ctx, String packageName) {
		int versionCode = 0;
		try {
			PackageInfo packageinfo = ctx.getPackageManager().getPackageInfo(packageName, PackageManager.GET_INSTRUMENTATION);
			versionCode = packageinfo.versionCode;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return versionCode;
	}

	/**
	 * 比较versionName,是否存在新版本
	 * @param newVersionName 新版本号
	 * @param oldeVersionName 旧版本号
	 * @return 新版本号> 旧版本号 return true
	 */
	public static boolean isExistNewVersion(String newVersionName, String oldeVersionName) {
		if (oldeVersionName.toLowerCase().startsWith("v")) {
			oldeVersionName = oldeVersionName.substring(1, oldeVersionName.length());
		}
		if (newVersionName.toLowerCase().startsWith("v")) {
			newVersionName = newVersionName.substring(1, oldeVersionName.length());
		}

		if (oldeVersionName == null || newVersionName == null) {
			return false;
		}
		if (oldeVersionName.trim().length() == 0 || newVersionName.trim().length() == 0) {
			return false;
		}
		try {
			List<String> codes = parser(oldeVersionName.trim(), '.');
			List<String> versionCodes = parser(newVersionName.trim(), '.');
			for (int i = 0; i < codes.size(); i++) {
				if (i > (versionCodes.size() - 1)) {
					return false;
				}
				int a = Integer.parseInt(codes.get(i).trim());
				int b = Integer.parseInt(versionCodes.get(i).trim());
				if (a < b) {
					return true;
				} else if (a > b) {
					return false;
				}
			}
			if (codes.size() < versionCodes.size()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 2.60.3=>[2,60,3]
	 * 
	 * @param value
	 * @param c
	 * @return List<String>
	 */
	private static List<String> parser(String value, char c) {
		List<String> ss = new ArrayList<String>();
		char[] cs = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cs.length; i++) {
			if (c == cs[i]) {
				ss.add(sb.toString());
				sb = new StringBuffer();
				continue;
			}
			sb.append(cs[i]);
		}
		if (sb.length() > 0) {
			ss.add(sb.toString());
		}
		return ss;
	}

	/**
	 * 网络是否可用
	 * @param context
	 * @return boolean
	 */
	public synchronized static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		if (context == null) {
			return result;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (null != connectivityManager) {
			networkInfo = connectivityManager.getActiveNetworkInfo();
			if (null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnected()) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * wifi是否启动
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isWifiEnable(Context ctx) {
		if(ctx == null){
			return false;
		}
		try{
			return isWifiNetwork(ctx);
		} catch (Exception e) {
			e.printStackTrace();
			return isWifiOpen(ctx);
		}
	}

	private static boolean isWifiOpen(Context ctx) {
		try {
			Object obj = ctx.getSystemService(Context.WIFI_SERVICE);
			if (obj == null)
				return false;
			
			WifiManager wifiManager = (WifiManager) obj;
			return wifiManager.isWifiEnabled();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean isWifiNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
	
	/**
	 * 返回网络连接方式
	 * @param ctx
	 * @return int
	 */
	public static int getNetworkState(Context ctx) {
		if (isWifiEnable(ctx)) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * sim卡是否存在
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isSimExist(Context ctx) {
		TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
		if (manager.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
			return false;
		else
			return true;
	}

	/**
	 * sd卡是否存在
	 * @return boolean
	 */
	public static boolean isSdcardExist() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 返回屏幕分辨率,字符串型。如 320x480
	 * @param ctx
	 * @return String
	 */
	public static String getScreenResolution(Context ctx) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		String resolution = width + "x" + height;
		return resolution;
	}

	/**
	 * 返回屏幕分辨率,数组型
	 * @param ctx
	 * @return int[]
	 */
	public static int[] getScreenResolutionXY(Context ctx) {
		int[] resolutionXY = new int[2];
		if (resolutionXY[0] != 0) {
			return resolutionXY;
		}
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		resolutionXY[0] = metrics.widthPixels;
		resolutionXY[1] = metrics.heightPixels;
		return resolutionXY;
	}

	/**
	 * 返回屏幕密度
	 * @param ctx
	 * @return float
	 */
	public static float getScreenDensity(Context ctx) {
		return ctx.getResources().getDisplayMetrics().density;
	}

	/**
	 * 查询系统广播
	 * @param ctx
	 * @param actionName
	 * @return boolean
	 */
	public static boolean queryBroadcastReceiver(Context ctx, String actionName) {
		PackageManager pm = ctx.getPackageManager();
		try {
			Intent intent = new Intent(actionName);
			List<ResolveInfo> apps = pm.queryBroadcastReceivers(intent, 0);
			if (apps.isEmpty())
				return false;
			else
				return true;
		} catch (Exception e) {
			Log.d(TAG, "queryBroadcastReceivers: " + e.toString());
			return false;
		}
	}



	/**
	 * 获取数字型API_LEVEL 如：4、6、7
	 * @return int
	 */
	@SuppressWarnings("deprecation")
	public static int getApiLevel() {
		int apiLevel = 7;
		try {
			apiLevel = Integer.parseInt(Build.VERSION.SDK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apiLevel;
		// android.os.Build.VERSION.SDK_INT Since: API Level 4
		// return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 获取CPU_ABI
	 * @return String
	 */
	public static String getCPUABI() {
		String abi = Build.CPU_ABI;
		abi = (abi == null || abi.trim().length() == 0) ? "" : abi;
		// 检视是否有第二类型，1.6没有这个字段
		try {
			String cpuAbi2 = Build.class.getField("CPU_ABI2").get(null).toString();
			cpuAbi2 = (cpuAbi2 == null || cpuAbi2.trim().length() == 0) ? null : cpuAbi2;
			if (cpuAbi2 != null) {
				abi = abi + "," + cpuAbi2;
			}
		} catch (Exception e) {
		}
		return abi;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	/**
	 * 是否中文环境
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isZh(Context ctx) {
		Locale lo = ctx.getResources().getConfiguration().locale;
		if (lo.getLanguage().equals("zh"))
			return true;
		return false;
	}

	/**
	 * Log显示手机基本信息
	 */
	public static void logPhoneState() {
		Log.i(TAG, "MachineName:" + TelephoneUtil.getMachineName());
		Log.i(TAG, "Resolution WxH:" + ScreenUtil.getScreenWH()[0] + ":" + ScreenUtil.getScreenWH()[1]);
		Log.i(TAG, "Densit:" + ScreenUtil.getDensity());
		Log.i(TAG, "DensityDpi:" + ScreenUtil.getMetrics().densityDpi);
	}

	/**
	 * 是否拥有root权限
	 */
	public static boolean hasRootPermission() {
		boolean rooted = true;
		try {
			File su = new File("/system/bin/su");
			if (su.exists() == false) {
				su = new File("/system/xbin/su");
				if (su.exists() == false) {
					rooted = false;
				}
			}
		} catch (Exception e) {
			rooted = false;
		}
		return rooted;
	}

	/**
	 * 获取当前语言
	 * @return String
	 */
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * 获取手机mac地址
	 * @param ctx
	 * @return String
	 */
	public static String getLocalMacAddress(Context ctx) {
		WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获取手机上网类型(cmwap/cmnet/wifi/uniwap/uninet)
	 * @param ctx
	 * @return String
	 */
	public static String getNetworkTypeName(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (null == info || null == info.getTypeName()) {
			return "unknown";
		}
		return info.getTypeName();
	}

	/**
	 * 获取网络类型值，获取服务器端数据URL中需要用到
	 * @param ctx
	 * @return String
	 */
	public static String getNT(Context ctx) {
		/**
		 * 0 未知
		 * 
		 * 10 WIFI网络
		 * 
		 * 20 USB网络
		 * 
		 * 31 联通
		 * 
		 * 32 电信
		 * 
		 * 53 移动
		 * 
		 * IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
		 * 
		 * IMSI共有15位，其结构如下： MCC+MNC+MIN MNC:Mobile NetworkCode，移动网络码，共2位
		 * 在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
		 */
		String imsi = getIMSI(ctx);
		String nt = "0";
		if (TelephoneUtil.isWifiEnable(ctx)) {
			nt = "10";
		} else if (imsi == null) {
			nt = "0";
		} else if (imsi.length() > 5) {
			String mnc = imsi.substring(3, 5);
			if (mnc.equals("00") || mnc.equals("02")) {
				nt = "53";
			} else if (mnc.equals("01")) {
				nt = "31";
			} else if (mnc.equals("03")) {
				nt = "32";
			}
		}
		return nt;
	}

	/**
	 * 扫描SDCARD的指定目录 如动态生成图片到SDCARD，但是相册中看不见，是因为尚未被扫描到系统多媒体库中。
	 * @param dir 目录
	 */
	public static void scannerSdcardDir(final String dir, final Context context) {
		try {

			File dFile = new File(dir);
			if (dFile.exists() && dFile.isDirectory()) {
				File[] fileList = dFile.listFiles();
				for (File file : fileList) {
					scannerSdcardFile(file.getAbsolutePath(), context);
				}
			}

		} catch (Exception e) {
		}

	}

	/**
	 * 扫描SDCARD的指定文件，使其生效 如动态生成图片到SDCARD，但是相册中看不见，是因为尚未被扫描到系统多媒体库中。
	 * @param filePath
	 */
	public static void scannerSdcardFile(String filePath, Context context) {
		int sdkLevel = getApiLevel();
		if (sdkLevel > 7)
			MediaScannerConnection.scanFile(context, new String[] { filePath }, new String[] { "image/jpeg", "image/png" }, null);
		else {
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(new File(filePath));
			intent.setData(uri);
			context.sendBroadcast(intent);
		}
	}

	/**
	 * 是否是小米2手机
	 * @return boolean
	 */
	public static boolean isMI2Moble() {
		try {
			return TelephoneUtil.getMachineName().contains("MI 2");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是小米手机
	 * @return boolean
	 */
	public static boolean isMIMoble() {
		try {
			return TelephoneUtil.getManufacturer().equalsIgnoreCase("xiaomi");
//			return TelephoneUtil.getMachineName().contains("MI-ONE") || TelephoneUtil.getMachineName().contains("MI 2");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是HTC G13手机
	 * @return boolean
	 */
	public static boolean isHTC_G13_Mobile() {
		try {
			return getMachineName().contains("HTC A510e");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是Note2
	 * @return boolean
	 */
	public static boolean isNote2() {
		try {
			return (getMachineName().contains("GT-N71")|| getMachineName().contains("SCH-N719"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是HTC One
	 * @return boolean
	 */
	public static boolean isHTC_ONE() {
		try {
			return getMachineName().contains("HTC 802") || getMachineName().contains("HTC 801e") || getMachineName().contains("HTC One");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是HTC
	 * @return
	 */
	public static boolean isHTCPhone() {
		try {
			return getMachineName().toLowerCase().contains("htc");
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isLGPhone() {
		try {
			return getMachineName().toLowerCase().contains("lg-");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是oppo 4.2 的机子
	 * @return boolean
	 */
	public static boolean isOppo_42() {
		try {
			String name = TelephoneUtil.getManufacturer();
			String version = TelephoneUtil.getFirmWareVersion();
			if ("OPPO".equalsIgnoreCase(name) && "4.2".equals(version)){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 是否是HTC4.0或以上的手机
	 * @return boolean
	 **/
	public static boolean isHTC_4OrAbove() {
		try {
			return (getManufacturer().equalsIgnoreCase("HTC")) && (getApiLevel() >= 14);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是夏新N79+, 系统2.3.5的手机
	 * @return boolean
	 **/
	public static boolean isAmoiN79_235() {		
		try {
			return (getMachineName().contains("AMOI_N79+")) && (Build.VERSION.RELEASE.equals("2.3.5"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是Oppo的一些旧机型
	 *  R805, T703等。
	 * @return boolean
	 **/
	public static boolean isOppoOldPhone() {
		try {
			return (getMachineName().contains("R805") || getMachineName().contains("T703") || getManufacturer().equalsIgnoreCase("alps"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是步步高手机
	 * @return boolean
	 **/
	public static boolean isVivoPhone() {
		try {
			return (getMachineName().contains("vivo") || getManufacturer().equalsIgnoreCase("BBK"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否天宇手机
	 *  W700型号（manufacturer为NVIDIA有误，所以不放入判断）
	 * @return boolean
	 **/
	public static boolean isKtouchPhone() {
		try {
			return getMachineName().contains("W700");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否酷派手机
	 * @return boolean
	 **/
	public static boolean isCoolpadPhone() {
		try {
			return (getMachineName().contains("Coolpad") || getManufacturer().equalsIgnoreCase("YuLong"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否联想手机
	 * @return boolean
	 **/
	public static boolean isLenovoPhone() {
		try {
			return (getMachineName().toLowerCase().contains("lenovo") || getManufacturer().toLowerCase().contains("lenovo"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否华为手机
	 * @return
	 */
	public static boolean isHuaweiPhone(){
		try {
			return getMachineName().toLowerCase().contains("huawei") || getManufacturer().toLowerCase().contains("huawei");
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isHuaweiMT1(){
		try {
			return isHuaweiPhone() && getMachineName().contains("MT1");
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isCoolpad8908(){
		try {
			return isCoolpadPhone() && getMachineName().contains("8908");
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isCoolpad8750(){
		try {
			return isCoolpadPhone() && getMachineName().contains("8750");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否联想手机K900
	 * @return boolean
	 **/
	public static boolean isLenovoPhoneK900(){
		try {
			return isLenovoPhone() && getMachineName().contains("K900");
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 是否摩托罗拉手机
	 * @return boolean
	 **/
	public static boolean isMotorolaPhone() {
		try {
			return (getManufacturer().toLowerCase().contains("motorola"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否是Sony的一些机型
	 * @return boolean
	 **/
	public static boolean isSonyPhone() {
		try {
			String manufacturer = getManufacturer(); 
			return (manufacturer.equalsIgnoreCase("SONY") || manufacturer.equalsIgnoreCase("Sony Ericsson"));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否为魅族固件为4.2以上手机
	 * @return
	 */
	public static boolean isMeizuPhone42(){
		try {
			return getManufacturer().toLowerCase().contains("meizu") && Build.VERSION.SDK_INT >= 17;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 是否为魅族手机
	 * @return
	 */
	public static boolean isMeizuPhone(){
		try {
			return getManufacturer().equalsIgnoreCase("meizu");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获取制造商
	 * @return String
	 */
	public static String getManufacturer() {
		return Build.MANUFACTURER;
	}
	/**
	 * 是否是谷歌nexus 6手机
	 * @return boolean
	 **/
	public static boolean isNexus6() {		
		try {
			return getMachineName().toLowerCase().contains("nexus 6");
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * 是否是谷歌nexus 6手机
	 * @return boolean
	 **/
	public static boolean isMINoteLte() {
		try {
			return getMachineName().toLowerCase().contains("mi note lte");
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 是否是酷派T
	 * @return boolean
	 **/
	public static boolean isCoolpadT1() {		
		try {
			return getMachineName().toLowerCase().contains("coolpad t1");
		} catch (Exception e) {
			return false;
		}
	}

    /**
     * 获取miui的版本号
     * 
     * @author zhouheqiang_91
     * @return 不是miui返回空，是miui返回miui版本
     * */
    public static String getMiuiVersion() {
        if(!IS_READ_MIUI) {
            IS_READ_MIUI = true;
            MIUI_VERSION = getProp("ro.miui.ui.version.code");
        }
        Log.e("getMiuiVersion", MIUI_VERSION+"");
        return MIUI_VERSION;
    }
    
	public static boolean isOppoColorOS200() {
		String version = getProp("ro.build.version.opporom");
		if (version != null && version.startsWith("V2.")) {
			return true;
		}
		return false;
	}

    /**
     * 通过命令行getprop获取系统配置
     * 
     * @author zhouheqiang_91
     * @return 不是miui返回空，是miui返回miui版本
     * */
    public static String getProp(String propName) {
        String result = null;
        try {
            Process process = Runtime.getRuntime().exec("getprop " + propName);
            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            String prop = input.readLine();
            ir.close();
            process.destroy();
            result = prop;
        } catch(Exception e) {
			result = null;
		} catch (Error e1) {
            result = null;
        }
        if ("".equals(result)) {
            result = null;
        }
        return result;
    }
    
    /**
	 * 是否是乐视x600手机
	 * 
	 * @return boolean
	 */
	public static boolean isLeShiX600() {
		try {
			return TelephoneUtil.getMachineName().contains("X600");
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 魅蓝
	 * @return boolean
	 **/
	public static boolean isBlueMeizu() {		
		try {
			return (getMachineName().equals("m2") && getManufacturer().equals("Meizu") );
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取versionName
	 *
	 * @param context
	 * @return String
	 */
	public static String getDivideVersion(Context context) {
		String versionName = "";
		try {
			PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			versionName = packageinfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}


	/**
	 * 通过反射的方法，获取CUID
	 *
	 * @param ctx
	 */
	public static String getCUID(Context ctx) {
		if (null == ctx)
			return "";

		return NdAnalytics.getCUID(ctx);
	}


	/**
	 * 根据包名判断apk是否安装
	 * @author dingdj
	 * Date:2014-6-17下午5:46:29
	 *  @param packageName
	 *  @return
	 */
	public static boolean isApkInstalled(Context ctx, String packageName){
		try {
			PackageManager pm = ctx.getPackageManager();
			pm.getPackageInfo(packageName, 0);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    /**获取app的版本号*/
    public static int getAppVersionCode(Context ctx,String appName){
		try {
			int versionCode = getVersionCode(ctx, appName);
			String versionName = getVersionName(ctx,appName);
			//Log.e("====","====code:"+versionCode+"==versionName:"+versionName);
			return versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**判断当前桌面版本是否 大于 某个版本（例如 9.2）*/
    public static boolean isCurrent91LauncherAboveVersion(Context context){
		if(TelephoneUtil.getAppVersionCode(context, BaseConfig.PANDAHOME2_PKG) >= 9298){
			return true;
		}
		return false;
	}
	/**判断当前桌面版本是否 大于 某个版本（例如 9.5）*/
	public static boolean isCurrent91LauncherAbove95(Context context){
		if(TelephoneUtil.getAppVersionCode(context, BaseConfig.PANDAHOME2_PKG) > 9498){
			return true;
		}
		return false;
	}
}
