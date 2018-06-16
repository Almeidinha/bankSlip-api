package com.almeida.bankslips.rest.response;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

	private T data;
	private List<String> errors;
	private List<String> messages;

	public Response() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getErrors() {
		if (this.errors == null) {
			this.errors = new ArrayList<String>();
		}
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getMessages() {
		if (this.messages == null) {
			this.messages = new ArrayList<String>();
		}
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	

}
