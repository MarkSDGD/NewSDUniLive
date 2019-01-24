package com.xike.xkliveplay.framework.entity;

public class ActivateTerminalRes {
	String userId = "";
	String status = "";

	public ActivateTerminalRes() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ActivateTerminalRes [userId=" + userId + ", status=" + status
				+ "]";
	}

}
