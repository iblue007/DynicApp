//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.felink.corelib.webview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.felink.corelib.widget.CommonDialog.Builder;
import com.felink.corelib.R;
import com.felink.corelib.kitset.ScreenUtil;
import com.felink.corelib.kitset.SystemUtil;
import com.felink.corelib.widget.CommonDialog;

public class ViewFactory {
    public static final int NOMAL_ERR_VIEW = 0;
    public static final int LOADING_DATA_INFO_VIEW = 1;
    public static final int SEARCH_NO_DATA_VIEW = 2;
    public static final int NET_SLOWLY_VIEW = 3;
    public static final int NET_BREAK_VIEW = 4;
    public static final int DOWNLOAD_NO_LOG_VIEW = 5;

    public ViewFactory() {
    }

//    public static View getErrorView(int hintId, Context ctx) {
//        View result = LayoutInflater.from(ctx).inflate(R.layout.framework_viewfactory_data_error, (ViewGroup)null);
//        TextView tv = (TextView)result.findViewById(R.id.hint_error);
//        tv.setText(hintId);
//        return result;
//    }
//
//    public static CommonDialog getAlertDialog(Context ctx, CharSequence title, CharSequence message, OnClickListener ok, boolean isSingleBtn) {
//        if(isSingleBtn) {
//            Builder result = new Builder(ctx);
//            result.setTitle(title).setMessage(message).setPositiveButton(R.string.common_button_confirm, ok);
//            return result.create();
//        } else {
//            return getAlertDialog(ctx, title, message, ok, (OnClickListener)null);
//        }
//    }
//
//    public static CommonDialog getAlertDialog(Context ctx, CharSequence title, CharSequence message, OnClickListener ok) {
//        return getAlertDialog(ctx, title, message, ok, (OnClickListener)null);
//    }

    public static CommonDialog getAlertDialog(Context ctx, CharSequence title, CharSequence message, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialog(ctx, -1, title, message, ok, cancle);
    }

    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialog(ctx, icon, title, message, ctx.getText(R.string.common_button_confirm), ctx.getText(R.string.common_button_cancel), ok, cancle);
    }

    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, CharSequence positive, CharSequence negative, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialog(ctx, icon, title, message, (View)null, positive, negative, ok, cancle);
    }

    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, View view, CharSequence positive, CharSequence negative, OnClickListener ok, OnClickListener cancle) {
        Builder result = new Builder(ctx);
        if(icon != -1) {
            result.setIcon(icon);
        }

        result.setTitle(title).setMessage(message).setContentView(view).setPositiveButton(positive, ok);
        if(cancle != null) {
            result.setNegativeButton(negative, cancle);
        } else {
            result.setNegativeButton(negative, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
        }

        return result.create();
    }

    public static CommonDialog getAlertDialogWithSingleBtn(Context ctx, int icon, CharSequence title, CharSequence message, View view, CharSequence positive, OnClickListener ok) {
        Builder result = new Builder(ctx);
        if(icon != -1) {
            result.setIcon(icon);
        }

        result.setTitle(title).setMessage(message).setContentView(view).setPositiveButton(positive, ok);
        return result.create();
    }

    public static CommonDialog getAlertDialogEx(Context ctx, CharSequence title, CharSequence message, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialogEx(ctx, -1, title, message, ok, cancle);
    }

    public static CommonDialog getAlertDialogEx(Context ctx, int icon, CharSequence title, CharSequence message, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialogEx(ctx, icon, title, message, ctx.getText(R.string.common_button_confirm), ctx.getText(R.string.common_button_cancel), ok, cancle);
    }

    public static CommonDialog getAlertDialogEx(Context ctx, int icon, CharSequence title, CharSequence message, CharSequence positive, CharSequence negative, OnClickListener ok, OnClickListener cancle) {
        return getAlertDialogEx(ctx, icon, title, message, (View)null, positive, negative, ok, cancle);
    }

    public static CommonDialog getAlertDialogEx(Context ctx, int icon, CharSequence title, CharSequence message, View view, CharSequence positive, CharSequence negative, OnClickListener ok, OnClickListener cancle) {
        Builder result = new Builder(ctx);
        if(icon != -1) {
            result.setIcon(icon);
        }

        result.setTitle(title).setMessage(message).setContentView(view).setPositiveButton(positive, ok);
        if(cancle != null) {
            result.setNegativeButton(negative, cancle);
        } else {
            result.setNegativeButton(negative, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
        }

        return result.createEx();
    }

//    public static CommonDialog getAlertDialogEx(Context ctx, int icon, boolean showTitle, boolean showMessage, CharSequence title, CharSequence message, View view, CharSequence positive, CharSequence negative, OnClickListener ok, OnClickListener cancle) {
//        Builder result = new Builder(ctx);
//        if(icon != -1) {
//            result.setIcon(icon);
//        }
//
//        result.setTitle(title).setMessage(message).setContentView(view).setPositiveButton(positive, ok);
//        if(cancle != null) {
//            result.setNegativeButton(negative, cancle);
//        } else {
//            result.setNegativeButton(negative, new OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//                }
//            });
//        }
//
//        result.setShowTitle(showTitle);
//        result.setShowMessage(showMessage);
//        return result.createEx();
//    }

//    public static View getNoDataInfoView(Context context, View parentView, int titleId, int contentId) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.framework_viewfactory_info_view, (ViewGroup)null);
//        TextView titleView = (TextView)view.findViewById(R.id.framework_viewfactory_no_data_title);
//        TextView textView = (TextView)view.findViewById(R.id.framework_viewfactory_no_data_textview);
//        if(titleId <= 0) {
//            titleId = R.string.common_tip;
//        }
//
//        titleView.setText(titleId);
//        textView.setText(contentId);
//        if(parentView != null && parentView instanceof RelativeLayout) {
//            RelativeLayout parent = (RelativeLayout)parentView;
//            parent.addView(view);
//            LayoutParams l = new LayoutParams(view.getLayoutParams());
//            l.topMargin = context.getResources().getDimensionPixelSize(R.dimen.myphone_info_view_margin_top);
//            l.rightMargin = ScreenUtil.dip2px(context, 15.0F);
//            l.leftMargin = ScreenUtil.dip2px(context, 15.0F);
//            l.addRule(10, -1);
//            l.addRule(14, -1);
//            view.setLayoutParams(l);
//        }
//
//        return view;
//    }

    public static View getNomalErrInfoView(final Context context, View parentView, int flag) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.framework_viewfactory_err_info_view, (ViewGroup)null);
        ImageView imageView = (ImageView)view.findViewById(R.id.framework_viewfactory_err_img);
        WarningInfoTextView textView = (WarningInfoTextView)view.findViewById(R.id.framework_viewfactory_err_textview);
        switch(flag) {
            case 0:
                imageView.setBackgroundResource(R.drawable.frame_viewfacotry_net_break_img);
                textView.setText(R.string.frame_viewfacotry_err_info_text);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.framwork_viewfactory_load_data_img);
                textView.startProcess(context.getResources().getText(R.string.frame_viewfacotry_data_load_text).toString());
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.frame_viewfacotry_search_null_img);
                textView.setText(R.string.frame_viewfacotry_search_null);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.frame_viewfacotry_net_break_img);
                textView.setText(R.string.frame_viewfacotry_net_slowly_text);
                Button parent = (Button)view.findViewById(R.id.framework_viewfactory_err_btn);
                parent.setVisibility(View.VISIBLE);
                parent.setText(R.string.frame_viewfacotry_net_slowly_reflesh_btn);
                view.setTag(parent);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.frame_viewfacotry_net_break_img);
                textView.setText(R.string.frame_viewfacotry_net_break_text);
                Button l = (Button)view.findViewById(R.id.framework_viewfactory_err_btn);
                l.setVisibility(View.VISIBLE);
                l.setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(View paramView) {
                        try {
                            Intent e = new Intent("android.settings.WIRELESS_SETTINGS");
                            e.setFlags(268435456);
                            SystemUtil.startActivity(context, e);
                        } catch (Exception var3) {
                            Toast.makeText(context, R.string.frame_viewfacotry_show_netsetting_err, View.VISIBLE).show();
                        }

                    }
                });
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.frame_viewfacotry_search_null_img);
                textView.setText(R.string.frame_viewfacotry_download_null);
        }

        if(parentView != null) {
            if(parentView instanceof RelativeLayout) {
                RelativeLayout parent1 = (RelativeLayout)parentView;
                parent1.addView(view);
                LayoutParams l1 = new LayoutParams(view.getLayoutParams());
                l1.addRule(13, -1);
                view.setLayoutParams(l1);
            } else if(parentView instanceof LinearLayout) {
                LinearLayout parent2 = (LinearLayout)parentView;
                parent2.addView(view);
                android.widget.LinearLayout.LayoutParams l2 = new android.widget.LinearLayout.LayoutParams(view.getLayoutParams());
                l2.topMargin = ScreenUtil.dip2px(context, 50.0F);
                view.setLayoutParams(l2);
            }
        }

        return view;
    }
}
