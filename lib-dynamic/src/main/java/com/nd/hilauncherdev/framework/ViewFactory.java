package com.nd.hilauncherdev.framework;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.framework.view.dialog.CommonDialog;

import android.content.DialogInterface.OnClickListener;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ViewFactory {
    public static CommonDialog getAlertDialog(Context ctx, CharSequence title, CharSequence message, DialogInterface.OnClickListener ok, boolean isSingleBtn) {
        if (isSingleBtn) {
            CommonDialog.Builder result = new CommonDialog.Builder(ctx);
            result.setTitle(title).setMessage(message).setPositiveButton(R.string.common_button_confirm, ok);
            return result.create();
        } else {
            return getAlertDialog(ctx, title, message, ok, (DialogInterface.OnClickListener) null);
        }
    }

    public static CommonDialog getAlertDialog(Context ctx, CharSequence title, CharSequence message, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialog(ctx, -1, title, message, ok, cancle);
    }

    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialog(ctx, icon, title, message, ctx.getText(R.string.common_button_confirm), ctx.getText(R.string.common_button_cancel), ok, cancle);
    }

    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, CharSequence positive, CharSequence negative, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancle) {
        return getAlertDialog(ctx, icon, title, message, (View) null, positive, negative, ok, cancle);
    }

    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, View view, CharSequence positive, CharSequence negative, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancle) {
        CommonDialog.Builder result = new CommonDialog.Builder(ctx);
        if (icon != -1) {
            result.setIcon(icon);
        }

        result.setTitle(title).setMessage(message).setContentView(view).setPositiveButton(positive, ok);
        if (cancle != null) {
            result.setNegativeButton(negative, cancle);
        } else {
            result.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
        }

        return result.create();
    }

    public static CommonDialog getAlertDialogEx(Context ctx, int icon, CharSequence title, CharSequence message, CharSequence positive, CharSequence negative, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialogEx(ctx, icon, title, message, (View) null, positive, negative, ok, cancle);
    }

    public static CommonDialog getAlertDialogEx(Context ctx, int icon, CharSequence title, CharSequence message, View view, CharSequence positive, CharSequence negative, OnClickListener ok, OnClickListener cancle) {
        CommonDialog.Builder result = new CommonDialog.Builder(ctx);
        if (icon != -1) {
            result.setIcon(icon);
        }

        result.setTitle(title).setMessage(message).setContentView(view).setPositiveButton(positive, ok);
        if (cancle != null) {
            result.setNegativeButton(negative, cancle);
        } else {
            result.setNegativeButton(negative, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
        }

        return result.createEx();
    }

}
