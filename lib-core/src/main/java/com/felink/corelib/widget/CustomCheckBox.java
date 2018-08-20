package com.felink.corelib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * 支持只改变状态不触发监听的CheckBox
 * Created by linliangbin on 2018/6/8 11:39.
 */

public class CustomCheckBox extends CheckBox {

    private OnCheckedChangeListener checkedChangeListener;

    public CustomCheckBox(Context context) {
        super(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.checkedChangeListener = listener;
        super.setOnCheckedChangeListener(listener);
    }

    /**
     * @desc 改变设置时不触发监听回调
     * @author linliangbin
     * @time 2018/6/8 11:41
     */
    public void setCheckSilent(boolean isCheck) {
        super.setOnCheckedChangeListener(null);
        setChecked(isCheck);
        super.setOnCheckedChangeListener(this.checkedChangeListener);
    }
}
