package com.felink.corelib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.felink.corelib.R;

/**开启循环视频壁纸的dialog
 * Created by xuqunxing on 2017/8/18.
 */
public class MyDownHdTipDialog extends Dialog {

    private Context mContext;
    private TextView titleTv;
    private TextView tipTv;
    private TextView cancleTv;
    private TextView submitTv;
    private String tipStr,title,  cancleStr,  okStr;
    public MyDownHdTipDialog(Context context) {
        super(context, R.style.Dialog_No_Anim);
        this.mContext =context;
        init();
    }

    public MyDownHdTipDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext =context;
        init();
    }

    public MyDownHdTipDialog(Context context, String title, String tip, String cancleStr, String okStr) {
        super(context, R.style.Dialog_No_Anim);
        this.mContext =context;
        this.title = title;
        this.tipStr = tip;
        this.cancleStr =cancleStr;
        this.okStr = okStr;
        init();
    }


    private void init() {
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hd_down_tip_dialog);

        initViews();
        initValues();
    }

    private void initViews() {
        titleTv = (TextView) findViewById(R.id.dialog_title);
        submitTv = (TextView) findViewById(R.id.dialog_submit);
        submitTv.setOnClickListener(clickListener);
//        if(!TextUtils.isEmpty(title)){
//            titleTv.setText(title);
//        }
//        if(!TextUtils.isEmpty(tipStr)){
//            tipTv.setVisibility(View.VISIBLE);
//        }else {
//            tipTv.setVisibility(View.GONE);
//        }
//
//        if(!TextUtils.isEmpty(cancleStr)){
//            cancleTv.setText(cancleStr);
//            cancleTv.setVisibility(View.VISIBLE);
//        }else {
//            cancleTv.setVisibility(View.GONE);
//        }
//        if(!TextUtils.isEmpty(okStr)){
//            submitTv.setText(okStr);
//            submitTv.setVisibility(View.VISIBLE);
//        }else {
//            submitTv.setVisibility(View.GONE);
//        }
    }

    private void initValues() {
        // 不能写在init()中
//        Window window = getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//        lp.width = dm.widthPixels;//让dialog的宽占满屏幕的宽
//        lp.gravity = Gravity.BOTTOM;//出现在底部
//        window.setAttributes(lp);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.dialog_submit){
                if (onGnClickListener != null) {
                    onGnClickListener.onSubmitClick(v);
                }
                dismiss();
            }else  if(id == R.id.dialog_cancle){
                dismiss();
                if (onGnClickListener != null) {
                    onGnClickListener.onCancleClick(v);
                }
            }
        }
    };

    private OnGnClickListener onGnClickListener;

    public void setOnGnClickListener(OnGnClickListener onGnClickListener) {
        this.onGnClickListener = onGnClickListener;
    }

    public interface OnGnClickListener {
        public void onSubmitClick(View v);
        public void onCancleClick(View v);
    }

}
