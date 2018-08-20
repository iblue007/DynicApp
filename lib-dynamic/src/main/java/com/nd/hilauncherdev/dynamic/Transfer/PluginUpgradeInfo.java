package com.nd.hilauncherdev.dynamic.Transfer;

/**
 * 插件升级信息
 *
 * @author guojianyun
 */
public class PluginUpgradeInfo extends Plugin{
	private int targetVersion;//91桌面最低版本号
	private String downloadPath;//插件下载地址
	private String downloadType;// 下载类型
	private String md5Value;//该版本插件的MD5校验值
	public static final int NEED_RESTART = 1;
	public static final int NEED_NOT_RESTART = 0;
	private int needRestart = NEED_RESTART; //是否需要重新启动 1-需要重启 0-不需要重启 不配置时为1-需要重启
	public static final int STATE_ENABLE = 1;
	public static final int STATE_DISABLE = 0;

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	private int currentState = STATE_ENABLE;
	public int getNeedRestart() {
		return needRestart;
	}

	public void setNeedRestart(int needRestart) {
		this.needRestart = needRestart;
	}

	public String getMd5Value() {
		return md5Value;
	}
	
	public void setMd5Value(String md5Value) {
		this.md5Value = md5Value;
	}
	
	
	public String getPkgName() {
		return pkg;
	}
	public void setPkgName(String pkgName) {
		this.pkg = pkgName;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTargetVersion() {
		return targetVersion;
	}
	public void setTargetVersion(int targetVersion) {
		this.targetVersion = targetVersion;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	
	public String getDownloadType() {
		return downloadType;
	}
	
	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}
	
}
