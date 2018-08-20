package com.felink.corelib.net.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author cfb
 * @param <T>
 */
public class ServerResult<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	public Map<String, Object> extraData = new HashMap<String, Object>();

	private ServerResultHeader csResult = new ServerResultHeader();

	/** 分页信息*/
	private PageInfo pageInfo = new PageInfo();
	
	/**返回结果集*/
	public ArrayList<T> itemList = new ArrayList<T>();
	
	public boolean atLastPage = false;

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	
	public ServerResultHeader getCsResult() {
		return csResult;
	}

	public void setCsResult(ServerResultHeader csResult) {
		if (csResult!=null){
			this.csResult.setbNetworkProblem(csResult.isbNetworkProblem());
			this.csResult.setResultCode(csResult.getResultCode());
			this.csResult.setResultMessage(csResult.getResultMessage());
			this.csResult.setBodyEncryptType(csResult.getBodyEncryptType());
			this.csResult.setResponseJson(csResult.getResponseJson());
			this.csResult.setServerTime(csResult.getServerTime());
		}
	}
	
	@Override
	public String toString() {
		return csResult.toString();
	}
}
