package com.nd.hilauncherdev.dynamic.bean;


import com.felink.corelib.config.Global;

public enum ActivityProcess {

	// PROCESS_MYPHONE {
	// @Override
	// public String getName() {
	// return Global.PKG_NAME + ":hilauncherex_myphone";
	// }
	// },
	PROCESS_SHOP {
		@Override
		public String getName() {
			return Global.getPkgName() + ":hilauncherex_shopv2_process";
		}
	},
	PROCESS_LAUNCHER {
		@Override
		public String getName() {
			return Global.getPkgName();
		}
	};
	public abstract String getName();

	public static ActivityProcess toEnum(String name) {
		if (PROCESS_SHOP.getName().equals(name)) {
			return PROCESS_SHOP;
		} else {
			return PROCESS_LAUNCHER;
		}
		// else if (PROCESS_LAUNCHER.equals(name)) {
		// return PROCESS_LAUNCHER;
		// } else {
		// return PROCESS_MYPHONE;
		// }
	}
}
