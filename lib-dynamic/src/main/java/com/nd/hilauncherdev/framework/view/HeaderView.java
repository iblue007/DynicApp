package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;


/**
 * 通用的头部
 */
public class HeaderView extends LinearLayout{

	public static final int MENU_LAYOUT_WIDTH = 70;
	public static final int MENU_LAYOUT_RIGHT_PADDING = 15;

	private Context 		mContext;
	private LinearLayout 	mHead;
	private ImageView 		mMenu;
	private LinearLayout 	mMenuLayout , mGoBackLayout;
	private TextView 		mContainerTitle;
	private ImageView       mGoback;

	public HeaderView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	/**
	 * 初始化工作
	 */
	private void init() {
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(getContext(), 52)));
		this.setOrientation(HORIZONTAL);
//		this.setBackgroundResource(R.drawable.v7_common_header_bg);

		mHead = new LinearLayout(mContext);
		mHead.setOrientation(HORIZONTAL);
		mHead.setGravity(Gravity.CENTER_VERTICAL);
		mHead.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(getContext(), 52)));
		mHead.setBackgroundResource(R.drawable.v7_common_header_bg);
		LayoutParams lp = null;

		// 后退
		mGoBackLayout = new LinearLayout(mContext);
		lp = new LayoutParams(ScreenUtil.dip2px(getContext(), MENU_LAYOUT_WIDTH), LayoutParams.MATCH_PARENT);
		mGoBackLayout.setPadding(ScreenUtil.dip2px(getContext(), 15), 0, 0, 0);
		mGoBackLayout.setLayoutParams(lp);
		mGoBackLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//		mGoBackLayout.setBackgroundResource(R.drawable.common_menu_selector);

		mGoback = new ImageView(mContext);
		mGoback.setLayoutParams(new LayoutParams(ScreenUtil.dip2px(mContext, 24), LayoutParams.MATCH_PARENT));
		mGoback.setImageResource(R.drawable.common_back);
		mGoBackLayout.addView(mGoback);
		mHead.addView(mGoBackLayout);

		// 标题
		mContainerTitle = new TextView(mContext);
		mContainerTitle.setSingleLine(true);
		mContainerTitle.setEllipsize(TruncateAt.END);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		mContainerTitle.setLayoutParams(params);
		mContainerTitle.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
		mContainerTitle.setTextSize(17);// R.dimens.myphone_title_text_size
		mContainerTitle.setTextColor(getResources().getColor(R.color.common_header_title_color));
		mHead.addView(mContainerTitle);

		// 菜单
		mMenuLayout = new LinearLayout(mContext);
		lp = new LayoutParams(ScreenUtil.dip2px(getContext(), MENU_LAYOUT_WIDTH), LayoutParams.MATCH_PARENT);
		mMenuLayout.setLayoutParams(lp);
		mMenuLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		mMenuLayout.setPadding(0, 0, ScreenUtil.dip2px(getContext(), MENU_LAYOUT_RIGHT_PADDING), 0);
//		mMenuLayout.setBackgroundResource(R.drawable.common_menu_selector);

		mMenu = new ImageView(mContext);
		mMenu.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		mMenu.setImageResource(R.drawable.common_sort_menu);
		mMenuLayout.setVisibility(View.INVISIBLE);
		mMenuLayout.addView(mMenu);
		mHead.addView(mMenuLayout);

		addView(mHead);
	}

	public View getMenuView(){
		return mMenuLayout;
	}

	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		mContainerTitle.setText(title);
	}

	public String getTitle() {
		return mContainerTitle.getText().toString();
	}
	
	public void setBackgroundRes(int resId) {
		if(null == mHead) return;
		mHead.setBackgroundResource(resId);
	}

	/**
	 * 设置标题文字位置
	 * @param gravity
	 */
	public void setTitleGravity(int gravity){
		mContainerTitle.setGravity(gravity);

		if ((gravity & Gravity.LEFT) != 0) {
			mGoBackLayout.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
			mGoBackLayout.setPadding(ScreenUtil.dip2px(getContext(), 8), 0, ScreenUtil.dip2px(getContext(), 15), 0);
			mMenuLayout.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
	}

	public void setTitleColorResource(int resId){
		mContainerTitle.setTextColor(mContext.getResources().getColor(resId));
	}

	public TextView getTitleView() {
		return mContainerTitle;
	}

	/**
	 * 菜单是否可见
	 * @param visibility
	 */
	public void setMenuVisibility(int visibility) {
		mMenuLayout.setVisibility(visibility);
	}

	/**
	 * 修改菜单图片
	 * @param resid
	 */
	public void setMenuImageResource(int resid){
		mMenu.setImageResource(resid);
	}

	/**
	 * 返回按钮点击事件
	 * @param listener
	 */
	public void setGoBackListener(OnClickListener listener) {
		mGoBackLayout.setOnClickListener(listener);
	}

	/**
	 * 设置点击事件
	 * @param listener
	 */
	public void setMenuListener(OnClickListener listener) {
		mMenuLayout.setOnClickListener(listener);
	}

	/**
	 * 返回按钮是否可见
	 * @param visibility
	 */
	public void setGoBackVisibility(int visibility) {
		mGoBackLayout.setVisibility(visibility);
	}

	public void setGoBackResource(int resid){
		mGoback.setImageResource(resid);
	}

	public void replaceMenu(View view) {
		if (view == null) {
			return;
		}
		mMenuLayout.removeAllViews();
		mMenuLayout.setVisibility(View.VISIBLE);
		mMenuLayout.setPadding(0, 0, 0, 0);
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp != null && lp.width > ScreenUtil.dip2px(getContext(), MENU_LAYOUT_WIDTH - MENU_LAYOUT_RIGHT_PADDING)) {
			mGoBackLayout.getLayoutParams().width = lp.width + ScreenUtil.dip2px(getContext(), MENU_LAYOUT_RIGHT_PADDING);
			mMenuLayout.getLayoutParams().width = lp.width + ScreenUtil.dip2px(getContext(), MENU_LAYOUT_RIGHT_PADDING);
		}
		mMenuLayout.addView(view);
	}

    public void replaceMenu(View view, boolean adjustMenuSize) {
        if(adjustMenuSize) {
            ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            mMenuLayout.setLayoutParams(lp);
            if (view == null) {
                return;
            }
            mMenuLayout.removeAllViews();
            mMenuLayout.setVisibility(View.VISIBLE);
            mMenuLayout.setPadding(0, 0, ScreenUtil.dip2px(getContext(), MENU_LAYOUT_RIGHT_PADDING), 0);
            lp = view.getLayoutParams();
            if (lp != null && lp.width > ScreenUtil.dip2px(getContext(), MENU_LAYOUT_WIDTH - MENU_LAYOUT_RIGHT_PADDING)) {
                mGoBackLayout.getLayoutParams().width = lp.width + ScreenUtil.dip2px(getContext(), MENU_LAYOUT_RIGHT_PADDING);
                mMenuLayout.getLayoutParams().width = lp.width + ScreenUtil.dip2px(getContext(), MENU_LAYOUT_RIGHT_PADDING);
            }
            mMenuLayout.addView(view);

        }else {
            replaceMenu(view);
        }

    }

	/**
	 * 将dip转换为px
	 * @param cxt
	 * @param dipValue
	 * @return int
	 */
	public static int dip2px(Context cxt, float dipValue) {
		final float scale = cxt.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}


	/**
	 * 简单的popupwindow
	 * @param context
	 * @param backgroundId 背景资源
	 * @param texts
	 * @param onclicks 事件回调
	 * @return popupwindow
	 */
//	public static PopupWindow getCommonPopupWidow(Context context, int backgroundId, String[] texts, OnClickListener[] onclicks) {
//		if (texts == null) {
//			return null;
//		}
//		LinearLayout layout = new LinearLayout(context);
//		layout.setOrientation(LinearLayout.VERTICAL);
//		if (backgroundId <= 0) {
//			backgroundId = R.drawable.common_menu_bg;
//		}
//		layout.setBackgroundResource(backgroundId);
//		int maxWidth = ScreenUtil.dip2px(context, 100);
//		for (int i = 0; i < texts.length; i++) {
//			String text = texts[i];
//			TextView textView = new TextView(context);
//			textView.setTextColor(Color.BLACK);
//			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//			textView.setText(text);
//			textView.setGravity(Gravity.CENTER);
//			textView.setBackgroundResource(R.drawable.myphone_click_item_blue);
//			int padding = ScreenUtil.dip2px(context, 10);
//			textView.setPadding(padding, padding, padding, padding);
//			layout.addView(textView);
//			if (i != texts.length - 1) {
//				ImageView imageView = new ImageView(context);
//				imageView.setBackgroundResource(R.drawable.common_menu_seperator_line);
//				imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//				layout.addView(imageView);
//			}
//
//			int width = (int) textView.getPaint().measureText(text);
//			if (maxWidth < width) {
//				maxWidth = width;
//			}
//			if (onclicks != null && onclicks.length >= i + 1) {
//				textView.setOnClickListener(onclicks[i]);
//			}
//		}
//		PopupWindow popup = new PopupWindow(layout, (int) (maxWidth * 1.5f), LayoutParams.WRAP_CONTENT);
//		popup.setBackgroundDrawable(new BitmapD);
//		popup.setOutsideTouchable(true);
//		return popup;
//	}

}
