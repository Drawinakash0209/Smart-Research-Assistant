package com.research.assistant;

import lombok.Data;

@Data
public class ResearchRequest {
	
	private String content;
	private String operation;
	
	
	//getters and setetrs
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}

}
