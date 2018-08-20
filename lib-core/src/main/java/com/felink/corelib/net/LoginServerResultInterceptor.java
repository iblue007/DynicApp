package com.felink.corelib.net;

import com.felink.corelib.kitset.ThreadUtil;
import com.felink.corelib.net.base.ServerResultInterceptor;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月17日 12:50.</br>
 * @update: </br>
 */

public class LoginServerResultInterceptor extends ServerResultInterceptor {
    @Override
    public void onIntercept(int code) {
        if (code == 8) {
            ThreadUtil.executeMore(new Runnable() {
                @Override
                public void run() {
//                    LoginTool.autoLoginOnBackGround(Global.getContext());
//                    Global.runInMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            MessageUtils.showOnlyToast(Global.getContext(), "微视频壁纸开了小差，请重试一下~");
//                        }
//                    });
                }
            });
        }
    }
}
