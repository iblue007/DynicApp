package com.nd.hilauncherdev.dynamic;

public class PluginSrcNotFoundException extends Exception {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 5607403833881251846L;
	private static final String PLUGIN_ERROR = "Plugin Soruce Jar is Not Found in SD card!!!";
	private String mDexpath;
	private String mPackageName;

	public PluginSrcNotFoundException(String dexpath, String packageName) {
		super(PLUGIN_ERROR);
		this.mDexpath = dexpath;
		this.mPackageName = packageName;
	}

	public PluginSrcNotFoundException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public PluginSrcNotFoundException(String detailMessage) {
		super(detailMessage);
	}

	public PluginSrcNotFoundException(Throwable throwable) {
		super(throwable);
	}

	public String getDexpath() {
		return mDexpath;
	}

	public void setDexpath(String mDexpath) {
		this.mDexpath = mDexpath;
	}

	public String getPackageName() {
		return mPackageName;
	}

	public void setPackageName(String mPackageName) {
		this.mPackageName = mPackageName;
	}

}
