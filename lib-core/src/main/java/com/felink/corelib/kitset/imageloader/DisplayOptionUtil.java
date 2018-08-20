package com.felink.corelib.kitset.imageloader;

import android.graphics.Bitmap;

import com.felink.corelib.R;
import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.ScreenUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


/**
 * Created by dingdongjin_dian91 on 2016/2/19.
 */
public class DisplayOptionUtil {

    public static final DisplayImageOptions DEFAULT_OPTIONS = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public static final DisplayImageOptions VIDEO_UNIT_ITEM_OPTIONS = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.drawable.ic_loading_placehold)
            .showImageForEmptyUri(R.drawable.ic_loading_placehold)
            .showImageOnFail(R.drawable.ic_loading_placehold)
            .considerExifParams(true)
            .build();

    public static final DisplayImageOptions VIDEO_UNIT_ITEM_SMALL_OPTIONS = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.drawable.ic_loading_placehold_small)
            .showImageForEmptyUri(R.drawable.ic_loading_placehold_small)
            .showImageOnFail(R.drawable.ic_loading_placehold_small)
            .considerExifParams(true)
            .build();

    public static final DisplayImageOptions VIDEO_ROUNDED_OPTIONS = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.drawable.ic_loading_placehold)
            .showImageForEmptyUri(R.drawable.ic_loading_placehold)
            .showImageOnFail(R.drawable.ic_loading_placehold)
            .displayer(new RoundedBitmapDisplayer(ScreenUtil.dip2px(Global.getContext(), 3), 0))
            .considerExifParams(true)
            .build();


    public static final DisplayImageOptions VIDEO_ROUND_ICON_OPTIONS = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.drawable.ic_default_user_icon)
            .showImageForEmptyUri(R.drawable.ic_default_user_icon)
            .showImageOnFail(R.drawable.ic_default_user_icon)
            .displayer(new RoundedBitmapDisplayer(ScreenUtil.dip2px(Global.getContext(), 1000), 0))
            .considerExifParams(true)
            .build();


}
