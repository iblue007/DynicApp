package com.nd.hilauncherdev.kitset.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/10/16.
 */

public class MessageUtils {
    public static void makeLongToast(Context ctx, int cs) {
        Toast.makeText(ctx, cs, 1).show();
    }
}
