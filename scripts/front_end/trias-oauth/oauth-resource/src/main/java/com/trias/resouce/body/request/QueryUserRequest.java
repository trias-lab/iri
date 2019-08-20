package com.trias.resouce.body.request;

public class QueryUserRequest extends RegisterOauthRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int currentIndex;
	
	private int pageSize;

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
