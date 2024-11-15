package com.treematch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Represents a step element inside questions.json.
 * <pre>
 * {
 *    "id": 1,
 *    "question_id": 1,
 *    "answers": {
 *         "yes": 2,
 *         "no": 3
 *     }
 * },
 * </pre>
 * OR
 <pre>
 * {
 *     "id": 2,
 *     "result_id": 1
 * },
 * </pre>
 */
public record Step(
    @NotNull(message = "Missing id field")
    @Min(value = 0, message = "Invalid id field value")
    Long id,
    @JsonProperty("question_id")
    Long questionId,
    @JsonProperty("result_id")
    Long resultId,
    Map<
        @NotBlank(message = "Invalid answer key")
        String,
        @NotNull(message = "Missing answer entry value. IDs must not be null")
        @Min(value = 0, message = "Invalid answer entry value. IDs must be numeric")
        Long
    > answers
) {}
