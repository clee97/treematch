package com.treematch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

public record ApiAnswerResponse(@Nullable Question question, @Nullable Match match) {
    public ApiAnswerResponse(@NonNull Match match) {
        this(null, match);
    }
    public ApiAnswerResponse(@NonNull Question question) {
        this(question, null);
    }
    public record Match(@NonNull String name, @NonNull String description) {}
    public record Question(
        @JsonProperty("step_id") @NonNull Long stepId,
        @NonNull String question,
        @NonNull Set<String> answers
    ) {}
}
