package com.felink.corelib.kitset;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.felink.corelib.config.Global;

/**
 * 消息处理工具类
 */
public class MessageUtils {
	
	public static Toast toast;

	/**
	 * 描述:
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-10-23
	 * @param text
	 */
	public static void showOnlyToast(Context mContext, final String text) {
		try {
			mContext = Global.getApplicationContext();
			if (toast == null) {
                toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            } else {
                toast.setText(text);
            }
			//Log.e("====","====toast："+text);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 描述:
	 * 
	 * @author linqiang(866116)
	 * @Since 2012-10-23
	 * @param text
	 */
	public static void showOnlyToast(Context mContext, final int text) {
		mContext = Global.getApplicationContext();
		if (toast == null) {
			toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}
	
	public static void makeShortToast(Context ctx, CharSequence cs) {
		Toast.makeText(ctx, cs, Toast.LENGTH_SHORT).show();
	}
	
	public static void makeShortToast(Context ctx, int cs) {
		Toast.makeText(ctx, cs, Toast.LENGTH_SHORT).show();
	}
	
	public static void makeLongToast(Context ctx, int cs) {
		Toast.makeText(ctx, cs, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 发送广播消息
	 * @param ctx
	 * @param icon 图标
	 * @param title 标题
	 * @param msg 内容
	 * @param intent 广播Intent
	 */
	public static void makeBroadcastNotification(Context ctx, int icon, int title, int msg, Intent intent) {
		NotificationManager nManager = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);

//		Notification notif = new Notification(icon, ctx.getString(title), System.currentTimeMillis());
		PendingIntent pIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(ctx);
		Notification notif = builder.setContentTitle(ctx.getString(title))
				.setContentText(ctx.getString(msg))
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setContentIntent(pIntent)
				.build();

//		notif.flags = Notification.FLAG_AUTO_CANCEL;
//		notif.setLatestEventInfo(ctx, ctx.getResources().getString(title), ctx.getString(msg), pIntent);
		nManager.notify(title, notif);
	}
	
	/**
	 * 发送Activity消息
	 * @param ctx
	 * @param icon 图标
	 * @param title 标题
	 * @param msg 内容
	 * @param intent 广播Intent
	 */
	public static void makeActivityNotification(Context ctx, int icon, int title, int msg, Intent intent, boolean ifSoundVibarate) {
		NotificationManager nManager = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);

//		Notification notif = new Notification(icon, ctx.getString(title), System.currentTimeMillis());
		PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(ctx);
		Notification notif = builder.setContentTitle(ctx.getString(title))
				.setContentText(ctx.getString(msg))
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setContentIntent(pIntent)
				.build();


//		notif.flags = Notification.FLAG_AUTO_CANCEL;
//		notif.setLatestEventInfo(ctx, ctx.getResources().getString(title), ctx.getString(msg), pIntent);
		if (ifSoundVibarate) {
			notif.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		}
		nManager.notify(title, notif);
	}
	
	public static void makeActivityNotification(Context ctx, int icon, String title, String msg, Intent intent, boolean ifSoundVibarate) {
		NotificationManager nManager = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);

//		Notification notif = new Notification(icon, title, System.currentTimeMillis());
		PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(ctx);
		Notification notif = builder.setContentTitle(title)
				.setContentText(msg)
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setContentIntent(pIntent)
				.build();

		notif.flags = Notification.FLAG_AUTO_CANCEL;
//		notif.setLatestEventInfo(ctx, title, msg, pIntent);
		if (ifSoundVibarate) {
			notif.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		}
		nManager.notify(icon, notif);
	}
	
	
	/**
	 * 发送常驻通知，点击可消失
	 * @param ctx
	 * @param icon
	 * @param title
	 * @param msg
	 * @param intent
	 * @param ifSoundVibarate
	 */
	public static void makeActivityNotificationOnGoingOnce(Context ctx, int icon, int title, int msg, Intent intent, boolean ifSoundVibarate) {
		NotificationManager nManager = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);

		PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//		Notification notif = new Notification(icon, ctx.getString(title), System.currentTimeMillis());
		Notification.Builder builder = new Notification.Builder(ctx);
		Notification notif = builder.setContentTitle(ctx.getString(title))
				.setContentText(ctx.getString(msg))
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setOngoing(true)
				.setContentIntent(pIntent)
				.build();

//		notif.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;
//		notif.setLatestEventInfo(ctx, ctx.getResources().getString(title), ctx.getString(msg), pIntent);
		if (ifSoundVibarate) {
			notif.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		}
		nManager.notify(title, notif);
	}
	
	
	/**
	 * 发送通知,支持自定义声音
	 * @param ctx
	 * @param icon
	 * @param title
	 * @param msg
	 * @param intent
	 * @param soundUri 自定义声音资源的Uri
	 */
	public static void makeActivityNotification(Context ctx, int icon, int title, int msg, Intent intent, Uri soundUri) {
		NotificationManager nManager = (NotificationManager) ctx.getSystemService(Service.NOTIFICATION_SERVICE);

		PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		Notification notif = new Notification(icon, ctx.getString(title), System.currentTimeMillis());
		Notification.Builder builder = new Notification.Builder(ctx);
		Notification notif = builder.setContentTitle(ctx.getString(title))
				.setContentText(ctx.getString(msg))
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setContentIntent(pIntent)
				.build();
//		notif.flags = Notification.FLAG_AUTO_CANCEL;
//		notif.setLatestEventInfo(ctx, ctx.getResources().getString(title), ctx.getString(msg), pIntent);
		if (soundUri!=null){
			notif.sound = soundUri;
		}
		nManager.notify(title, notif);
	}


	/**
	 * @desc 展示定制Toast 提示
	 * @author linliangbin
	 * @time 2017/5/19 16:19
	 */
	public static void showCustomToast(Context context,String title,String msg){

		Toast customToast = Toast.makeText(context,title,Toast.LENGTH_LONG);
//		View view = LayoutInflater.from(context).inflate(com.felink.corelib.R.layout.layout_message_toast,null);
//		((TextView)view.findViewById(R.id.tv_toast_title)).setText(title);
//		((TextView)view.findViewById(R.id.tv_toast_message)).setText(msg);
//		customToast.setView(view);
//		customToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL,0, 0);
//		customToast.setText(msg);
//		customToast.setDuration(Toast.LENGTH_LONG);
		customToast.show();


	}
}
