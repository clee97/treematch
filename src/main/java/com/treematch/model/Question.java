package com.treematch.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * Represents a question element inside questions.json.
 * <pre>
 * {
 *     "id": 1,
 *     "question": "Do you like fruit?",
 *     "validation": ["yes", "no"]
 * }
 * </pre>
 */
public record Question(
    @NotNull(message = "Missing id field")
    @Min(value = 0, message = "Invalid id field value")
    Long id,
    @NotBlank(message = "Missing or blank question field")
    String question,
    @NotEmpty(message = "Missing or blank validation field")
    Set<@NotBlank(message = "Validation elements cannot be blank") String> validation
) {}
