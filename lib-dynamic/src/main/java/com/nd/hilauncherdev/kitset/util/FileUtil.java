//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.nd.hilauncherdev.kitset.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class FileUtil {
    private static final String TAG = "FileUtil";
    public static FileFilter folderFilter = new FileFilter() {
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };
    public static FileFilter mp3fileFilter = new FileFilter() {
        public boolean accept(File pathname) {
            String tmp = pathname.getName().toLowerCase();
            return tmp.endsWith(".mp3");
        }
    };

    public FileUtil() {
    }

    public static boolean generateFile(File file, List<byte[]> datas) {
        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            Iterator e = datas.iterator();

            while(e.hasNext()) {
                byte[] e1 = (byte[])e.next();
                bos.write(e1);
            }

            boolean e2 = true;
            return e2;
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if(bos != null) {
                try {
                    bos.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return false;
    }

    public static boolean appendData(File file, byte[]... datas) {
        RandomAccessFile rfile = null;

        try {
            rfile = new RandomAccessFile(file, "rw");
            rfile.seek(file.length());
            byte[][] e = datas;
            int e1 = datas.length;

            for(int var5 = 0; var5 < e1; ++var5) {
                byte[] data = e[var5];
                rfile.write(data);
            }

            boolean var17 = true;
            return var17;
        } catch (Exception var15) {
            var15.printStackTrace();
        } finally {
            if(rfile != null) {
                try {
                    rfile.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }

        }

        return false;
    }

    public static void createDir(String dir) {
        File f = new File(dir);
        if(!f.exists()) {
            f.mkdirs();
        }

    }

    public static void writeFile(String path, String content, boolean append) {
        try {
            File e = new File(path);
            if(!e.getParentFile().exists()) {
                e.getParentFile().mkdirs();
            }

            if(!e.exists()) {
                e.createNewFile();
                e = new File(path);
            }

            FileWriter fw = new FileWriter(e, append);
            if(content != null && !"".equals(content)) {
                fw.write(content);
                fw.flush();
            }

            fw.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static void delFile(String path) {
        try {
            File e = new File(path);
            if(e.exists()) {
                e.delete();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            delFile(folderPath);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static boolean delAllFile(String path) {
        return delAllFile(path, (FilenameFilter)null);
    }

    public static boolean delAllFile(String path, FilenameFilter filenameFilter) {
        boolean flag = false;
        File file = new File(path);
        if(!file.exists()) {
            return true;
        } else if(!file.isDirectory()) {
            return flag;
        } else {
            File[] tempList = file.listFiles(filenameFilter);
            if(tempList == null) {
                return true;
            } else {
                int length = tempList.length;

                for(int i = 0; i < length; ++i) {
                    if(tempList[i].isFile()) {
                        tempList[i].delete();
                    }

                    if(tempList[i].isDirectory()) {
                        delAllFile(tempList[i].getAbsolutePath(), filenameFilter);
                        String[] ifEmptyDir = tempList[i].list();
                        if(null == ifEmptyDir || ifEmptyDir.length <= 0) {
                            tempList[i].delete();
                        }

                        flag = true;
                    }
                }

                return flag;
            }
        }
    }

    public static boolean copy(String srcFile, String destFile) {
        FileInputStream in = null;
        FileOutputStream out = null;

        boolean c;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] e = new byte[1024];

            int c1;
            while((c1 = in.read(e)) != -1) {
                out.write(e, 0, c1);
            }

            out.flush();
            boolean e1 = true;
            return e1;
        } catch (Exception var20) {
            System.out.println("Error!" + var20);
            c = false;
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException var19) {
                    var19.printStackTrace();
                }
            }

            if(null != out) {
                try {
                    out.close();
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            }

        }

        return c;
    }

    public static void copyFolder(String oldPath, String newPath) {
        (new File(newPath)).mkdirs();
        File a = new File(oldPath);
        String[] file = a.list();
        if(null != file) {
            File temp = null;

            for(int i = 0; i < file.length; ++i) {
                try {
                    if(oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + file[i]);
                    } else {
                        temp = new File(oldPath + File.separator + file[i]);
                    }

                    if(temp.isFile()) {
                        FileInputStream e = new FileInputStream(temp);
                        FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
                        byte[] b = new byte[5120];

                        int len;
                        while((len = e.read(b)) != -1) {
                            output.write(b, 0, len);
                        }

                        output.flush();
                        output.close();
                        e.close();
                    }

                    if(temp.isDirectory()) {
                        copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                }
            }

        }
    }

    public static void moveFile(String oldPath, String newPath) {
        copy(oldPath, newPath);
        delFile(oldPath);
    }

    public static boolean renameFile(String resFilePath, String newFilePath) {
        File resFile = new File(resFilePath);
        File newFile = new File(newFilePath);
        return resFile.renameTo(newFile);
    }

    public static void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    public static void saveStream2File(InputStream in, String fileName) {
        byte[] buffer = new byte[1000];
        BufferedOutputStream bufferedOutputStream = null;

        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));

            int size;
            while((size = in.read(buffer)) > -1) {
                bufferedOutputStream.write(buffer, 0, size);
            }

            bufferedOutputStream.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static File[] getFilesFromDir(String dirPath, FileFilter fileFilter) {
        File dir = new File(dirPath);
        return dir.isDirectory()?(fileFilter != null?dir.listFiles(fileFilter):dir.listFiles()):null;
    }

    public static List<String> getExistsFileNames(String dir, FileFilter fileFilter, boolean hasSuffix) {
        File file = new File(dir);
        File[] files = file.listFiles(fileFilter);
        ArrayList fileNameList = new ArrayList();
        if(null != files) {
            File[] var7 = files;
            int var8 = files.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                File tmpFile = var7[var9];
                String tmppath = tmpFile.getAbsolutePath();
                String fileName = getFileName(tmppath, hasSuffix);
                fileNameList.add(fileName);
            }
        }

        return fileNameList;
    }

    public static List<String> getAllExistsFileNames(String dir, boolean hasSuffix) {
        File file = new File(dir);
        File[] files = file.listFiles();
        ArrayList fileNameList = new ArrayList();
        if(null != files) {
            File[] var6 = files;
            int var7 = files.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                File tmpFile = var6[var8];
                if(tmpFile.isDirectory()) {
                    fileNameList.addAll(getAllExistsFileNames(tmpFile.getPath(), hasSuffix));
                } else {
                    String tmp = tmpFile.getName().toLowerCase();
                    if(tmp.endsWith(".png") || tmp.endsWith(".jpg") || tmp.endsWith(".bmp") || tmp.endsWith(".gif") || tmp.endsWith(".jpeg")) {
                        fileNameList.add(tmpFile.getAbsolutePath());
                    }
                }
            }
        }

        return fileNameList;
    }

    public static String getFileName(String path, boolean hasSuffix) {
        return null != path && -1 != path.lastIndexOf("/") && -1 != path.lastIndexOf(".")?(!hasSuffix?path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")):path.substring(path.lastIndexOf("/") + 1)):null;
    }

    public static String getPath(String path) {
        File file = new File(path);

        try {
            if(!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }

            return file.getAbsolutePath();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static boolean isFileExits(String dir, String fileName) {
        fileName = fileName == null?"":fileName;
        dir = dir == null?"":dir;
        int index = dir.lastIndexOf("/");
        String filePath;
        if(index == dir.length() - 1) {
            filePath = dir + fileName;
        } else {
            filePath = dir + "/" + fileName;
        }

        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFileExits(String filePath) {
        try {
            File file = new File(filePath);
            if(file.exists()) {
                return true;
            }
        } catch (Exception var2) {
            ;
        }

        return false;
    }

    public static boolean saveImageFile(String dirPath, String fileName, Bitmap bmp) {
        try {
            File e = new File(dirPath);
            if(!e.exists()) {
                boolean picPath = e.mkdirs();
                if(!picPath) {
                    return false;
                }
            }

            if(fileName == null || fileName.trim().length() == 0) {
                fileName = System.currentTimeMillis() + ".jpg";
            }

            File picPath1 = new File(dirPath, fileName);
            FileOutputStream fos = new FileOutputStream(picPath1);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public static boolean saveImageFile(String dirPath, String fileName, Bitmap bmp, CompressFormat format) {
        try {
            File e = new File(dirPath);
            if(!e.exists()) {
                boolean picPath = e.mkdirs();
                if(!picPath) {
                    return false;
                }
            }

            format = format == null?CompressFormat.JPEG:format;
            if(fileName == null || fileName.trim().length() == 0) {
                fileName = System.currentTimeMillis() + "";
                if(format.equals(CompressFormat.PNG)) {
                    fileName = fileName + ".png";
                } else {
                    fileName = fileName + ".jpg";
                }
            }

            File picPath1 = new File(dirPath, fileName);
            FileOutputStream fos = new FileOutputStream(picPath1);
            bmp.compress(format, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public static long getFileAllSize(String path) {
        try {
            File e = new File(path);
            if(!e.exists()) {
                return 0L;
            } else if(!e.isDirectory()) {
                long var10 = e.length();
                return var10;
            } else {
                File[] size = e.listFiles();
                long size1 = 0L;
                File[] var5 = size;
                int var6 = size.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    File f = var5[var7];
                    size1 += getFileAllSize(f.getPath());
                }

                return size1;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            return 0L;
        }
    }

    public static String readFileContent(String path) {
        StringBuffer sb = new StringBuffer();
        if(!isFileExits(path)) {
            return sb.toString();
        } else {
            FileInputStream ins = null;

            try {
                ins = new FileInputStream(new File(path));
                BufferedReader e = new BufferedReader(new InputStreamReader(ins));
                String line = null;

                while((line = e.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            } finally {
                if(ins != null) {
                    try {
                        ins.close();
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }
                }

            }

            return sb.toString();
        }
    }

    public static String readAssetsContent(Context ctx, String path) {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(path)));
            String e = null;

            while((e = reader.readLine()) != null) {
                sb.append(e);
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return sb.toString();
    }

    

    public static String getMemorySizeString(long size) {
        float result = (float)size;
        BigDecimal temp;
        if(result < 1024.0F) {
            temp = new BigDecimal((double)result);
            temp = temp.setScale(2, 4);
            return temp + "Bytes";
        } else {
            result /= 1024.0F;
            if(result < 1024.0F) {
                temp = new BigDecimal((double)result);
                temp = temp.setScale(2, 4);
                return temp + "KB";
            } else {
                result /= 1024.0F;
                if(result < 1024.0F) {
                    temp = new BigDecimal((double)result);
                    temp = temp.setScale(2, 4);
                    return temp + "MB";
                } else {
                    result /= 1024.0F;
                    if(result < 1024.0F) {
                        temp = new BigDecimal((double)result);
                        temp = temp.setScale(2, 4);
                        return temp + "GB";
                    } else {
                        result /= 1024.0F;
                        temp = new BigDecimal((double)result);
                        temp = temp.setScale(2, 4);
                        return temp + "TB";
                    }
                }
            }
        }
    }

    public static String getMemoryPercentString(float percent) {
        BigDecimal result = new BigDecimal((double)(percent * 100.0F));
        return result.setScale(2, 4) + "%";
    }

    
    public static boolean copyAssetsFile(Context context, String srcFileName, String targetDir, String targetFileName) {
        AssetManager asm = null;
        FileOutputStream fos = null;
        DataInputStream dis = null;

        try {
            asm = context.getAssets();
            dis = new DataInputStream(asm.open(srcFileName));
            createDir(targetDir);
            File e = new File(targetDir, targetFileName);
            if(e.exists()) {
                e.delete();
            }

            fos = new FileOutputStream(e);
            byte[] buffer = new byte[1024];
            boolean len = false;

            int len1;
            while((len1 = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }

            fos.flush();
            boolean var10 = true;
            return var10;
        } catch (Exception var20) {
            Log.w("FileUtil", "copy assets file failed:" + var20.toString());
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }

                if(dis != null) {
                    dis.close();
                }
            } catch (Exception var19) {
                ;
            }

        }

        return false;
    }

    public static boolean downloadFileByURL(String downloadUrl, String localPath) {
        File f = null;
        Object is = null;
        FileOutputStream os = null;

        boolean con;
        try {
            try {
                f = new File(localPath);
                if(!f.exists()) {
                    URL e = new URL(downloadUrl);
                    URLConnection con1 = e.openConnection();
                    con1.setConnectTimeout(8000);
                    con1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
                    is = con1.getInputStream();
                    if(con1.getContentEncoding() != null && con1.getContentEncoding().equalsIgnoreCase("gzip")) {
                        is = new GZIPInputStream(con1.getInputStream());
                    }

                    byte[] e1 = new byte[2048];
                    boolean len = true;
                    os = new FileOutputStream(f);

                    int len1;
                    while((len1 = ((InputStream)is).read(e1)) != -1) {
                        os.write(e1, 0, len1);
                    }
                }

                return true;
            } catch (Exception var17) {
                var17.printStackTrace();
                if(f != null && f.exists()) {
                    delFile(f.getAbsolutePath());
                }
            }

            con = false;
        } finally {
            try {
                if(is != null) {
                    ((InputStream)is).close();
                }

                if(os != null) {
                    os.close();
                }
            } catch (Exception var16) {
                var16.printStackTrace();
            }

        }

        return con;
    }

    public static boolean writeObjectToFile(String filePath, Object object) {
        FileOutputStream output = null;
        ObjectOutputStream oos = null;

        try {
            File e = new File(filePath);
            if(!e.getParentFile().exists()) {
                e.getParentFile().mkdirs();
            }

            if(e.exists()) {
                e.delete();
            }

            output = new FileOutputStream(e);
            oos = new ObjectOutputStream(output);
            oos.writeObject(object);
            oos.flush();
            boolean var5 = true;
            return var5;
        } catch (Exception var15) {
            var15.printStackTrace();
        } finally {
            try {
                if(null != output) {
                    output.close();
                }

                if(null != oos) {
                    oos.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

        return false;
    }

    public static Object readObjectFromFile(String filePath) {
        FileInputStream input = null;
        ObjectInputStream ois = null;

        Object var4;
        try {
            File e = new File(filePath);
            if(!e.exists()) {
                var4 = null;
                return var4;
            }

            input = new FileInputStream(e);
            ois = new ObjectInputStream(input);
            var4 = ois.readObject();
        } catch (Exception var15) {
            var15.printStackTrace();
            return null;
        } finally {
            try {
                if(null != input) {
                    input.close();
                }

                if(null != ois) {
                    ois.close();
                }
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

        return var4;
    }
}
