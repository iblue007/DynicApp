package com.nd.hilauncherdev.dynamic.clientparser;

public class ClientWidgetView {
	/**
	 * View类路径
	 */
	private String layoutName;

	/**
	 * 预览图名称
	 */
	private String previewName;

	/**
	 * View大小
	 */
	private String type;

	public String getLayoutName() {
		return layoutName;
	}

	public String getPreviewName() {
		return previewName;
	}

	public String getType() {
		return type;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public void setPreviewName(String previewName) {
		this.previewName = previewName;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ClientWidgetView [layout_name=" + layoutName + ", type=" + type + ", preview_name=" + previewName + "]";
	}

}
