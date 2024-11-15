package com.treematch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApiAnswerRequest(
    @NotNull(message = "Missing step_id parameter")
    @Min(value = 0, message = "Invalid step_id parameter")
    @JsonProperty("step_id")
    Long stepId,

    @NotBlank(message = "Missing or empty answer parameter")
    String answer
) {}
