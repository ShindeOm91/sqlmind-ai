package com.sqlmind.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record GenerateSqlRequest(
		@NotBlank(message = "Prompt is required")
		String prompt
) {}
