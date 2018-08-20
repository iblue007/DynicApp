package com.felink.corelib.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.felink.corelib.R;

public class CustomProgressDialog extends ProgressDialog {
    private RotateImageView imageView = null;
    CharSequence mText=null;
    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.customprogressdialog);
        imageView = (RotateImageView) findViewById(R.id.loadingImageView);
//        imageView.setBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.loding_point));
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.5f;
        lp.flags = lp.flags & WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(lp);
        TextView tvMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null && mText != null) {
            tvMsg.setText(mText);
        }
        
    }
    
    public void onWindowFocusChanged(boolean hasFocus) {
        if (imageView != null) {
            imageView.startAni();
        }
    }
    
    @Override
    public void setMessage(CharSequence strMessage) {
        mText=strMessage;
        TextView tvMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null && mText != null) {
            tvMsg.setText(mText);
        }
        return;
    }
    
    @Override
    public void setTitle(CharSequence title) {
    }
    
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (imageView != null) {
            imageView.stopAni();
        }
    }
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    
    @Override
    public void setIndeterminate(boolean indeterminate) {
        super.setIndeterminate(indeterminate);
    }
}

