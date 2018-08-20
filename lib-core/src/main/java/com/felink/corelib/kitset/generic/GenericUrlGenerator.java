package com.felink.corelib.kitset.generic;

import android.content.Context;

import com.felink.corelib.kitset.TelephoneUtil;
import com.felink.corelib.kitset.URLs;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 14:46.</br>
 * @update: </br>
 */

public class GenericUrlGenerator implements IGenericUrlGenerator {

    private static final String BASE_URL = URLs.PANDAHOME_BASE_URL + "commonuse/clientconfig.ashx?";

    @Override
    public String genWithVer(Context context, String pName, int pVer, String pid, int mt) {
        String appVerName = TelephoneUtil.getVersionName(context);
        String url = BASE_URL + "cname=" + pName + "&ver=" + pVer + "&pid=" + pid + "&mt=" + mt + "&DivideVersion=" + appVerName;
        return url;
    }

    @Override
    public String genIgnoreVer(Context context, String pName, String pid, int mt) {
        return genWithVer(context, pName, 0, pid, mt);
    }
}
