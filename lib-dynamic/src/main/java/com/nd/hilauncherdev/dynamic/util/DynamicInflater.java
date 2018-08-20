package com.nd.hilauncherdev.dynamic.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DynamicInflater extends LayoutInflater {
	private Context mContext;
	private static final String[] sClassPrefixList = { "android.widget.", "android.webkit." };

	public DynamicInflater(Context context) {
		super(context);
		this.mContext = context;
	}

	protected DynamicInflater(LayoutInflater original, Context newContext) {
		super(original, newContext);
		this.mContext = newContext;
	}

	@Override
	public View inflate(int resource, ViewGroup root) {
		XmlResourceParser parser = mContext.getResources().getLayout(resource);
		return super.inflate(parser, root);
	}

	@Override
	public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
		XmlResourceParser parser = mContext.getResources().getLayout(resource);
		return super.inflate(parser, root, attachToRoot);
	}

	@Override
	protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
		for (String prefix : sClassPrefixList) {
			try {
				View view = createView(name, prefix, attrs);
				if (view != null) {
					return view;
				}
			} catch (ClassNotFoundException e) {
				// In this case we want to let the base class take a crack
				// at it.
			}
		}

		return super.onCreateView(name, attrs);
	}

	@Override
	public LayoutInflater cloneInContext(Context newContext) {
		return new DynamicInflater(this, newContext);
	}
}