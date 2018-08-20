package com.nd.hilauncherdev.dynamic.util;

import com.nd.hilauncherdev.dynamic.clientparser.Client;
import com.nd.hilauncherdev.dynamic.clientparser.SaxParseService;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LoaderUtil {

	/**
	 * 读取动态apk中的配置文件client.xml，生成Client对象
	 *
	 * @Title: readClientApkInfo
	 * @Description: TODO
	 * @param path
	 *            Apk文件路径
	 * @return
	 */
	public static Client readClientApkInfo(String path) {
		InputStream inputstream = null;
		try {
			@SuppressWarnings("resource")
			ZipFile zip = new ZipFile(path);
			ZipEntry entry = zip.getEntry("assets/client.xml");
			inputstream = zip.getInputStream(entry);
		} catch (Exception e) {
			return null;
		}
		SaxParseService sax = new SaxParseService();
		Client client = null;
		try {
			client = sax.getClient(inputstream);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return client;
	}

	/**
	 * 判断是否解析出client对象
	 *
	 * @Title: isClientExist
	 * @Description: TODO
	 * @param client
	 *            解析得到的Client类
	 * @return
	 */
	public static boolean isClientExist(Client client) {
		if (null == client) {
			return false;
		}
		return true;
	}

}
