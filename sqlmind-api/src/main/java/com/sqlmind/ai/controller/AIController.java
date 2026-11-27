package com.sqlmind.ai.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sqlmind.ai.dto.GenerateSqlRequest;
import com.sqlmind.ai.service.AIService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

	private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate-sql")
    public ResponseEntity<Map<String, String>> generateSQL(
            @Valid @RequestBody GenerateSqlRequest request) {

        String sql = aiService.generateSQL(request.prompt());

        return ResponseEntity.ok(Map.of("sql", sql));
    }
}
