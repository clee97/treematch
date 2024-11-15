package com.treematch.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a result element inside questions.json.
 * <pre>
 * {
 *     "id": 1,
 *     "name": "Apple tree",
 *     "description": "The most basic fruit tree"
 * }
 * </pre>
 */
public record Result(
    @NotNull(message = "Missing id field")
    @Min(value = 0, message = "Invalid id field value")
    Long id,
    @NotBlank(message = "Missing or blank name field")
    String name,
    @NotBlank(message = "Missing or blank description field")
    String description
) {}
