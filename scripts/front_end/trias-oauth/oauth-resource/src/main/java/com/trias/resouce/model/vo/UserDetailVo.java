package com.trias.resouce.model.vo;

import com.trias.resouce.model.User;

public class UserDetailVo extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String roleType;

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}
