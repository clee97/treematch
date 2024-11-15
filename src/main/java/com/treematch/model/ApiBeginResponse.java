package com.treematch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.util.Set;

public record ApiBeginResponse(@NonNull Question question) {
    public record Question(
        @JsonProperty("step_id") @NonNull Long stepId,
        @NonNull String question,
        @NonNull Set<String> answers
    ) {}
}
