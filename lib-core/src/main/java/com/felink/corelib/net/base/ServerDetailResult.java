package com.felink.corelib.net.base;


import java.io.Serializable;

/**
 * 
 * @author cfb
 * 
 * @param <T>
 */
public class ServerDetailResult<T> implements Serializable{
		
	private static final long serialVersionUID = 1L;
	
	private ServerResultHeader csResult = new ServerResultHeader();
	
	public T detailItem;

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
		}
	}
	
	@Override
	public String toString() {
		return csResult.toString();
	}
}
