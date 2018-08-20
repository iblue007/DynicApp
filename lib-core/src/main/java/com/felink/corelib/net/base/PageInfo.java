package com.felink.corelib.net.base;

import java.io.Serializable;


/**
 * pageInfo
 * 
 * @author cfb
 * 
 */
public class PageInfo implements Serializable{

	private static final long serialVersionUID = -3687753242127348682L;

	/**当前页码*/
    public int currentPageNum = 1;

    /**总页数*/
    public int totalPages = 1;

    /**总记录数  搜索结果需要用到*/
    public int totalRecordNums = 0;
    public boolean hasNext = false;//是否有下一页，1表示存在下一页

    /**页面大小**/
    public int pagesize = 0;
    
    public PageInfo() {
    }

	public PageInfo(int currentPageNum, int pagesize) {
		this.currentPageNum = currentPageNum;
		this.pagesize = pagesize;
	}

	public int getNextPageIndex() {
		if (currentPageNum < totalPages)
			return (++currentPageNum);
		else
			return -1;
	}

	public boolean isLastPage(){
		
		return currentPageNum>=totalPages;
	}
	
	@Override
	public String toString(){
		return "currentPageNum="+currentPageNum+", totalPages="+totalPages+", totalRecordNums="+totalRecordNums;
	}

	public void checkHasNext(int haxNext){
		if(haxNext == 0){
			hasNext = false;
		}else {
			hasNext = true;
		}
	}
}
