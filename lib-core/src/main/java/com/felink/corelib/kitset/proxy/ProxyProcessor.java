package com.felink.corelib.kitset.proxy;

import android.text.TextUtils;
import android.util.Log;

import com.felink.corelib.config.Global;
import com.felink.corelib.kitset.StringUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 19:37.</br>
 * @update: </br>
 */

public class ProxyProcessor implements Runnable {

    private static final String TAG = "ProxyProcessor";

    private static final String HTTP_END = "\r\n\r\n";
    private static final String KEY_URL = "_url";

    private String[] queue = new String[1];

    private byte[] _buffer = new byte[512];
    private Socket proxySocket;
    private int proxyPort = -1;
    private String proxyHost;
    private int originPort = -1;
    private String originHost;

    private String originUrl;

    private AtomicInteger atomicInteger = new AtomicInteger();

    private Map<String, String> reqParams = new HashMap<String, String>();

    private IVideoDiskCache diskCache;

    /**
     * 设置全局共享的代理IP和端口
     *
     * @param proxyHost 代理IP
     * @param proxyPort 代理端口
     */
    public ProxyProcessor(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    /**
     * 加入处理队列中
     *
     * @param url
     */
    public synchronized String enqueue(final String url, final ProxyCallback callback) {
        //终止前一个socket
        shutdownSocket();
        String proxyUrl = url;
        originUrl = url;
        try {
            proxyUrl = ProxyUtil.getRedirectUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        queue[0] = proxyUrl;
        URI uri = URI.create(proxyUrl);
        //原始的Host
        String sHost = uri.getHost();
        //原始的端口
        int sPort = uri.getPort();
        originPort = sPort;
        originHost = sHost;
        if (sPort == -1) {
            //无明确指定端口
            proxyUrl = proxyUrl.replace(sHost, proxyHost + ":" + proxyPort);
        } else {
            //明确指定端口
            proxyUrl = proxyUrl.replace(sHost + ":" + sPort, proxyHost + ":" + proxyPort);
        }

        final Map<String, String> p = new HashMap<String, String>();
        if (diskCache != null) {
            File cache = diskCache.get(proxyUrl);
            if (cache != null && cache.exists()) {
                p.put("Range", "bytes=" + cache.length() + "-");
            }
        }

        if (callback != null) {
            final String pUrl = proxyUrl;
            Global.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    callback.onPrepared(url, pUrl, p);
                }
            });
        }
        return proxyUrl;
    }

    /**
     * 是否正在处理中
     *
     * @param url
     * @return
     */
    public boolean inQueue(String url) {
        String key = queue[0];
        if (!TextUtils.isEmpty(key) && key.equals(url)) {
            return true;
        }
        return false;
    }

    public void process(Socket socket) {
        //阻塞
        atomicInteger.incrementAndGet();
        Map<String, String> params = parseRequestInfo(socket);
        if (!inQueue(params.get(KEY_URL))) {
            shutdownSocket();
        }

        this.proxySocket = socket;
        reqParams.clear();
        reqParams.putAll(params);

        //开始执行...
        run();
    }

    private void shutdownSocket() {
        if (this.proxySocket != null && !this.proxySocket.isClosed()) {
            try {
                this.proxySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析请求信息
     *
     * @param socket
     * @return
     */
    private Map<String, String> parseRequestInfo(Socket socket) {
        InputStream is = null;
        Map<String, String> params = new HashMap<String, String>();
        try {
            is = new BufferedInputStream(socket.getInputStream());
            //取发起的请求头信息+请求行
            StringBuffer header = new StringBuffer();
            int len = 0;
            while ((len = is.read(_buffer)) != -1) {
                byte[] tmp = new byte[len];
                System.arraycopy(_buffer, 0, tmp, 0, len);
                String s = new String(tmp, "UTF-8");
                header.append(s);
                if (s.contains(HTTP_END)) {
                    break;
                }
            }

            //替换请求头信息中的HOST和PORT
            String headerStr = header.toString();
            headerStr = headerStr.replace(proxyHost + ":" + proxyPort, originHost + (originPort == -1 ? "" : ":" + originPort));

            //start >> 代理给 okhttp 请求
            String[] arr = headerStr.split("\\r\\n");
            String url = null;
            if (arr != null && arr.length > 0) {
                for (String line : arr) {
                    if (!line.startsWith("GET ")) {
                        String[] peer = line.split(":");
                        if (peer != null && peer.length == 2) {
                            params.put(peer[0].trim(), peer[1].trim());
                        }
                    } else {
                        url = line.replace("GET ", "").replaceAll(" (HTTP)[\\s\\S]*", "");
                    }
                }

                //1、拼接URL
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + params.get("Host") + url;
                }
                params.put(KEY_URL, url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public void setDiskCache(IVideoDiskCache diskCache) {
        this.diskCache = diskCache;
    }

    @Override
    public void run() {
        OutputStream proxyOs = null;
        OutputStream netOs = null;
        InputStream netIs = null;

        //是否需要缓存
        boolean shouldCache = false;
        //真实的资源地址
        String url = null;
        //总的内容大小
        long totalLen = 0;
        //数据读取成功
        boolean suc = true;

        try {
            //获取请求头
            Map<String, String> requestParams = reqParams;
            //获取真实的请求地址
            url = requestParams.get(KEY_URL);
            //判断当前请求的URL地址是否是当前正在代理的URL
            if (!inQueue(url)) {
                return;
            }

            proxyOs = new BufferedOutputStream(proxySocket.getOutputStream());

            OkHttpClient client = new OkHttpClient.Builder().build();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);

            long[] range = ProxyUtil.parseRange(requestParams.get("Range"));

            //2、添加头信息
            for (HashMap.Entry<String, String> entry : requestParams.entrySet()) {
                if (!KEY_URL.equals(entry.getKey())) {
                    requestBuilder.addHeader(entry.getKey(), entry.getValue());
                }
            }

            //3、请求
            Response response = client.newCall(requestBuilder.build()).execute();

            //4、拼接响应状态行+响应头
            StringBuffer sb = new StringBuffer();
            //状态行
            sb.append(response.protocol().toString().toUpperCase() + " " + response.code() + " " + response.message() + "\r\n");
            //响应头
            Headers headers = response.headers();
            //获取响应头-内容长度
            long contentLen = StringUtil.parseLong(headers.get("Content-Length"), 0l);
            //获取响应头-内容类型
            String contentType = headers.get("Content-Type");
            Log.e(TAG, "mimeType=" + contentType);

            //提取响应头部信息
            for (int i = 0, size = headers.size(); i < size; i++) {
                String name = headers.name(i);
                String value = headers.get(name);
                sb.append(name + ":" + value + "\r\n");
            }
            sb.append("\r\n");
            netIs = response.body().byteStream();
            proxyOs.write(sb.toString().getBytes());

            //实际的资源总长度，用以验证缓存文件的合法性
            totalLen = range[0] < 0 ? contentLen : contentLen + range[0];


            //校验缓存的完整性 -- start

            File cacheFile = diskCache.get(url);
            if (diskCache != null && (!diskCache.isAvailable(url))) {
                if (totalLen > 0 && totalLen != cacheFile.length()) {
                    shouldCache = true;
                    diskCache.beginTransaction(url);
                }
            }
            //校验缓存的完整性 -- end

            int len = 0;
            //6、解析响应数据
            while ((len = netIs.read(_buffer)) != -1) {
                try {
                    byte[] tmp = new byte[len];
                    System.arraycopy(_buffer, 0, tmp, 0, len);
                    if (proxySocket.isInputShutdown() || proxySocket.isOutputShutdown() || proxySocket.isClosed()) {
                        suc = false;
                        break;
                    }
                    try {
                        proxyOs.write(tmp);
                        proxyOs.flush();
                    } catch (Exception e) {
                    }

                    try {
                        if (diskCache != null && shouldCache) {
                            diskCache.write(tmp);
                        }
                    } catch (Exception e) {
                        suc = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //end >> 代理给 okhttp 请求
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (diskCache != null && shouldCache) {
                diskCache.endTransaction();
                //如果缓存过程出现异常或文件长度不一致时，删除
                if (!suc || diskCache.get(url).length() != totalLen) {
                    Log.e(TAG, "cache failure![" + diskCache.get(url).length() + "," + totalLen + "] post remove");
                    diskCache.remove(url);
                }
            }

            ProxyUtil.closeQuietly(proxyOs);
            ProxyUtil.closeQuietly(netIs);
            ProxyUtil.closeQuietly(netOs);
//            CommonUtil.closeQuietly(cacheOs);
            if (diskCache != null) {
                diskCache.endTransaction();
            }
            try {
                if (proxySocket != null) {
                    proxySocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
