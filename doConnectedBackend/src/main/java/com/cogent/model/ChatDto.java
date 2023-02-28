package com.cogent.model;

public class ChatDto {
	
	private String from_user;
	private String to_user;
	private String message;
	public ChatDto(String from_user, String to_user, String message, String datetime) {
		super();
		this.from_user = from_user;
		this.to_user = to_user;
		this.message = message;
	}
	public ChatDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFrom_user() {
		return from_user;
	}
	public void setFrom_user(String from_user) {
		this.from_user = from_user;
	}
	public String getTo_user() {
		return to_user;
	}
	public void setTo_user(String to_user) {
		this.to_user = to_user;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
