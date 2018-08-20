package com.nd.hilauncherdev.framework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nd.hilauncherdev.webconnect.downloadmanage.model.DownloadLogTable;

/**
 * Created by linliangbin on 2016/10/18.
 */

public class BaseDataBase extends AbstractDataBase {


    private static final int VERSION = 1;
    public static final String DB_NAME = "videopaper.db";

    private static BaseDataBase mMyPhoneDB;

//    public static final String TIETU_ALBUM_TABLE_NAME = "album";
//    /** 合辑信息表*/
//    public static final String CREATE_TIETU_ALBUM_TABLE = "CREATE TABLE IF NOT EXISTS 'album' ('album_id' INTEGER PRIMARY KEY, 'title' varchar(150) , 'iconUrl' varchar(150), 'desc' varchar(100), 'localPath' varchar(150), 'type' INTEGER, 'cateId' INTEGER DEFAULT 0, 'createTime' INTEGER DEFAULT 0)";
//
//    public static final String TIETU_DIY_EXPRESSION_NAME = "diy_expr";
//    /** DIY表情表*/
//    public static String CREATE_DIY_EXPRESSION_TABLE = "CREATE TABLE IF NOT EXISTS 'diy_expr' ('tietu_id' INTEGER PRIMARY KEY,'album_id' INTEGER DEFAULT 0, 'icon_Url' varchar(150), 'downloadUrl' varchar(200), 'localPath' varchar(200), 'price_type' INTEGER , 'proPrice' FLOAT ,'price' FLOAT, 'title' varchar(200)  , 'desc' varchar(200) , 'type' INTEGER DEFAULT 0, 'createTime' INTEGER DEFAULT 0, 'template_id' INTEGER DEFAULT 0)";
//
//    public static final String TIETU_DOWNLOAD_EXPRESSION_NAME = "download_expr";
//    /** 下载的表情表*/
//    public static final String CREATE_DOWNLOAD_EXPRESSION_TABLE = "CREATE TABLE IF NOT EXISTS 'download_expr' ('tietu_id' INTEGER PRIMARY KEY,'album_id' INTEGER DEFAULT 0, 'icon_Url' varchar(150), 'downloadUrl' varchar(200), 'localPath' varchar(200), 'price_type' INTEGER , 'proPrice' FLOAT ,'price' FLOAT, 'title' varchar(200)  ,'desc' varchar(200), 'type' INTEGER DEFAULT 0, 'createTime' INTEGER DEFAULT 0,'template_id' INTEGER DEFAULT 0)";
//
//    public static final String TIETU_RECENT_DIY_EXPRESSION_NAME = "recent_diy_expr";
//    /** 最近使用的DIY表情表*/
//    public static final String CREATE_RECENT_DIY_EXPRESSION_TABLE = "CREATE TABLE IF NOT EXISTS " + TIETU_RECENT_DIY_EXPRESSION_NAME + " ('tietu_id' INTEGER PRIMARY KEY,'album_id' INTEGER DEFAULT 0, 'icon_Url' varchar(150), 'downloadUrl' varchar(200), 'localPath' varchar(200), 'price_type' INTEGER , 'proPrice' FLOAT ,'price' FLOAT, 'title' varchar(200)  ,'desc' varchar(200), 'type' INTEGER DEFAULT 0, 'count' INTEGER DEFAULT 0)";
//
//    public static final String TIETU_RECENT_ONLINE_EXPRESSION_NAME = "recent_online_expr";
//    /** 最近使用的ONLINE表情表*/
//    public static final String CREATE_RECENT_ONLINE_EXPRESSION_TABLE = "CREATE TABLE IF NOT EXISTS " + TIETU_RECENT_ONLINE_EXPRESSION_NAME + " ('tietu_id' INTEGER PRIMARY KEY,'album_id' INTEGER DEFAULT 0, 'icon_Url' varchar(150), 'downloadUrl' varchar(200), 'localPath' varchar(200), 'price_type' INTEGER , 'proPrice' FLOAT ,'price' FLOAT, 'title' varchar(200)  ,'desc' varchar(200), 'type' INTEGER DEFAULT 0, 'count' INTEGER DEFAULT 0)";

    private BaseDataBase(Context c) {
        super(c, DB_NAME, VERSION);
    }

    public static BaseDataBase getInstance(Context c){
        if(mMyPhoneDB == null){
            mMyPhoneDB = new BaseDataBase(c.getApplicationContext());
        }
        return mMyPhoneDB;
    }



    @Override
    public void onDataBaseCreate(SQLiteDatabase var1) {
        var1.execSQL(DownloadLogTable.CREATE_TABLE);
//        var1.execSQL(CREATE_TIETU_ALBUM_TABLE);
//        var1.execSQL(CREATE_DIY_EXPRESSION_TABLE);
//        var1.execSQL(CREATE_DOWNLOAD_EXPRESSION_TABLE);
//        var1.execSQL(CREATE_RECENT_DIY_EXPRESSION_TABLE);
//        var1.execSQL(CREATE_RECENT_ONLINE_EXPRESSION_TABLE);
    }

    @Override
    public void onDataBaseUpgrade(SQLiteDatabase var1, int var2, int var3) {

    }
}
