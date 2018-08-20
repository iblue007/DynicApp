//package com.felink.corelib.kitset;
//
//import android.content.Context;
//
//import com.baidu91.account.login.LoginHandler;
//import com.baidu91.account.login.LoginManager;
//import com.felink.corelib.analytics.AnalyticsConstant;
//import com.felink.corelib.analytics.HiAnalytics;
//
///**
// * 登录工具类
// * init                     初始化
// * autoLoginOnBackGround    自动登录
// * felinkLogin              统一登录
// * felinkLoginOut           统一退出
// * 作者：xiaomao on 2017/6/5.
// */
//
//public class LoginTool {
//    /**
//     * 初始化登录模块
//     *
//     * @param ctx
//     */
//    public static void init(Context ctx) {
//        LoginManager.getInstance().init(ctx);
//    }
//
//    /**
//     * 自动登录
//     *
//     * @param mContext
//     * @return
//     */
//    public static boolean autoLoginOnBackGround(Context mContext) {
//        boolean success = LoginManager.getInstance().tryLogin(mContext);
//        if (success) {
//            onLoginSuccess(mContext);
//        } else {
//            onLoginFail(0, mContext);
//        }
//        return success;
//    }
//
//    /**
//     * 统一登录
//     *
//     * @param ctx
//     * @param mCallBack
//     */
//    public static void felinkLogin(final Context ctx, LoginCallBackWrapper mCallBack) {
//        LoginManager.getInstance().login(ctx, mCallBack, new RegisterCallBackWrapper(ctx));
//    }
//
//    /**
//     * 统一退出
//     *
//     * @param context
//     */
//    public static void felinkLoginOut(Context context) {
//        LoginManager.getInstance().logout(context);
//    }
//
//    /**
//     * 登录成功
//     *
//     * @param ctx
//     */
//    private static void onLoginSuccess(final Context ctx) {
//        HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_ACCOUNT_LOGIN, "succ");
//    }
//
//    /**
//     * 登录失败
//     *
//     * @param rawCode
//     * @param ctx
//     */
//    private static void onLoginFail(int rawCode, Context ctx) {
//        HiAnalytics.submitEvent(ctx, AnalyticsConstant.APP_ACCOUNT_LOGIN, "fail");
//    }
//
//    /**
//     * 登录回调
//     */
//    public static class LoginCallBackWrapper implements LoginHandler.LoginCallBack {
//        private Context ctx;
//
//        public LoginCallBackWrapper(Context ctx) {
//            this.ctx = ctx;
//        }
//
//        @Override
//        public void onCallback(int resultCode, int rawCode) {
//            switch (resultCode) {
//                case LoginManager.LOGIN_RESULT_CODE_SUCCESS:
//                    onLoginSuccess(ctx);
//                    break;
//                case LoginManager.LOGIN_RESULT_CODE_FAIL:
//                    onLoginFail(rawCode, ctx);
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 注册回调
//     */
//    public static class RegisterCallBackWrapper implements LoginHandler.RegisterCallBack {
//        private Context ctx;
//
//        public RegisterCallBackWrapper(Context ctx) {
//            this.ctx = ctx;
//        }
//
//        @Override
//        public void onSubmit() {
//        }
//
//        @Override
//        public void onSuccess() {//注册成功
//            onLoginSuccess(ctx);
//        }
//
//        @Override
//        public void onFail() {
//            onLoginFail(-1, ctx);
//        }
//    }
//}
