package com.ifi.votretemps.model.bo;

import java.util.List;

public class BOUser {
	private int userId;
	private String userName;
	private String password;
	private List<BOTask> tasks;

	public BOUser() {

	}

	public BOUser(int userId, String userName, String password) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
	}

	public BOUser(int userId, String userName, String password,
			List<BOTask> tasks) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.tasks = tasks;
	}

	// Setter and Getter
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<BOTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<BOTask> tasks) {
		this.tasks = tasks;
	}
}
