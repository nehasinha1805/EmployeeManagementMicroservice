package com.training.microservices.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Credentials {

	@NotNull
	@NotEmpty
	private String user;
	
	@NotNull
	@NotEmpty
	private String pass;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}
