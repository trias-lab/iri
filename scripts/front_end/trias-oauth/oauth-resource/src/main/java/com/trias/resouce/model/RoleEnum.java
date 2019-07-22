package com.trias.resouce.model;

public enum RoleEnum {
	Admin("ROLE_ADMIN");

	private final String roleType;

	private RoleEnum(String roleType) {
		this.roleType = roleType;
	}

}
