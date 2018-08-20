package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.kitset.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的手机统一布局(无TAB)
 */
public class MyphoneContainer extends LinearLayout {
	private final static int GREEN = 0 , RED = 1 , BLUE = 3;
	public static final int DEFALUT_THEME = 0, WHITE_THEME = 1;

	// MARGIN_LEFT参见R.dimen.myphone_margin_left,TEXT_PADDING参见dimen/text_drawpadding
	private final static int TEXT_PADDING = 3;
	// 底部高度
	private final static int BOTTOM_H = 60;// @dimen/myphone_bottom_content
	private Context mContext;

	/*******************************************
	 * 头部Layout变量
	 *******************************************/
	// 头部Layout
	private HeaderView mHeader;

	private ImageView selectIv;
	/*******************************************
	 * end 头部变量
	 *******************************************/

	/*******************************************
	 * 内容Layout变量
	 *******************************************/
	// 内容Layout
	private LinearLayout mContainer, mEidterBottom, mBottom;

	private boolean isEditer;
	/*******************************************
	 * end 内容Layout变量
	 *******************************************/

	/**
	 * 每一个底部操作项中外层都多包了一个RelativeLayout 因为一般会在底部操作项中加入一点标志 如:我的铃声下载管理中有下载个数标志,
	 * 新功能标志等,所以bottoms保存底部操作项
	 */
	private List<RelativeLayout> bottoms;

	/**
	 * 底部颜色
	 */
//	private int bottomColor = Color.WHITE;

	public MyphoneContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public MyphoneContainer(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	/**
	 * 初始化工作
	 */
	private void init() {
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setOrientation(VERTICAL);
//		this.setBackgroundResource(R.drawable.myphone_bg_color);
//		this.setBackgroundColor(R.color.myphone_common_bg_color);
	}

	/**
	 * 初始化头部
	 */
	private void initHead() {
		// 头部Layout
		mHeader = new HeaderView(mContext);
		addView(mHeader);
	}

	/**
	 * 初始化Container
	 * @param title
	 * @param view
	 * @param theme
	 */
	public void initContainer(String title, View view, int theme) {
		initHead();
		if (title != null) {
			setTitle(title);
		}
		RelativeLayout layout = new RelativeLayout(mContext);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
		layout.setBackgroundResource(R.drawable.myphone_bg_color);
		this.addView(layout);

		// 内容Layout
		mContainer = new LinearLayout(mContext);
		mContainer.setOrientation(VERTICAL);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mContainer.setLayoutParams(params);
		layout.addView(mContainer);

		mContainer.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
	}

	/**
	 * 添加底部操作栏(可以没有底部操作栏)
	 * @param texts
	 * @param srcs (小图标)不能为空,如果不用小图标,设置为<=0 new int[]{-1,-1,-1}
	 * @param listeners 可以为空
	 */
	public void initBottom(String[] texts, int[] srcs, OnClickListener[] listeners) {
		if (texts == null) {
			return;
		}
		if (bottoms == null) {
			bottoms = new ArrayList<RelativeLayout>();
		}
		// 自定义底部
		mBottom = new LinearLayout(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, BOTTOM_H));
		mBottom.setGravity(Gravity.CENTER);
		mBottom.setOrientation(HORIZONTAL);
		mBottom.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, BOTTOM_H)));
		mBottom.setBackgroundResource(R.drawable.common_btn_layout_bg);
		mBottom.setPadding(dip2px(mContext, 6), dip2px(mContext, 6), 0 ,dip2px(mContext, 6));
		mBottom.setLayoutParams(params);

		mContainer.addView(mBottom);
		int res = -1;
		// 参见@color/myphone_bottom_black_bg_text_color
//		bottomColor = Color.parseColor("#d3d3d3");
		// 添加底部操作
		for (int i = 0; i < texts.length; i++) {
			if (srcs == null) {
				res = -1;
			} else {
				res = srcs[i];
			}
			if (listeners == null || listeners[i] == null) {
				mBottom.addView(createBottom(texts[i], res, null, BLUE));
			} else {
				mBottom.addView(createBottom(texts[i], res, listeners[i], BLUE));
			}// end if-else
		}// end for
	}



    /**
     * 添加底部操作栏(可以没有底部操作栏)
     * @param texts
     * @param drawables (小图标)不能为空,如果不用小图标,设置为<=0 new int[]{-1,-1,-1}
     * @param listeners 可以为空
     */
    public void initBottom(String[] texts, Drawable[] drawables, OnClickListener[] listeners, int height_dp) {
        if (texts == null) {
            return;
        }
        if (bottoms == null) {
            bottoms = new ArrayList<RelativeLayout>();
        }
        // 自定义底部
        mBottom = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, height_dp));
        mBottom.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        mBottom.setOrientation(HORIZONTAL);
        mBottom.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, height_dp)));
        mBottom.setBackgroundResource(R.drawable.common_btn_layout_bg);
        mBottom.setPadding(dip2px(mContext, 6), dip2px(mContext, 6), 0 ,dip2px(mContext, 6));
        mBottom.setLayoutParams(params);
        mContainer.addView(mBottom);
        int i=0;
        for (OnClickListener  listener: listeners) {
            if (listener == null ) {
                mBottom.addView(createBottom(texts[i], drawables[i], null));
            } else {
                mBottom.addView(createBottom(texts[i], drawables[i], listener));
            }
            i++;
        }
    }

	/**
	 * 添加底部操作栏(可以没有底部操作栏)
	 * @param texts
	 * @param srcs (小图标)
	 * @param listeners 可以为空
	 */
	public void initEditerBottom(String[] texts, int[] srcs, OnClickListener[] listeners) {
		if (texts == null) {
			return;
		}
		if (bottoms == null) {
			bottoms = new ArrayList<RelativeLayout>();
		}
		// 自定义底部
		mEidterBottom = new LinearLayout(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, BOTTOM_H));
		mEidterBottom.setBackgroundResource(R.drawable.common_btn_layout_bg);
		mEidterBottom.setPadding(dip2px(mContext, 6), dip2px(mContext, 6), 0 ,dip2px(mContext, 6));
		mEidterBottom.setLayoutParams(params);
		mEidterBottom.setOrientation(HORIZONTAL);
		mEidterBottom.setGravity(Gravity.CENTER);
		mContainer.addView(mEidterBottom);
		mEidterBottom.setVisibility(GONE);
		int res = -1;
		// 参见@color/myphone_bottom_blue_bg_text_color
//		bottomColor = Color.parseColor("#cdf2ff");
		// 添加底部操作
		for (int i = 0; i < texts.length; i++) {
			if (srcs == null) {
				res = -1;
			} else {
				res = srcs[i];
			}
			if (listeners == null) {
				mEidterBottom.addView(createBottom(texts[i], res, null, BLUE));
			} else {
				mEidterBottom.addView(createBottom(texts[i], res, listeners[i], BLUE));
			}// end if-else
		}// end for
	}


	/**
	 * 初始化底部
	 * @param texts
	 * @param srcs
	 * @param btnListeners
	 * @param selectListener
	 */
	public void initBottomWithSelect(String[] texts, int[] srcs, OnClickListener[] btnListeners,OnClickListener selectListener) {
		if (texts == null) {
			return;
		}
		if (bottoms == null) {
			bottoms = new ArrayList<RelativeLayout>();
		}
		// 自定义底部
		mEidterBottom = new LinearLayout(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(mContext, BOTTOM_H));
		mEidterBottom.setBackgroundResource(R.drawable.common_btn_layout_bg);
		mEidterBottom.setPadding(dip2px(mContext, 6), dip2px(mContext, 6), 0 ,dip2px(mContext, 6));
		mEidterBottom.setLayoutParams(params);
		mEidterBottom.setOrientation(HORIZONTAL);
		mEidterBottom.setGravity(Gravity.CENTER);
		mContainer.addView(mEidterBottom);
		mEidterBottom.setVisibility(GONE);

		// 自定义底部
		int res = -1;
		// 添加底部操作
		RelativeLayout layout = new RelativeLayout(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(lp);
		mEidterBottom.addView(layout);

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams( dip2px(mContext, 54f) , LayoutParams.MATCH_PARENT);
		rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		RelativeLayout right = createSelect(selectListener);
		right.setId(99);
		right.setLayoutParams(rl);
		layout.addView(right);

		RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout mFooterleft = new LinearLayout(mContext);
		mFooterleft.setOrientation(HORIZONTAL);
		ll.addRule(RelativeLayout.LEFT_OF, right.getId());
		mFooterleft.setLayoutParams(ll);
		layout.addView(mFooterleft);

		for (int i = 0; i < texts.length; i++) {
			if (srcs == null) {
				res = -1;
			} else {
				res = srcs[i];
			}

			int style = GREEN;
			if((i + 1) % 2 == 0){
				style = RED;
			}

			if (btnListeners == null || btnListeners[i] == null) {
				mFooterleft.addView(createBottom(texts[i], res, null , style ));
			} else {
				mFooterleft.addView(createBottom(texts[i], res, btnListeners[i] , style));
			}// end if-else
		}// end for
	}

	/**
	 * @param listener
	 * @return RelativeLayout
	 */
	private RelativeLayout createSelect(OnClickListener listener) {
		RelativeLayout layout = new RelativeLayout(mContext);
		LinearLayout innerLayout = new LinearLayout(mContext);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(0, 0, dip2px(mContext, 6), 0);
		innerLayout.setLayoutParams(lp);
		innerLayout.setBackgroundResource(R.drawable.common_checkbox_bg_selector);
		innerLayout.setGravity(Gravity.CENTER);
		selectIv = new ImageView(mContext);
		selectIv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		selectIv.setBackgroundResource(R.drawable.common_checkbox_uncheck);
		innerLayout.addView(selectIv);
		innerLayout.setOnClickListener(listener);
		layout.addView(innerLayout);
		// 保存
//		bottoms.add(layout);
		return layout;
	}

	/**
	 * 创建底部操作项
	 * @param text
	 * @param src
	 * @param listener
	 * @return RelativeLayout
	 */
	private RelativeLayout createBottom(String text, int src, OnClickListener listener,  int style) {
		RelativeLayout layout = new RelativeLayout(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		lp.setMargins(0, 0, dip2px(mContext, 6), 0);
		layout.setLayoutParams(lp);
		LinearLayout innerLayout = new LinearLayout(mContext);
		innerLayout.setId(199);
		
		if(style == GREEN){
			innerLayout.setBackgroundResource(R.drawable.common_btn_blue_selector);
		}else if(style == RED){
			innerLayout.setBackgroundResource(R.drawable.common_btn_red_selector);
		}else{
			innerLayout.setBackgroundResource(R.drawable.common_btn_blue_selector);
		}
		innerLayout.setGravity(Gravity.CENTER);

		if (src > 0) {
			ImageView im = new ImageView(mContext);
			im.setBackgroundResource(src);
			innerLayout.addView(im);
		}

		TextView tv = new TextView(mContext);
		tv.setId(299);
		tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 参见@dimen/myphone_title_bottom_text_size
//		tv.setTextColor(bottomColor);// 参见@color/white
		tv.setTextColor(getResources().getColor(R.color.white));
		tv.setText(text);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(dip2px(mContext, TEXT_PADDING), 0, 0, 0);// @dimen/text_drawpadding
		tv.setLayoutParams(params);
		innerLayout.addView(tv);
		innerLayout.setOnClickListener(listener);
		
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(mContext, 37));
		relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(innerLayout, relativeParams);
		// 保存
		bottoms.add(layout);
		return layout;
	}


    /**
     * 创建底部操作项
     * @param text
     * @param src
     * @param listener
     * @return RelativeLayout
     */
    private RelativeLayout createBottom(String text, Drawable src, OnClickListener listener) {
        RelativeLayout layout = new RelativeLayout(mContext);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, dip2px(mContext, 6), 0);
        layout.setGravity(Gravity.RIGHT);
        layout.setLayoutParams(lp);
        LinearLayout innerLayout = new LinearLayout(mContext);
        innerLayout.setId(199);

        if (src != null){
            src.setBounds(0, 0, src.getIntrinsicWidth(), src.getIntrinsicHeight());
            innerLayout.setBackgroundDrawable(src);
        }else{
            innerLayout.setBackgroundResource(R.drawable.common_btn_blue_selector);
        }

        innerLayout.setGravity(Gravity.CENTER);

        TextView tv = new TextView(mContext);
        tv.setId(299);
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.RIGHT);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 参见@dimen/myphone_title_bottom_text_size
//		tv.setTextColor(bottomColor);// 参见@color/white
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setText(text);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(dip2px(mContext, TEXT_PADDING), 0, 3*dip2px(mContext, TEXT_PADDING), 0);// @dimen/text_drawpadding
        tv.setLayoutParams(params);
        innerLayout.addView(tv);
        innerLayout.setOnClickListener(listener);
        
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(mContext, 37));
		relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(innerLayout, relativeParams);
        // 保存
        bottoms.add(layout);
        return layout;
    }

	/**
	 * 替换头部菜单
	 * @param child
	 */
	public void replaceMenu(View child) {
		if (child != null && mHeader != null)
			mHeader.replaceMenu(child);
	}

    /**
     * 替换头部菜单
     * @param child
     */
    public void replaceMenu(View child, boolean adjustMenuSize) {
        if (child != null && mHeader != null)
            mHeader.replaceMenu(child, adjustMenuSize);
    }

	/**
	 * 设置返回监听
	 * @param listener
	 */
	public void setGoBackListener(OnClickListener listener) {
		if (mHeader != null) {
			mHeader.setOnClickListener(listener);
		}
	}

	/**
	 * 向外提供一个底部添加子View的接口
	 * @param index 第几个操作项
	 * @param child
	 */
	public void addViewIntoBottom(int index, View child) {
		if (child != null)
			bottoms.get(index).addView(child);
	}

	/**
	 * 隐藏头部
	 * @param visibility
	 */
	public void setHeadVisibility(int visibility) {
		if (mHeader != null) {
			mHeader.setVisibility(visibility);
		}
	}

	/**
	 * 隐藏底部按钮
	 * @param index
	 * @param visibility
	 */
	public void setBottomVisibility(int index, int visibility) {
		if (index >= 0 && index < bottoms.size()) {
			bottoms.get(index).setVisibility(visibility);
		}
	}

	/**
	 * 描述:隐藏底部按钮
	 *
	 * @author linqiang(866116)
	 * @Since 2013-4-23
	 * @param index
	 * @param visibility
	 */
//	public void setBottomTitle(int index, String title) {
//		if (index >= 0 && index < bottoms.size()) {
//			bottoms.get(index).setVisibility(visibility);
//		}
//	}

	/**
	 * 将dip转换为px
	 * @param cxt
	 * @param dipValue
	 * @return int
	 */
	private int dip2px(Context cxt, float dipValue) {
		final float scale = cxt.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 设置底部是否可见 (这里要注意,在设置可见和不可见时要对应好编辑状态)
	 * @param visibility
	 */
	public void setBottomBgVisibility(int visibility) {
		// 注意
		if (isEditerMode()) {
			if (mEidterBottom != null)
				mEidterBottom.setVisibility(visibility);
		} else {
			if (mBottom != null)
				mBottom.setVisibility(visibility);
		}
	}

	/**
	 * 头部返回是否可见
	 * @param visibility
	 */
	public void setGobackVisibility(int visibility) {
		if (mHeader != null) {
			mHeader.setVisibility(visibility);
		}
	}

	/**
	 * 切换编辑模式
	 * @param isEditer
	 */
	public void setEditerMode(boolean isEditer) {
		this.isEditer = isEditer;
		if (isEditer) {
			if (mEidterBottom != null)
				mEidterBottom.setVisibility(VISIBLE);
			if (mBottom != null)
				mBottom.setVisibility(GONE);
		} else {
			if (mBottom != null)
				mBottom.setVisibility(VISIBLE);
			if (mEidterBottom != null)
				mEidterBottom.setVisibility(GONE);
		}
	}

	/**
	 * 是否编辑模式
	 * @return boolean
	 */
	public boolean isEditerMode() {
		return isEditer;
	}

	/**
	 * 设置title
	 * @param title
	 */
	public void setTitle(String title) {
		if (title != null && mHeader != null) {
			mHeader.setTitle(title);
		}
	}

	/**
	 * 设置标题文字位置
	 * @param gravity
	 */
	public void setTitleGravity(int gravity) {
		if (mHeader != null) {
			mHeader.setTitleGravity(gravity);
		}
	}

	/**
	 * 菜单是否可见
	 * @param visibility
	 */
	public void setMenuVisibility(int visibility) {
		if (mHeader != null) {
			mHeader.setMenuVisibility(visibility);
		}
	}

	/**
	 * 修改菜单图片
	 * @param resid
	 */
	public void setMenuImageResource(int resid) {
		if (mHeader != null) {
			mHeader.setMenuImageResource(resid);
		}
	}

	/**
	 * 设置点击事件
	 * @param listener
	 */
	public void setMenuListener(OnClickListener listener) {
		if (mHeader != null) {
			mHeader.setMenuListener(listener);
		}
	}

	/**
	 * 设置按钮是否可用
	 * @param index
	 * @param enable
	 */
	public void setBottomEnabled(int index, boolean enable) {
		if (index >= 0 && index < bottoms.size()) {
			bottoms.get(index).findViewById(199).setEnabled(enable);
		}
	}

	/**
	 * 修改按钮文字
	 * @param index
	 * @param text
	 */
	public void setBottomText(int index, String text) {
		if (index >= 0 && index < bottoms.size()) {
			TextView tv = (TextView)bottoms.get(index).findViewById(299);
			tv.setText(text);
		}
	}

	/**
	 * 获取按钮文字
	 * @param index
	 */
	public CharSequence getBottomText(int index) {
		if (index >= 0 && index < bottoms.size()) {
			TextView tv = (TextView)bottoms.get(index).findViewById(299);
			return tv.getText();
		} else {
			return "";
		}
	}

	public View getBottom(int index) {
		if (index >= 0 && index < bottoms.size()) {
			return bottoms.get(index);
		}
		return null;
	}

	@Override
	public void setSelected(boolean selected){
		if(selectIv != null){
			if(selected){
				selectIv.setBackgroundResource(R.drawable.common_checkbox_checked);
			}else{
				selectIv.setBackgroundResource(R.drawable.common_checkbox_uncheck);
			}
		}
	}

	public void setBottomTag(int index, Object tag) {
		if (index >= 0 && index < bottoms.size()) {
			bottoms.get(index).findViewById(199).setTag(tag);
		}
	}
}
