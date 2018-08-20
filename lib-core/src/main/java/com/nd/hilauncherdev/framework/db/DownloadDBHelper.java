package com.nd.hilauncherdev.framework.db;

import android.content.Context;
import android.database.Cursor;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年05月19日 22:29.</br>
 * @update: </br>
 */

public class DownloadDBHelper {

    /**
     * 获取新的下载完成数
     *
     * @param context
     * @param rowFlag 上次统计的行号
     * @return
     */
    public static int[] queryNewerCount(Context context, int rowFlag) {
        BaseDataBase db = BaseDataBase.getInstance(context);
        String sql = "select count(*),rowid from log_download where rowid > ? and progress = 100 and file_type in (18,19) order by rowid asc";
        Cursor cursor = null;
        int[] newCount = new int[]{0, 0};
        try {
            cursor = db.query(sql, new String[]{rowFlag + ""});
            if (cursor != null) {
                cursor.moveToFirst();
                newCount[0] = cursor.getInt(0);
                newCount[1] = cursor.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return newCount;
    }

    public static boolean deleteTask(Context context, String identifier) {
        try {
            BaseDataBase db = BaseDataBase.getInstance(context);
//        String sql = "delete from log_download where _id = ?";
            return db.delete("log_download", "_id = ?", new String[]{identifier});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
