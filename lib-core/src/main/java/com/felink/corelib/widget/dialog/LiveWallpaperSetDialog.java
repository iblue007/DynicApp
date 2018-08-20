package com.felink.corelib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.felink.corelib.R;

/**提示设置视频壁纸的dialog
 * Created by xuqunxing on 2017/8/18.
 */
public class LiveWallpaperSetDialog extends Dialog {

    private Context mContext;
    private TextView titleTv;
    private TextView cancleTv;
    private TextView submitTv;

    public LiveWallpaperSetDialog(Context context) {
        super(context, R.style.Dialog_Fullscreen_slow);
        this.mContext =context;
        init();
    }

    public LiveWallpaperSetDialog(Context context, int themeResId) {
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
        setContentView(R.layout.view_live_wallpaper_set_dialog);

        initViews();
        initValues();
    }

    private void initViews() {
        titleTv = (TextView) findViewById(R.id.dialog_title);
        cancleTv = (TextView) findViewById(R.id.dialog_cancle);
        submitTv = (TextView) findViewById(R.id.dialog_submit);
        cancleTv.setOnClickListener(clickListener);
        submitTv.setOnClickListener(clickListener);

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
