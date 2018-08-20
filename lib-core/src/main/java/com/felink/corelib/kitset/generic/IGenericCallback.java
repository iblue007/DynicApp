package com.felink.corelib.kitset.generic;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 15:21.</br>
 * @update: </br>
 */

public interface IGenericCallback {

    void onFailed(int code, String pName, int pVer);

    void onCompleted(String pName, int pOldVer, int pNewVer, String content);
}
