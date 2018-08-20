package com.nd.hilauncherdev.webconnect.downloadmanage.model.filetype;

import com.nd.hilauncherdev.webconnect.downloadmanage.model.BaseDownloadInfo;

public enum FileType {
	
	FILE_DYNAMIC_APK(4, new DynamicApkFileHelper()),
	FILE_NONE(BaseDownloadInfo.FILE_TYPE_NONE, null);

	IFileTypeHelper mHelper = null;
	int mId = 0;

	FileType(int id, IFileTypeHelper helper) {
		mId = id;
		mHelper = helper;
	};

	public IFileTypeHelper getHelper() {
		return mHelper;
	}
		
	public int getId() {
		return mId;
	}
		
	public static FileType fromId(int id) {
		for (FileType f : FileType.values()) {
			if (id == f.getId()) {
				return f;
			}
		}
		return FILE_NONE;
	}
}
