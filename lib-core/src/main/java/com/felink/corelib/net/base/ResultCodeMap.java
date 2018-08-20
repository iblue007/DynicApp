package com.felink.corelib.net.base;

import android.annotation.SuppressLint;

import java.util.HashMap;

public class ResultCodeMap {

	public static final int SERVER_RESPONSE_CODE_SUCCESS = 0;
	public static final int SERVER_RESPONSE_CODE_NOIMP = -1;
	public static final int SERVER_RESPONSE_CODE_1 = 1;
	public static final int SERVER_RESPONSE_CODE_2 = 2;
	public static final int SERVER_RESPONSE_CODE_3 = 3;
	public static final int SERVER_RESPONSE_CODE_4 = 4;
	public static final int SERVER_RESPONSE_CODE_5 = 5;
	public static final int SERVER_RESPONSE_CODE_6 = 6;
	public static final int SERVER_RESPONSE_CODE_7 = 7;
	public static final int SERVER_RESPONSE_CODE_8 = 8;
	public static final int SERVER_RESPONSE_CODE_997 = 997;
	public static final int SERVER_RESPONSE_CODE_998 = 998;
	public static final int SERVER_RESPONSE_CODE_999 = 999;
	
	public static final int SERVER_RESPONSE_CODE_8800 = 8800;

	public static final int RESULT_CODE_NOTHING = -10;
	public static final int RESULT_CODE_EXCEPTION = -11;
	public static final int RESULT_CODE_VERIFY_FAILED = -12;
	public static final int RESULT_CODE_FILE_NOT_FOUND = -13;

	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer,String> resultCodeMap = new HashMap<Integer,String>();
	static {
		resultCodeMap.put(SERVER_RESPONSE_CODE_SUCCESS, "成功");
		resultCodeMap.put(SERVER_RESPONSE_CODE_NOIMP, "接口未实现");
		resultCodeMap.put(SERVER_RESPONSE_CODE_1, "用户未登录");
		resultCodeMap.put(SERVER_RESPONSE_CODE_2, "无效的接口编号");
		resultCodeMap.put(SERVER_RESPONSE_CODE_3, "参数错误");
		resultCodeMap.put(SERVER_RESPONSE_CODE_4, "传递的参数值非法(如：电话号码、IMSI、IMEI中含有英文字母)");
		resultCodeMap.put(SERVER_RESPONSE_CODE_5, "数据校验失败");
		resultCodeMap.put(SERVER_RESPONSE_CODE_6, "数据加密失败");
		resultCodeMap.put(SERVER_RESPONSE_CODE_7, "数据解密失败");
		resultCodeMap.put(SERVER_RESPONSE_CODE_8, "用户未登录");
		
		resultCodeMap.put(SERVER_RESPONSE_CODE_997, "发送的请求数据长度太长");
		resultCodeMap.put(SERVER_RESPONSE_CODE_998, "服务器维护");
		resultCodeMap.put(SERVER_RESPONSE_CODE_999, "服务器内部错误");	
		
		resultCodeMap.put(SERVER_RESPONSE_CODE_8800, "客户端数据解析失败");

		resultCodeMap.put(RESULT_CODE_EXCEPTION, "数据处理异常");
		resultCodeMap.put(RESULT_CODE_NOTHING, "数据为空");
	}
	
	public static String getCodeDesc(int resultCode){
		String resultStr = resultCodeMap.get(resultCode);
		if ( resultStr!=null )
			return resultStr;
		return "访问服务发生未知异常!";
	}
}
