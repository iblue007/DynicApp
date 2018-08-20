package com.felink.corelib.kitset;

import android.content.Context;

/**
 * Created by linliangbin on 2017/3/10 11:50.
 */

public class PhoneUtil {

    /**
     * 获取手机信息
     *
     * @param ctx
     * @return 手机信息
     */
    public static String getPhoneInfo(Context ctx) {
        String result = "";
        try {
            String rootInfo = TelephoneUtil.hasRootPermission() ? "root" : "not root";
            result = "Phone=" + TelephoneUtil.getMachineName() + "\n";
            result += "CPU=" + TelephoneUtil.getCPUABI() + "\n";
            result += "RootInfo=" + rootInfo + "\n";
            result += "Resolution=" + TelephoneUtil.getScreenResolution(ctx) + "\n";
            result += "Density=" + ScreenUtil.getDensity() + "\n";
            result += "FirmwareVersion=" + TelephoneUtil.getFirmWareVersion() + "\n";
//            result += "All Installed Version=" + ConfigPreferences.getInstance().getAllInstalledVersion() + "\n";
            result += "VersionCode=" + TelephoneUtil.getVersionCode(ctx, ctx.getPackageName()) + "\n";
            result += "Git=" + MetaUtil.getMetaData(ctx, "git") + "\n";


        } catch (Exception e) {
            return "";
        }

        return result;
    }

}
