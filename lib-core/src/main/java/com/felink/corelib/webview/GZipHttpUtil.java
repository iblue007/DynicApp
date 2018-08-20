package com.felink.corelib.webview;


import com.felink.sdk.common.HttpCommon;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLHandshakeException;

/**
 * GZip工具类
 */
public class GZipHttpUtil {
	
	public 	static final int MAX_REQUEST_RETRY_COUNTS = 3;
	private static final int CONNECTION_TIME_OUT = 5000;
	private static final int SOCKET_TIME_OUT = 30000;
	
	private static HttpRequestRetryHandler mReqRetryHandler;
	private static ResponseHandler<String> mResponseHandler;
	
	/**
	 * Get方式请求(默认使用UTF-8编码)GZip压缩数据, 并且解压服务端响应结果
	 * @param url 请求地址url
	 * @return 解压后的服务端响应字符串
	 */
	public static String get(String url) {
		return get(url, HTTP.UTF_8, null);
	}
	
	/**
	 * Get方式请求GZip压缩数据, 并且解压服务端响应结果
	 * @param url 请求地址url
	 * @param encoding 字符编码
	 * @return 解压后的服务端响应字符串
	 */
	public static String get(String url, String encoding) {
		return get(url, encoding, null);
	}
	
	
	/**
	 * Get方式请求GZip压缩数据, 并且解压服务端响应结果
	 * @param url 请求地址url
	 * @param encoding 字符编码
	 * @param paramsMap get参数集
	 * @return 解压后的服务端响应字符串
	 */
	public static String get(String url, String encoding, HashMap<String, String> paramsMap) {
		HttpGet request = null;
		HttpClient client = null;
		String responseStr = null;
		
		try {
			String encodeUrl = HttpCommon.utf8URLencode(buildGetURL(url, paramsMap));
			request = new HttpGet(encodeUrl);
			request.setHeader("Accept-Encoding", "gzip, deflate");  
			
			client = getDefaultHttpClient();
			responseStr = client.execute(request, getResponseHandler(encoding));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			abortConnection(request, client);
		}
		return responseStr;
	}
	
	
	/**
	 * POST方式请求(默认使用UTF-8编码)GZip压缩数据, 并且解压服务端响应结果
	 * @param url 请求地址url
	 * @return 解压后的服务端响应字符串
	 */
	public static String post(String url) {
		return post(url, HTTP.UTF_8, null);
	}
	
	/**
	 * POST方式请求GZip压缩数据, 并且解压服务端响应结果
	 * @param url 请求地址url
	 * @param encoding 字符编码
	 * @return 解压后的服务端响应字符串
	 */
	public static String post(String url, String encoding) {
		return post(url, encoding, null);
	}
	
	/**
	 * POST方式请求GZip压缩数据, 并且解压服务端响应结果
	 * @param url 请求地址url
	 * @param encoding 字符编码
	 * @param paramsMap POST参数集
	 * @return 解压后的服务端响应字符串
	 */
	public static String post(String url, String encoding, HashMap<String, String> paramsMap) {
		HttpPost request = null;
		HttpClient client = null;
		String responseStr = null;
		
		try {
			request = new HttpPost(url);
			request.setHeader("Accept-Encoding", "gzip, deflate");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (paramsMap != null) {
				for (String key : paramsMap.keySet()) {
					params.add(new BasicNameValuePair(key, paramsMap.get(key)));
				}
			}
			
			HttpEntity entity = new UrlEncodedFormEntity(params, encoding);
			request.setEntity(entity);
			client = getDefaultHttpClient();
			responseStr = client.execute(request, getResponseHandler(encoding));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			abortConnection(request, client);
		}
		return responseStr;
	}
	
	
	
	// ------------------------- private method -------------------------------\\
	
	
	
	private static DefaultHttpClient getDefaultHttpClient() {
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIME_OUT);
		DefaultHttpClient client = new DefaultHttpClient(params);
		client.setHttpRequestRetryHandler(getReqRetryHandler());
		return client;
	}
	
	private static String buildGetURL(String url, HashMap<String, String> paramsMap) {
		if (paramsMap == null)
			return url;
		
		StringBuffer paramStr = new StringBuffer("");
		boolean hasQuestion = url.lastIndexOf("?") > 0 ? true : false;
		for (String key : paramsMap.keySet()) {
			paramStr.append("&").append(key).append("=").append(paramsMap.get(key));
		}
		if (!hasQuestion) {
			paramStr.replace(0, 1, "?");
		}
		return url + paramStr.toString();
	}
	
	private static ResponseHandler<String> getResponseHandler(final String encoding) {
		if (mResponseHandler == null) {
			mResponseHandler = new ResponseHandler<String>() {
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int statusCode = response.getStatusLine().getStatusCode();
					
					if (statusCode == HttpStatus.SC_OK) {
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							String res = "";
							InputStream is = null;
							BufferedInputStream bis = null;
							try {
								is = entity.getContent();
								bis = new BufferedInputStream(is);
								bis.mark(2); // 取前两个字节
								byte[] header = new byte[2];
								int result = bis.read(header);
								bis.reset(); // reset输入流到开始位置
								
								if (result != -1 && getShort(header, 0) == GZIPInputStream.GZIP_MAGIC) {
									is = new GZIPInputStream(bis);
								} else {
									is = bis;
								}
								
								res = parse(entity, is, encoding); 
							} catch (UnsupportedEncodingException ex) {
								res = parse(entity, is, HTTP.DEFAULT_CONTENT_CHARSET);
								ex.printStackTrace();
							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								entity.consumeContent();
								if (is != null) is.close();
								if (bis != null) bis.close();
							}
							
							return res;
						}
					}
					
					return String.valueOf(statusCode);
				}
			};
		}
		
		return mResponseHandler;
	}
	
	private static HttpRequestRetryHandler getReqRetryHandler() {
		if (mReqRetryHandler == null) {
			mReqRetryHandler = new HttpRequestRetryHandler() {
				@Override
				public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
					// we will try three times before getting connection
					if (executionCount >= MAX_REQUEST_RETRY_COUNTS) {
						// Do not retry if over max retry count
						return false;
					}
					if (exception instanceof NoHttpResponseException) {
						// Retry if the server dropped connection on us
						return true;
					}
					if (exception instanceof SSLHandshakeException) {
						// Do not retry on SSL handshake exception
						return false;
					}

					HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
					boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
					if (!idempotent) {
						// Retry if the request is considered idempotent
						return true;
					}
					return false;

				}
			};
		}
		
		return mReqRetryHandler;
	}
	
	private static int getShort(byte[] buffer, int off) {
		return (buffer[off] & 0xFF) | ((buffer[off + 1] & 0xFF) << 8);
	}
	
	private static void abortConnection(final HttpRequestBase hrb, final HttpClient httpclient) {
		if (hrb != null) {
			hrb.abort();
		}
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	private static String parse(final HttpEntity entity, InputStream instream, final String charset) throws IOException, ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        
        if (instream == null) {
            return "";
        }
        
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int)entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        
        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }
}
