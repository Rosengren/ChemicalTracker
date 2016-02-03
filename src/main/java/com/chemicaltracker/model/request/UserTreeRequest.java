package com.chemicaltracker.model.request;

public class UserTreeRequest {

	private String username;

	public UserTreeRequest() {
		username = "unknown";
	}

	public UserTreeRequest(final String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}