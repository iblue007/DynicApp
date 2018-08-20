package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.nd.hilauncherdev.dynamic.R;


public class PercentBarView extends View {

	private Drawable bg;
	private Drawable d;
	private int progress;
	private int end;
	private boolean needAnim = false;
	private int factor = 1; // 递进因子

	public PercentBarView(Context context) {
		super(context);
		init();
	}

	private void init() {
		bg = getResources().getDrawable(R.drawable.battery_batterybar_empty);
		d = getResources().getDrawable(R.drawable.battery_batterybar_full);
	}

	public PercentBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PercentBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		bg.setBounds(0, 0, getRight() * 98 / 100, getHeight());
		bg.draw(canvas);
		int right = (int) (getRight() * progress / 100);
		d.setBounds(0, 0, right * 98 / 100, getHeight());
		d.draw(canvas);

		if (progress < end && needAnim) {
			progress += factor;
			invalidate();
		}
	}

	public void setBackground(int resid) {
		bg = getResources().getDrawable(resid);
	}

	public void setForeground(int resid) {
		d = getResources().getDrawable(resid);
	}

	public void setProgressiveFactor(int factor) {
		this.factor = factor;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
		this.needAnim = false;
		invalidate();
	}

	public void setProgress(int progress, boolean needAnim) {
		this.progress = 0;
		this.end = progress;
		this.needAnim = needAnim;
		invalidate();
	}
}
