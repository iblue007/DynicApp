package com.felink.corelib.net.base;


/**
 * 网络访问入口
 */
public class NetworkAccess {
    private static NetworkAccess sInstance;

    public static synchronized NetworkAccess getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkAccess();
        }
        return sInstance;
    }

    private NetworkAccess() {
    }

    /**
     * 执行网络访问（不启动新线程）
     *
     * @param runnable 执行的代码块
     * @return 执行结果
     */
    public Result execute(Runnable runnable) {
        Result result = new Result();
        if (!isNetworkAccessPermitted()) {
            result.code = Result.Code.NOT_PERMITTED;
        } else {
            if (runnable != null) {
                runnable.run();
            }
            result.code = Result.Code.SUCCEED;
        }
        return result;
    }

    /**
     * 网络访问结果
     */
    public static class Result {
        /**
         * 结果代码
         */
        public static enum Code {
            /**
             * 不允许进行网络访问
             */
            NOT_PERMITTED,
            /**
             * 执行成功
             */
            SUCCEED,
        }

        public Code code = Code.SUCCEED;
    }


    private static boolean isNetworkAccessPermitted() {
        return true;
    }

}
