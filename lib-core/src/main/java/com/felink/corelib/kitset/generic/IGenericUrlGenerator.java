package com.felink.corelib.kitset.generic;

import android.content.Context;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 14:47.</br>
 * @update: </br>
 */

public interface IGenericUrlGenerator {

    String genWithVer(Context context, String pName, int pVer, String pid, int mt);

    String genIgnoreVer(Context context, String pName, String pid, int mt);
}
