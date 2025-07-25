package com.research.assistant;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {
	private List<Candidate> candidates;
	
	
	//Getterrs and setetrs
	public List<Candidate> getCandidates() {
		return candidates;
	}
	
	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Candidate {
	    private Content content;

	    public Content getContent() {
	        return content;
	    }

	    public void setContent(Content content) {
	        this.content = content;
	    }
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Content {
	    private List<Part> parts;

	    public List<Part> getParts() {
	        return parts;
	    }

	    public void setParts(List<Part> parts) {
	        this.parts = parts;
	    }
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Part {
	    private String text;

	    public String getText() {
	        return text;
	    }

	    public void setText(String text) {
	        this.text = text;
	    }
	}

}
