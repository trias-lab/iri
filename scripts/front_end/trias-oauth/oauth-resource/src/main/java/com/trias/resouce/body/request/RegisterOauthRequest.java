package com.trias.resouce.body.request;

public class RegisterOauthRequest extends OauthLoginRequestBody {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String address;

	private String sex;

	private String email;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
