package com.felink.corelib.kitset.generic;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 15:51.</br>
 * @update: </br>
 */

public interface IGenericPersist {

    void put(String pName, int ver, String content);

    GenericLoader.GenericBean get(String pName);

    void remove(String pName);

    String keyContent(String pName);

    String keyVersion(String pName);
}
