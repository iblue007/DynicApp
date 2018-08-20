package com.felink.corelib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.felink.corelib.R;

/**明星冲榜dialog
 * Created by xuqunxing on 2017/8/18.
 */
public class StarsEffectUpDialog extends Dialog {

    private Context mContext;
    private TextView submitTv;

    public StarsEffectUpDialog(Context context) {
        super(context, R.style.Dialog_Fullscreen);
        this.mContext =context;
        init();
    }

    public StarsEffectUpDialog(Context context, int themeResId) {
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
        setContentView(R.layout.view_help_starts_up_dialog);

        initViews();
        initValues();
    }

    private void initViews() {
        submitTv = (TextView) findViewById(R.id.dialog_submit);
        submitTv.setOnClickListener(clickListener);
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
                dismiss();
            }
        }
    };


}
