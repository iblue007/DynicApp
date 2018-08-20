package com.nd.hilauncherdev.widget.shop.util;

import com.nd.hilauncherdev.datamodel.DynamicConstant;

public enum WidgetPositionType {
	
	OFFLINE_TYPE {
		@Override
		public String getBaseDir() {
			return "";
		}
	},ONLINE_TYPE {
		@Override
		public String getBaseDir() {
			return DynamicConstant.DOWNLOAD_DIR;
		}
	},
	ONLINE_WIFI_TYPE {
		@Override
		public String getBaseDir() {
			return DynamicConstant.WIFI_DOWNLOAD_PATH;
		}
	};

	public abstract String getBaseDir();

	public static int getIntValue(WidgetPositionType t) {
		int ret = -1;
		switch (t) {
		case OFFLINE_TYPE:
			ret = 0;
			break;
		case ONLINE_TYPE:
			ret = 1;
			break;
		case ONLINE_WIFI_TYPE:
			ret = 2;
			break;
		default:
			break;
		}
		return ret;
	}
	
	public static WidgetPositionType getEnumValue(int v) {
		WidgetPositionType widgetPosType = OFFLINE_TYPE;
		switch (v) {
		case 0:
			widgetPosType = OFFLINE_TYPE;
			break;
		case 1:
			widgetPosType = ONLINE_TYPE;
			break;
		case 2:
			widgetPosType = ONLINE_WIFI_TYPE;
			break;
		default:
			break;
		}
		return widgetPosType;
	}
}