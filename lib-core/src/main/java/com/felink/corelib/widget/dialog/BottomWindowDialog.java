package com.felink.corelib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felink.corelib.R;

/**个人中心和壁纸详情的dialog
 * Created by xuqunxing on 2017/8/18.
 */
public class BottomWindowDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView submitTv;
    private static final int AUTO_CANCLE_INDEX = 1000;
    private String[] bottomMenuString;
    private OnMenuItemClickListener onMenuItemClickListener;
    private boolean addCancleAuto = false;

    public BottomWindowDialog(Context context,String[] menus, OnMenuItemClickListener itemClickListener, boolean addCancleAuto) {
        this(context, R.style.Dialog_Fullscreen);
        this.mContext =context;
        this.bottomMenuString = menus;
        this.onMenuItemClickListener = itemClickListener;
        this.addCancleAuto = addCancleAuto;
        init();
    }

//    public BottomWindowDialog(Context context,String[] menus,boolean addCancleAuto, OnMenuItemClickListener itemClickListener) {
//        this(context, R.style.Dialog_Fullscreen);
//        this.mContext =context;
//        this.bottomMenuString = menus;
//        this.onMenuItemClickListener = itemClickListener;
//        this.addCancleAuto = addCancleAuto;
//        init();
//    }

    public BottomWindowDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext =context;
        init();
    }


    private void init() {
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.view_bottom_popwindow_container);
        initViews();
        initValues();
    }

    private void initViews() {
        View container = LayoutInflater.from(mContext).inflate(R.layout.view_bottom_popwindow_container, null);

        LinearLayout containerLayout = (LinearLayout) container.findViewById(R.id.layout_menu_container);
        for (int i = 0; i < bottomMenuString.length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_bottom_popwindow_item, null);
            ((TextView) view.findViewById(R.id.tv_bottom_popwindow_menu_title)).setText(bottomMenuString[i]);
            view.setTag(new Integer(i));
            view.setOnClickListener(this);
            containerLayout.addView(view);
        }
        container.setOnClickListener(this);

        if (addCancleAuto) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_bottom_popwindow_cancle_item, null);
            ((TextView) view.findViewById(R.id.tv_bottom_popwindow_menu_title)).setText("取消");
            view.setTag(new Integer(AUTO_CANCLE_INDEX));
            view.setOnClickListener(this);
            containerLayout.addView(view);
        }
        setContentView(container);
    }

    private void initValues() {
        // 不能写在init()中
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        lp.width = dm.widthPixels;//让dialog的宽占满屏幕的宽
        lp.gravity = Gravity.BOTTOM;//出现在底部
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        Object tagObj = view.getTag();
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
