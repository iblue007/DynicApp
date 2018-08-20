package com.felink.corelib.widget.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.felink.corelib.R;
import com.felink.corelib.kitset.ScreenUtil;

/**
 * 底部菜单公用类
 * Created by linliangbin on 2017/6/20 11:27.
 */

public class BottomPopwindow extends PopupWindow implements View.OnClickListener {

    private static final int AUTO_CANCLE_INDEX = 1000;
    private String[] bottomMenuString;
    private OnMenuItemClickListener onMenuItemClickListener;
    private boolean addCancleAuto = false;
    private Context context;

    public BottomPopwindow(Context context, String[] menus, OnMenuItemClickListener itemClickListener, boolean addCancleAuto) {
        super(context);
        this.context = context;
        this.bottomMenuString = menus;
        this.onMenuItemClickListener = itemClickListener;
        this.addCancleAuto = addCancleAuto;
        init();
    }

    public BottomPopwindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View container = LayoutInflater.from(context).inflate(R.layout.view_bottom_popwindow_container, null);

        LinearLayout containerLayout = (LinearLayout) container.findViewById(R.id.layout_menu_container);
        for (int i = 0; i < bottomMenuString.length; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_bottom_popwindow_item, null);
            ((TextView) view.findViewById(R.id.tv_bottom_popwindow_menu_title)).setText(bottomMenuString[i]);
            view.setTag(new Integer(i));
            view.setOnClickListener(this);
            containerLayout.addView(view);
        }
        container.setOnClickListener(this);

        if (addCancleAuto) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_bottom_popwindow_cancle_item, null);
            ((TextView) view.findViewById(R.id.tv_bottom_popwindow_menu_title)).setText("取消");
            view.setTag(new Integer(AUTO_CANCLE_INDEX));
            view.setOnClickListener(this);
            containerLayout.addView(view);
        }

        setContentView(container);
        setWidth(ScreenUtil.getCurrentScreenWidth(context));
        setHeight(ScreenUtil.getCurrentScreenHeight(context));
        setBackgroundDrawable(new ColorDrawable(0x66000000));
        setOutsideTouchable(true);
        setFocusable(true);
        setAnimationStyle(R.style.BottomPopwindowAnimation);
        setClippingEnabled(false);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    @Override
    public void onClick(View v) {
        Object tagObj = v.getTag();
        if(tagObj == null || !(tagObj instanceof Integer)){
            this.dismiss();
            return;
        }
        int index = ((Integer)tagObj).intValue();
        if (index == AUTO_CANCLE_INDEX) {
            this.dismiss();
        } else {
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onItemClick(index,bottomMenuString[index]);
            }
            this.dismiss();
        }

    }

    public interface OnMenuItemClickListener {
        public void onItemClick(int itemIndex,String menuString);
    }
}
