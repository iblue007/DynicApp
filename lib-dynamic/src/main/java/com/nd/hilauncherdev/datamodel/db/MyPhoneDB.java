package com.nd.hilauncherdev.datamodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nd.hilauncherdev.framework.db.AbstractDataBase;
import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadLogTable;

/**
 * 数据库工具类
 * 
 * @author youy
 * 
 */
public class MyPhoneDB extends AbstractDataBase {

	/**
	 * db版本号在3.1版本升至2，之前版本号位1 by pdw 在3.2.1升至3，增加小部件列表表 by lx
	 * db版本号在3.5.2版本升至4，之前版本号位3 by pdw 2013-01-11 db版本号在3.62版本升至6 by youy 文件扫描记录
	 * db版本号在5.1版本升至7，修改通用的下载管理模块，by hjiang
	 */
	private static final int VERSION = 7;
	private static final String DB_NAME = "myphone.db";

//	public static final String WIDGET_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS 'widget_manage_list' " + "('_id' INTEGER PRIMARY KEY AUTOINCREMENT ,'type' VARCHAR(4) NOT NULL,"
//			+ "'icon_name' TEXT NOT NULL,'packageName' VARCHAR(32) NOT NULL," + "'title' VARCHAR(32) NOT NULL,'description' TEXT)";
	public static String CREATE_LOCK_TABLE = "CREATE TABLE IF NOT EXISTS 'AppLockTable' ('pkg' varchar(150) NOT NULL,'lock'INTEGER default 0)";
	public static final String CREATE_CONFIG_TABLE = "CREATE TABLE IF NOT EXISTS Config ('ID' varchar(10) PRIMARY KEY NOT NULL, 'value' varchar(10))";

	// 文件扫描记录表 PATH 路劲,TYPE:类型 1日志,2临时,3空文件夹,4安装包,5大文件
//	public static final String CREATE_FILE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS FILE_HISTORY ('ID' INTEGER PRIMARY KEY AUTOINCREMENT , 'F_PATH' TEXT,'F_TYPE' INTEGER)";

	private static MyPhoneDB mMyPhoneDB;
	
	private MyPhoneDB(Context c) {
		super(c, DB_NAME, VERSION);
	}
	
	public static MyPhoneDB getInstance(Context c){
		if(mMyPhoneDB == null){
			mMyPhoneDB = new MyPhoneDB(c.getApplicationContext());
		}
		return mMyPhoneDB;
	}

	@Override
	public void onDataBaseCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LOCK_TABLE);
		// 下载记录表
		db.execSQL(DownloadLogTable.CREATE_TABLE);
//		db.execSQL(WIDGET_CREATE_TABLE);
		db.execSQL(CREATE_CONFIG_TABLE);

		// 文件扫描记录表
//		db.execSQL(CREATE_FILE_HISTORY_TABLE);
	}

	@Override
	public void onDataBaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 2) {
			db.execSQL(DownloadLogTable.CREATE_TABLE);
		}
//		if (oldVersion < 3) {
//			db.execSQL(WIDGET_CREATE_TABLE);
//		}

		// 折包时,添加一个myphone下的Config表(目前里面有:静默安全和卸载前备份提醒)
		if (oldVersion < 5) {
			db.execSQL(CREATE_CONFIG_TABLE);
		}

		// TODO 注意测试3.0-3.1,3.1-3.5.2,3.0-3.5.2
		if (oldVersion >= 2 && oldVersion < 4) {
			final String[] sqls = new DownloadLogTable().ALTER_TABLE;
			try {
				for (String sql : sqls) {
					db.execSQL(sql);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("MyPhoneDB", "Fixme:Error when upgrading db structure for log_download from " + oldVersion + " to " + newVersion);
				Log.e("MyPhoneDB", ex.getMessage());
			}
		}
//		if (oldVersion < 6) {// 文件扫描记录表
//			db.execSQL(CREATE_FILE_HISTORY_TABLE);
//		}
		
		// 将铃声下载、字体下载等合并到下载管理
		if (oldVersion < 7) {
			final String[] sqls = new DownloadLogTable().ALTER_TABLE_7;
			try {
				for (String sql : sqls) {
					db.execSQL(sql);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("MyPhoneDB", "Fixme:Error when upgrading db structure for log_download from " + oldVersion + " to " + newVersion);
				Log.e("MyPhoneDB", ex.getMessage());
			}
		}
	}

	@Override
	public void onDataBaseDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}