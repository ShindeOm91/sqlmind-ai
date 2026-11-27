package com.sqlmind.ai.service;

import org.springframework.stereotype.Service;

import com.sqlmind.ai.provider.AIProvider;

@Service
public class AIService {

	private final AIProvider aiProvider;

    public AIService(AIProvider aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String generateSQL(String userPrompt) {

        String prompt = """
                You are SQLMind AI, an expert SQL developer.
                Convert the user's natural language request into SQL.
                Return only the SQL query.
                Do not include markdown or explanations.

                User Request:
                """ + userPrompt;

        return aiProvider.generateSQL(prompt);
    }
}
