package com.treematch.model;

import org.springframework.lang.NonNull;

public record ApiErrorResponse(@NonNull String errorMessage){}
