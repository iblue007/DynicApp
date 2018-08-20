package com.felink.corelib.kitset.proxy;

import android.text.TextUtils;

import com.felink.corelib.kitset.DigestUtil;
import com.felink.corelib.kitset.FileUtil;
import com.felink.corelib.kitset.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年06月23日 18:05.</br>
 * @update: </br>
 */

public class VideoDiskCache implements IVideoDiskCache {

    public DiskLruCache getDiskLruCache() {
        return diskLruCache;
    }

    private DiskLruCache diskLruCache;
    private File cacheDir;
    private int versionCode;
    private int valueCount;
    private long cacheSize;

    public VideoDiskCache(DiskLruCache cache) {
        this.diskLruCache = cache;
    }

    public VideoDiskCache(File cacheDir, int versionCode, int valueCount, long cacheSize) {
        this.cacheDir = cacheDir;
        this.versionCode = versionCode;
        this.valueCount = valueCount;
        this.cacheSize = cacheSize;
        rebuild();
    }

    private void rebuild() {
        try {
            diskLruCache = DiskLruCache.open(cacheDir, versionCode, valueCount, cacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkCache() {
        if (diskLruCache == null || diskLruCache.isClosed()) {
            rebuild();
        }
    }

    @Override
    public File getDirectory() {
        checkCache();
        try {
            String cacheDir = diskLruCache.getDirectory().getAbsolutePath();
            return new File(cacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File get(String url) {
        checkCache();
        try {
            File file = new File(getDirectory(), getKey(url) + ".0");
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(getDirectory(), getKey(url));
    }

    @Override
    public boolean save(String url, InputStream is) {
        checkCache();
        if (TextUtils.isEmpty(url) || is == null) {
            return false;
        }
        OutputStream os = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit(getKey(url));
            if (editor == null) {
                return false;
            }
            os = editor.newOutputStream(0);

            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = is.read(buff)) != -1) {
                byte[] tmp = new byte[len];
                System.arraycopy(buff, 0, tmp, 0, len);
                os.write(tmp);
                os.flush();
            }
            editor.commit();
            diskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if (editor != null) {
                try {
                    diskLruCache.remove(getKey(url));
                    editor.abort();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    diskLruCache.flush();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(String url) {
        checkCache();
        try {
            return diskLruCache.remove(getKey(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        checkCache();
        try {
            diskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        checkCache();
        try {
            diskLruCache.delete();
            FileUtil.delAllFile(getDirectory().getAbsolutePath());
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getKey(String url) {
        if (!TextUtils.isEmpty(url)) {
            return DigestUtil.md5Hex(url);
        } else {
            return "";
        }
    }

    @Override
    public long size() {
        checkCache();
        File[] files = FileUtil.getFilesFromDir(getDirectory().getPath(), null);
        if (files != null && files.length > 0) {
            return diskLruCache.size();
        } else {
            return 0;
        }
    }

    private OutputStream transationOs = null;
    private boolean transaction = false;
    private DiskLruCache.Editor transactionEditor;
    private boolean processError = false;

    @Override
    public synchronized void beginTransaction(String url) {
        checkCache();
        if (transaction) {
            throw new IllegalStateException("previous transaction has not closed!");
        }
        transactionEditor = null;
        transaction = false;
        processError = false;
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            transactionEditor = diskLruCache.edit(getKey(url));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (transactionEditor != null) {
            try {
                transationOs = transactionEditor.newOutputStream(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (transactionEditor != null && transationOs != null) {
            transaction = true;
        }
    }

    @Override
    public synchronized void write(byte[] bytes) {
        checkCache();
        if (!transaction && processError) {
            return;
        }
        try {
            transationOs.write(bytes);
            transationOs.flush();
        } catch (Exception e) {
            e.printStackTrace();
            processError = true;
        }
    }

    @Override
    public void write(byte[] bytes, int offset) {
        checkCache();
        if (!transaction && processError) {
            return;
        }
        try {
            transationOs.write(bytes,0,offset);
            transationOs.flush();
        } catch (Exception e) {
            e.printStackTrace();
            processError = true;
        }
    }

    @Override
    public synchronized void endTransaction() {
        checkCache();
        if (!transaction) {
            return;
        }
        try {
            if (processError) {
                transactionEditor.abort();
            } else {
                transactionEditor.commit();
            }
            diskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ProxyUtil.closeQuietly(transationOs);
        transaction = false;
    }

    @Override
    public boolean isAvailable(String url) {
        checkCache();
        File cache = get(url);
        if (cache != null && cache.exists() && cache.isFile()) {
            if (ProxyUtil.isVideoAvailable(cache.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }
}
