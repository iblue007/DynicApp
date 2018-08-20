package com.felink.corelib.kitset.generic;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 15:52.</br>
 * @update: </br>
 */

public class GenericPersist implements IGenericPersist {

    private static final String SP_NAME = "generic";

    private SharedPreferences sp;

    public GenericPersist(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void put(String pName, int ver, String content) {
        sp.edit().putString(keyContent(pName), content).commit();
        sp.edit().putInt(keyVersion(pName), ver).commit();
    }

    @Override
    public GenericLoader.GenericBean get(String pName) {
        int ver = sp.getInt(keyVersion(pName), 0);
        String content = sp.getString(keyContent(pName), "");
        GenericLoader.GenericBean bean = new GenericLoader.GenericBean();
        bean.version = ver;
        bean.content = content;
        return bean;
    }

    @Override
    public void remove(String pName) {
        sp.edit().remove(keyContent(pName));
        sp.edit().remove(keyVersion(pName));
    }

    @Override
    public String keyContent(String pName) {
        return "generic_" + pName + "_content";
    }

    @Override
    public String keyVersion(String pName) {
        return "generic_" + pName + "_version";
    }

}
