package com.ifi.votretemps.model.bo;

public class BOTask {
	private int taskId;
	private String taskName;
	private String info;
	private String startDate;
	private String endDate;
	private int color;
	private int state;
	private int userId;

	public BOTask() {

	}

	public BOTask(int taskId, String taskName, String info, String startDate,
			String endDate, int color, int state, int userId) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.info = info;
		this.startDate = startDate;
		this.endDate = endDate;
		this.color = color;
		this.state = state;
		this.userId = userId;
	}

	// Setter and Getter
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
