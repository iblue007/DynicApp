package com.felink.corelib.widget.popwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.felink.corelib.R;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.MessageUtils;
import com.felink.corelib.kitset.OpUtil;
import com.felink.corelib.kitset.ScreenUtil;

/**
 * 通知栏顶部消息使用的popwindow
 * Created by linliangbin on 2017/6/23 14:22.
 */

public class TitlebarPopwindow {

    private Context context;
    private String bgColor;
    private String textColor;
    private String message;

    private WindowManager wm;

    private View container;

    public TitlebarPopwindow(Context context, String message) {
        this.context = context;
        this.message = message;
        init();
    }

    public TitlebarPopwindow(Context context, String message, String bgColor, String textColor) {
        this.context = context;
        this.message = message;
        this.bgColor = bgColor;
        this.textColor = textColor;
        init();
    }

    public static boolean show(View parent, String message) {
        if (parent != null) {

            if(OpUtil.checkFloatWindowPermission(parent.getContext())){
                new TitlebarPopwindow(parent.getContext(), message);
                return false;
            }else{
               // Toast.makeText(parent.getContext(),message,Toast.LENGTH_SHORT).show();
                MessageUtils.showOnlyToast(parent.getContext(), message);
                return true;
            }
        }
        return true;
    }

    public static boolean show(View parent, int message,String showStarsName,int ContribPower) {
        if (parent != null) {

            if(OpUtil.checkFloatWindowPermission(parent.getContext())){
                new TitlebarPopwindow(parent.getContext(), parent.getContext().getString(message));
                return false;
            }else{
                MessageUtils.showOnlyToast(parent.getContext(),parent.getContext().getString(message, showStarsName,ContribPower+""));
                return true;
            }
        }
        return true;
    }

    public static void show(View parent, String message, String bgColor, String textColor) {
        if (parent != null) {
            if(OpUtil.checkFloatWindowPermission(parent.getContext())){
                new TitlebarPopwindow(parent.getContext(), message, bgColor, textColor);
            }else{
                Toast.makeText(parent.getContext(),message,Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void init() {

        initView();
        show();
        postRemove();

    }

    private void initView() {
        container = LayoutInflater.from(context).inflate(R.layout.view_top_popwindow_view, null);
        if (!TextUtils.isEmpty(bgColor)) {
            container.setBackgroundColor(Color.parseColor(bgColor));
        } else {
            container.setBackgroundColor(Color.parseColor("#000000"));
        }
        if (!TextUtils.isEmpty(textColor)) {
            ((TextView) container.findViewById(R.id.tv_msg)).setTextColor(Color.parseColor(textColor));
        }

        ((TextView) container.findViewById(R.id.tv_msg)).setText(message);
    }

    private void show() {

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams1 = new WindowManager.LayoutParams();
        wmParams1.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        int height = ScreenUtil.dip2px(context, 25);
        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 19) {//有透明通知栏
            wmParams1.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 0x01000000 | 0x04000000;
        } else {
            wmParams1.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 0x01000000;
        }
        wmParams1.gravity = Gravity.TOP; // 调整悬浮窗口右上角
        wmParams1.x = 0;//距离边距50dp
        wmParams1.y = 0;
        wmParams1.windowAnimations = R.style.TopPopwindowAnimation;
        // 设置悬浮窗口长宽数据
        wmParams1.width = ScreenUtil.getCurrentScreenWidth(context);
        wmParams1.height = height;
        wmParams1.format = PixelFormat.RGBA_8888;
        wm.addView(container, wmParams1);

    }

    private void postRemove() {

        Global.runInMainThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (wm != null && container != null) {
                        wm.removeView(container);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, 2000);

    }

}
