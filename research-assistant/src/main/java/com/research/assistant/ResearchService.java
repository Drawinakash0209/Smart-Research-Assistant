package com.research.assistant;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ResearchService {
	
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	private final WebClient webClient;
	
	private final ObjectMapper objectMapper;
	
	public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
		this.webClient = webClientBuilder.build();
		this.objectMapper = objectMapper;
	}

	public String processContent(ResearchRequest request) {
		//Build the prompt 
		String prompt = buildPrompt(request);
		// Query the AI model API
		Map<String, Object> requestBody = Map.of(
				"contents", new Object[] {
						Map.of("parts", new Object[] {
								Map.of("text", prompt)
						})
				}
				
				);
		
		String response = webClient.post()
				.uri(geminiApiUrl + geminiApiKey)
				.bodyValue(requestBody)
				.retrieve().bodyToMono(String.class)
				.block();
		
		//PArse the Response
		
		
		
		// Return the result

		return extractTextFromResponse(response);
	}
	
	private String extractTextFromResponse(String response) {
		try {
			
			GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
			
			if(geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
				GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
				if(firstCandidate.getContent() != null &&
						firstCandidate.getContent().getParts() != null &&
						!firstCandidate.getContent().getParts().isEmpty()) {
					return firstCandidate.getContent().getParts().get(0).getText();
				}
 			 
			}
			
			return "no content";
			
		} catch (Exception e) {

			e.printStackTrace();
			return "Error Parsing: " + e.getMessage();
		}

	}

	private String buildPrompt(ResearchRequest request) {
		StringBuilder prompt = new StringBuilder();
		switch(request.getOperation()) {
		case "summarize":
			prompt.append("provide a cleer and concise summary of the following content: \n\n");
			break; 
		case "suggest":
			prompt.append("based on the following content, suggest a list of relevant topics for further research. Format the response with clear headings and bullet points \n\n");
			break;
		default:
			throw new IllegalArgumentException("Unsupported operation: " + request.getOperation());		
		}
		
		prompt.append(request.getContent());
		return prompt.toString();
	}
	
	

}
