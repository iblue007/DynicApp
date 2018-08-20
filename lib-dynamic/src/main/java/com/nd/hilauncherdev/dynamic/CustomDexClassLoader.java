package com.nd.hilauncherdev.dynamic;

import dalvik.system.DexClassLoader;

/**
 * Created by dingdongjin_dian91 on 2016/3/30.
 */
public class CustomDexClassLoader extends DexClassLoader {

    private static final String TAG = "CustomDexClassLoader";


    public CustomDexClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(className);
        if (clazz == null) {
            try {
                clazz = findClass(className);
            } catch (ClassNotFoundException e) {
            }
            if(clazz == null) {
                try {
                    clazz = super.loadClass(className);
                    if(clazz != null) {
                        //Log.e(TAG, "super.loadClass: "+className);
                    }
                } catch (ClassNotFoundException e) {
                    // Don't want to see this.
                }
            }else{
                //Log.e(TAG, "findClass: "+className);
            }
        }else{
           // Log.e(TAG, "findLoadedClass: "+className);
        }

        if(clazz == null) {
            throw new ClassNotFoundException(className);
        }

        return clazz;
    }


}
