package com.trias.resouce.body.response;

import java.io.Serializable;
import java.util.List;

import com.trias.resouce.model.User;

public class QueryUserResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<User> userList;

	private int totalCount;

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
