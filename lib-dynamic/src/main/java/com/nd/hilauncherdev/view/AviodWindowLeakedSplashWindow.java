/**
 * Version 1.0
 *
 * =============================================================
 * Revision History
 * 
 * Modification                    Tracking
 * Date           Author           Number      Description of changes
 * ----------     --------------   ---------   -------------------------
 * 2009-9-23         yangbin            代码重构
 */
package com.nd.hilauncherdev.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.felink.corelib.widget.RotateImageView;
import com.nd.hilauncherdev.dynamic.R;


/**
 * 避免出现 WindowLeaked异常
 * <br>Author:ryan
 * <br>Date:2011-11-6下午08:36:09
 */
public class AviodWindowLeakedSplashWindow extends ProgressDialog {
    /**
     * listener
     */
    private View.OnClickListener preLis;
    private View.OnClickListener postLis;
    CharSequence mText=null;

    /**
     * actionthread
     */
    private ActionThread actionthread = null;

    public long id;

    
   /* public long getId() {
		return id;
	}*/

	/*public void setId(long id) {
		this.id = id;
	}*/
    private Context context = null;
    private RotateImageView imageView = null;
    
	public AviodWindowLeakedSplashWindow(Context ctx, String msg, View.OnClickListener preLis, View.OnClickListener postLis, boolean isCacelable) {
		super(ctx);
        this.setCancelable(isCacelable);
        this.preLis = preLis;
        this.postLis = postLis;
        this.setMessage(msg);
        initSplashWindow();
	}

	public AviodWindowLeakedSplashWindow(Context ctx, String title, String msg, View.OnClickListener preLis, View.OnClickListener postLis, boolean isCacelable) {
		super(ctx);
        this.setCancelable(isCacelable);
        this.preLis = preLis;
        this.postLis = postLis;
        this.setMessage(msg);
        initSplashWindow();
	}
    
    public AviodWindowLeakedSplashWindow(Context ctx, String title, String msg, View.OnClickListener preLis, View.OnClickListener postLis) {
        super(ctx);
        this.setCancelable(false);
        this.preLis = preLis;
        this.postLis = postLis;
        if (title != null) {
            setTitle(title);
        }
        this.setMessage(msg);
        initSplashWindow();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window win = getWindow();
        setContentView(R.layout.customprogressdialog);
        imageView = (RotateImageView) findViewById(R.id.loadingImageView);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.dimAmount=0.5f;
        lp.flags=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        win.setAttributes(lp);
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
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return;
    }
    @Override
    public void setTitle(CharSequence title) {
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
        if (imageView != null) {
            imageView.stopAni();
        }
    }
    /**
     * show出等待框并启动事件线程
     */
    private void initSplashWindow() {
    	if(!isShowing()) {
    		this.show();// 出现等待框
    	}
        actionthread = new ActionThread(preLis, postLis);
        actionthread.start();//启动事件线程
    }

    /**
     * 
     */
    private void closeWindow() {
        this.dismiss();
    }

    /**
     * 
     *执行事件的线程
     */
    private class ActionThread extends Thread {
        private View.OnClickListener preAction = null;
        private View.OnClickListener postAction = null;

        public ActionThread(View.OnClickListener preAction, View.OnClickListener postAction) {
        	this.preAction = preAction;
        	this.postAction = postAction;
        }

        @Override
		public void run() {
            try {
            	if (preAction != null)
            		preAction.onClick(null);
            	
                closeWindow();
                
                if (postAction != null)
                	postAction.onClick(null);
            } catch (Throwable ex) {
                ex.printStackTrace();
                closeWindow();
            } 
        }
    }
}
