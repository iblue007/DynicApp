package com.felink.corelib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felink.corelib.R;
import com.felink.corelib.kitset.ScreenUtil;


/**
 * 天天提示弹框
 * Created by linliangbin on 16-6-29.
 */
public class SeriesTipDialog extends Dialog implements View.OnClickListener, DialogInterface.OnKeyListener {

    ImageView previewImg, iconImage, closeImage;
    TextView titleText, descText;
    Handler handler = new Handler();
    private Context context;
    private View recommend_dlg_view = null;
    private RelativeLayout recommend_dlg_layout = null;

    public SeriesTipDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SeriesTipDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public static void showSeriasTopDialog(Context context) {

        try {
            SeriesTipDialog dialog = new SeriesTipDialog(context, R.style.Dialog);
            dialog.init();
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void init() {
        initView();
        setCanceledOnTouchOutside(true);
        setContentView(recommend_dlg_layout);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) (ScreenUtil.getCurrentScreenWidth(context) * 0.8), ScreenUtil.getCurrentScreenHeight(context));
//        recommend_dlg_layout.setLayoutParams(lp);
    }

    private void initView() {
        recommend_dlg_layout = new RelativeLayout(context);
        recommend_dlg_layout.setBackgroundResource(R.color.transparent);

        recommend_dlg_view = LayoutInflater.from(context).inflate(R.layout.dialog_series_tip, null);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        recommend_dlg_layout.addView(recommend_dlg_view, rlp);

        previewImg = (ImageView) recommend_dlg_layout.findViewById(R.id.banner_img);
//        ViewGroup.LayoutParams layoutParams = previewImg.getLayoutParams();
//        layoutParams.height = layoutParams.width * (564 / 516);
//        previewImg.setLayoutParams(layoutParams);

        titleText = (TextView) recommend_dlg_layout.findViewById(R.id.title);
        descText = (TextView) recommend_dlg_layout.findViewById(R.id.desc);
        closeImage = (ImageView) recommend_dlg_layout.findViewById(R.id.iv_close);
        closeImage.setOnClickListener(this);
        recommend_dlg_layout.findViewById(R.id.recommend_download_btn).setOnClickListener(this);
        this.setOnKeyListener(this);

        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return false;
    }
}
