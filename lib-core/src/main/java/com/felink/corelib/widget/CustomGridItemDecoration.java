package com.felink.corelib.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.felink.corelib.R;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.ScreenUtil;

/**
 * 控制每行第一个和最后一个的decoration
 * Created by linliangbin on 2016/11/4.
 */

public class CustomGridItemDecoration extends RecyclerView.ItemDecoration {

    private Rect mPreOutRect;

    public void setOutRectWithPx(int left, int top, int right, int bottom) {
        mPreOutRect = new Rect(left, top, right, bottom);
    }

    public void setOutRect(int left, int top, int right, int bottom) {
        mPreOutRect = new Rect(
                ScreenUtil.dip2px(Global.getContext(), left),
                ScreenUtil.dip2px(Global.getContext(), top),
                ScreenUtil.dip2px(Global.getContext(), right),
                ScreenUtil.dip2px(Global.getContext(), bottom));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mPreOutRect != null) {
            outRect.left = mPreOutRect.left;
            outRect.top = mPreOutRect.top;
            outRect.right = mPreOutRect.right;
            outRect.bottom = mPreOutRect.bottom;
        } else {
            outRect.bottom = parent.getContext().getResources().getDimensionPixelSize(R.dimen.grid_vertical_margin);
            outRect.top = parent.getContext().getResources().getDimensionPixelSize(R.dimen.grid_vertical_margin);
            outRect.left = parent.getContext().getResources().getDimensionPixelSize(R.dimen.grid_horizon_margin);
            outRect.right = parent.getContext().getResources().getDimensionPixelSize(R.dimen.grid_horizon_margin);
        }
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
//            int spanCount = gridLayoutManager.getSpanCount();
//            int pos = gridLayoutManager.getPosition(view);
//            if ((pos % spanCount) == (spanCount - 1)) {
//                //同行最后一个，不要right
//                outRect.right = 0;
//            } else if (pos % spanCount == 0) {
//                //同行第一个，不要left
//                outRect.left = 0;
//            }
//
//        }
    }
}
