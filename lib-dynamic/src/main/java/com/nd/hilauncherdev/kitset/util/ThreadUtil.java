package com.nd.hilauncherdev.kitset.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ThreadUtil {
    private static ExecutorService moreExecutorService;
    public static void executeMore(Runnable command) {
        if(moreExecutorService == null) {
            moreExecutorService = Executors.newCachedThreadPool();
        }
        moreExecutorService.execute(command);
    }
}
