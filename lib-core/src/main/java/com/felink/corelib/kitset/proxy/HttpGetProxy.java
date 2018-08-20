package com.felink.corelib.kitset.proxy;

import android.text.TextUtils;
import android.util.Log;

import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.ThreadUtil;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description: media Get请求代理</br>
 * @author: cxy </br>
 * @date: 2017年06月22日 18:58.</br>
 * @update: </br>
 */

public class HttpGetProxy {

    private static final String LOCAL_HOST = "127.0.0.1";
    private static final String LOCAL_HOST_EX = "10.0.2.2";
    /**
     * 可用的代理HOST
     */
    private static String sAvailableProxyHost = LOCAL_HOST;
    /**
     * 代理socket
     */
    private static ServerSocket sProxySocket;
    /**
     * 监听端口
     */
    private static int sMonitorPort = -1;

    private static ProxyProcessor sProcessor;

    private IVideoDiskCache diskCache;

    private volatile boolean postPaused = false;
    private volatile boolean inLooping = false;

    private HttpGetProxy() {

    }

    private static class HttpGetProxyHolder {
        public static final HttpGetProxy sInstance = new HttpGetProxy();
    }

    public static HttpGetProxy get() {
        return HttpGetProxyHolder.sInstance;
    }

    public void setDiskCache(IVideoDiskCache cache) {
        this.diskCache = cache;
        sProcessor.setDiskCache(diskCache);
    }

    /**
     * 监听端口
     *
     * @param port
     */
    public void monitor(int port) {
        sMonitorPort = port;
        //重置属性
        if (sProxySocket != null) {
            try {
                if (!sProxySocket.isClosed()) {
                    sProxySocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            sProxySocket = null;
        }

        boolean gotHost = false;
        if (!TextUtils.isEmpty(sAvailableProxyHost)) {
            try {
                sProxySocket = new ServerSocket(port, 1, InetAddress.getByName(sAvailableProxyHost));
                gotHost = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!gotHost) {
            try {
                sProxySocket = new ServerSocket(port, 1, InetAddress.getByName(LOCAL_HOST));
                sAvailableProxyHost = LOCAL_HOST;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    sProxySocket = new ServerSocket(port, 1, InetAddress.getByName(LOCAL_HOST_EX));
                    sAvailableProxyHost = LOCAL_HOST;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        sProcessor = new ProxyProcessor(sAvailableProxyHost, sMonitorPort);

//        loop();
    }

    public void enqueue(final String url, final ProxyCallback callback) {
        //出现异常 或 代理关闭，就不再代理
        if (!ProxyUtil.isProxyEnable() || !validSSocket()) {
            if (callback != null) {
                Global.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onPrepared(url, url, null);
                    }
                });
            }
            return;
        }
        postPaused = false;
        if (!inLooping) {
            loop();
        }
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                sProcessor.enqueue(url, callback);
            }
        });
    }

    public void postPause() {
        postPaused = true;
    }

    private void loop() {
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                Log.e("cxydebug", "p is looping");
                inLooping = true;
                while (true) {
                    if (postPaused) {
                        Log.e("cxydebug", "p do pause!!");
                        inLooping = false;
                        break;
                    }
                    try {
                        //出现异常就直接退出
                        final Socket socket = sProxySocket.accept();
                        sProcessor.process(socket);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (!validSSocket()) {
                            inLooping = false;
                            break;
                        }
                    }
                }
            }
        });
    }

    private boolean validSSocket() {
        if (sProxySocket == null || sProxySocket.isClosed() || !sProxySocket.isBound()) {
            return false;
        }
        return true;
    }
}
