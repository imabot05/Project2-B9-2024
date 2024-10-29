package com.javaweb.dto;

import java.util.List;

public class ErrorDetailDTO {
	private String error;
	private List<String> detail;
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}

	public List<String> getDetail() {
		return detail;
	}

	public void setDetail(List<String> detail) {
		this.detail = detail;
	}		
	
}
