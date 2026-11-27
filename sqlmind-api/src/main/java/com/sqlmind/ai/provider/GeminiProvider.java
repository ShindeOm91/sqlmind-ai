package com.sqlmind.ai.provider;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.util.retry.Retry;

@Component
public class GeminiProvider implements AIProvider {

	@Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    @Override
    public String generateSQL(String prompt) {

    	Map<String, Object> request = Map.of(
    		    "contents", List.of(
    		        Map.of("parts", List.of(
    		            Map.of("text", prompt)
    		        ))
    		    ),
    		    "generationConfig", Map.of(
    		        "maxOutputTokens", 256
    		    )
    		);

        Map response = webClient.post()
                .uri(apiUrl)
                .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block(Duration.ofSeconds(60));

        if (response == null || !response.containsKey("candidates")) {
            throw new RuntimeException("No SQL response received from Gemini");
        }

        List candidates = (List) response.get("candidates");
        Map candidate = (Map) candidates.get(0);
        Map content = (Map) candidate.get("content");
        List parts = (List) content.get("parts");
        Map part = (Map) parts.get(0);

        return part.get("text").toString();
    }
    
}
