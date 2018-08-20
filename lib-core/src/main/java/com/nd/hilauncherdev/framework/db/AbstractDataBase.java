package com.nd.hilauncherdev.framework.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/10/16.
 */
public abstract class AbstractDataBase {
    static final String TAG = "AbstractDataBase";
    private Context context;
    private DBHelper helper;
    private SQLiteDatabase mDb;

    public static String DOWNGRADE_EXCEPTION = "downgrade_exception";

    public AbstractDataBase(Context c, String dbName, int version) {
        context = c;
        helper = new DBHelper(context, dbName, version);
    }

    /**
     * Description:数据库助手，可以通过此类对数据库进行操作。 <br>
     */
    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String dbName, int version) {
            super(context, dbName, null, version);
        }

        /**
         * 创建数据库时候执行方法。
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, "onCreate");
            onDataBaseCreate(db);
        }

        /**
         * 如果数据库版本更新了，则在此方法提供更新表结构的sql。
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "onUpgrade, oldVersion:" + oldVersion + ", newVersion:" + newVersion);
            onDataBaseUpgrade(db, oldVersion, newVersion);
        }

        @SuppressWarnings("unused")
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //不支持降级，抛出异常
            throw new SQLiteException(DOWNGRADE_EXCEPTION +" Can't downgrade database from version " +
                    oldVersion + " to " + newVersion);
//			onDataBaseDowngrade(db, oldVersion, newVersion);
        }
    }

    public abstract void onDataBaseCreate(SQLiteDatabase db);

    public abstract void onDataBaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    @Deprecated
    public void onDataBaseDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    /**
     * 关闭数据库连接。
     */
    public boolean close() {
        try {
//			helper.close();
//			if (mDb != null) {
//				mDb.close();
//			}
            return true;
        } catch (SQLException s) {
            Log.e(TAG, "close db error" + s.toString());
            return false;
        }
    }

    private void getReadableDatabaseSure() {
//		if (mDb == null || !mDb.isOpen()) {
//			mDb = helper.getReadableDatabase();
//		} else if (!mDb.isReadOnly()) {
//			mDb.close();
//			mDb = helper.getReadableDatabase();
//		}
        mDb = helper.getReadableDatabase();
    }

    /**
     * 查询数据。
     */
    public Cursor query(String sql) {
        getReadableDatabaseSure();
        return mDb.rawQuery(sql, null);
    }

    /**
     * 查询数据。
     * cfb add
     */
    public Cursor query(String sql, String[] selectionArgs) {
        getReadableDatabaseSure();
        return mDb.rawQuery(sql, selectionArgs);
    }

    /**
     *
     * @Title: query
     * @Description: 查询单个条件
     * @author linyt
     * @date 2012-8-8
     * @param table 表名
     * @param key 查询字段
     * @param value 查询字段值
     * @param sort 排序
     * @return
     */
    public Cursor query(String table, String key, String value, String sort) {
        getReadableDatabaseSure();
        Cursor cursor = mDb.query(table, null, key + "=?", new String[] { value }, null, null, sort);
        return cursor;
    }

    /**
     *
     * @Title: query
     * @Description: 查询给定表所有数据
     * @author linyt
     * @date 2012-8-8
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        getReadableDatabaseSure();
        Cursor cursor = mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    /**
     * 修改数据。
     * cfb add
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        makesureWriteable();
        return mDb.update(table, values, whereClause, whereArgs);
    }

    /**
     * 增加数据。
     * cfb add
     */
    public long insertOrThrow(String table, String nullColumnHack, ContentValues values) {
        makesureWriteable();
        return mDb.insertOrThrow(table, nullColumnHack, values);
    }

    /**
     * 执行SQL语句。
     */
    public boolean execSQL(String sql) {
        makesureWriteable();
        try {
            mDb.execSQL(sql);
            return true;
        } catch (SQLException s) {
            Log.e(TAG, "execSQL:" + sql + " ex:" + s.toString());
            return false;
        }
    }

    /**
     * 执行SQL语句。
     */
    public boolean delete(String table) {
        makesureWriteable();
        try {
            mDb.delete(table, null, null);
            return true;
        } catch (SQLException s) {
            Log.e(TAG, "ERROR when delete " + table);
            return false;
        }
    }

    /**
     * 执行SQL语句。
     */
    public boolean delete(String table, String where, String[] whereValue) {
        makesureWriteable();
        try {
            mDb.delete(table, where, whereValue);
            return true;
        } catch (SQLException s) {
            Log.e(TAG, "ERROR when delete " + table);
            return false;
        }
    }

    private void makesureWriteable() {
//		if (mDb != null && mDb.isOpen()) {
//			if (mDb.isReadOnly()) {
//				mDb.close();
//				mDb = helper.getWritableDatabase();
//			}
//		} else {
//			mDb = helper.getWritableDatabase();
//		}
        mDb = helper.getWritableDatabase();
    }

    /**
     * 执行SQL语句。
     */
    public boolean execSQL(String sql, Object[] obj) {
        makesureWriteable();
        try {
            mDb.execSQL(sql, obj);
            return true;
        } catch (SQLException s) {
            Log.e(TAG, "execSQL:" + sql + " ex:" + s.toString());
            return false;
        }
    }

    /**
     * 批量执行sql语句。
     */
    public boolean execBatchSQL(String[] sqls, boolean transaction) {
        makesureWriteable();
        if (transaction) {
            mDb.beginTransaction();
        }

        for (String sql : sqls) {
            try {
                if (sql != null) {
                    mDb.execSQL(sql);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "execSQL:[" + sql + "] ex:" + e);
                if (transaction) {
                    mDb.endTransaction();
                }
                return false;
            }
        }

        if (transaction) {
            mDb.setTransactionSuccessful();
            mDb.endTransaction();
        }

        return true;
    }

    /**
     * 批量执行sql语句。
     */
    public boolean execBatchSQL(String[] sqls, Object[][] objects,  boolean transaction) {
        makesureWriteable();
        if (transaction) {
            mDb.beginTransaction();
        }
        int i=0;
        for (String sql : sqls) {
            try {
                if (sql != null) {
                    mDb.execSQL(sql, objects[i]);
                }
                i++;
            } catch (Exception e) {
                Log.e(TAG, "execSQL:[" + sql + "] ex:" + e);
                if (transaction) {
                    mDb.endTransaction();
                }
                return false;
            }

        }

        if (transaction) {
            mDb.setTransactionSuccessful();
            mDb.endTransaction();
        }

        return true;
    }

    /**
     * 增加新的记录
     * @param table
     * @param values
     * @return
     */
    public long add(String table, ContentValues values) {
        makesureWriteable();
        return mDb.insert(table, null, values);
    }

    public void beginTransaction() {
        makesureWriteable();
        mDb.beginTransaction();
    }

    public void endTransaction() {
        makesureWriteable();
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    /**
     * 异常情况下中断事务
     */
    public void endTransactionByException(){
        makesureWriteable();
        mDb.endTransaction();
    }
}
