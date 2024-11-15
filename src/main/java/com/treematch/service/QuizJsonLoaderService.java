package com.treematch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treematch.model.Quiz;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuizJsonLoaderService {

    private final ObjectMapper objectMapper;

    private final Validator validator;

    /**
     * Loads and validates the questions.json file and caches it in spring's default cache store for later re-use
     */
    @Cacheable(value = "quiz", key = "#fileName")
    public Quiz lazyLoadQuiz(@NonNull String fileName) {
        Quiz quiz;
        try {
            quiz = objectMapper.readValue(ResourceUtils.getFile("classpath:quiz/%s".formatted(fileName)), Quiz.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        validateQuizStructure(quiz);
        validateQuizDataIntegrity(quiz);
        validateQuizSteps(quiz);
        return quiz;
    }

    /**
     * Validates the json file has the correct structured data with the necessary fields
     *
     * @param quiz - De-serialised quiz object from the questions.json file
     */
    private void validateQuizStructure(@NonNull Quiz quiz) {
        var violations = validator.validate(quiz);
        if (!validator.validate(quiz).isEmpty()) {
            throw new ConstraintViolationException("The questions.json file has invalid or missing data", violations);
        }
        quiz.steps().forEach(step -> {
            // This step is a result step
            if (step.resultId() != null) {
                // Ensure we don't have a question_id or answers field
                if (step.questionId() != null || step.answers() != null) {
                    throw new IllegalArgumentException("Result steps element should only contain result_id");
                }
            } else if (step.questionId() == null || step.answers() == null) {
                throw new IllegalArgumentException("Next question steps should only contain question_id and answers");
            }
        });
    }

    /**
     * Validates all the IDs in the json file point to existent elements
     *
     * @param quiz - De-serialised quiz object from the questions.json file
     */
    private void validateQuizDataIntegrity(@NonNull Quiz quiz) {
        quiz.steps().forEach(step -> {
            // Ensure result steps point to existing result elements
            if (step.resultId() != null) {
                quiz.getResult(step.resultId()).orElseThrow(() -> new IllegalArgumentException("Result %s does not exist"));
            } else if (step.questionId() != null && step.answers() != null) {
                quiz.getQuestion(step.questionId()).orElseThrow(() -> new IllegalArgumentException("Question %s does not exist"));
                step.answers().values().forEach(
                    stepId -> quiz.getStep(stepId).orElseThrow(() -> new IllegalArgumentException("Step %s does not exist"))
                );
            }
        });
    }

    private void validateQuizSteps(@NonNull Quiz quiz) {
        quiz.steps().forEach(step -> validateStep(quiz, step.id(), new ArrayList<>()));
    }

    /**
     * Recursively validates a step of a quiz by going through each answer and ensuring a final result can be reached.
     * A step is deemed invalid if any of the following cases occur:
     * - IDs inside the json file point to non-existent elements
     * - An infinite loop can be formed by choosing a specific path
     *
     * @param quiz - De-serialised quiz object from the questions.json file
     * @param stepId - Current step ID that is being validated
     * @param prevChoices - Previous choices we have taken to get to the current step
     */
    private void validateStep(@NonNull Quiz quiz, long stepId, @NonNull List<Pair<Long, String>> prevChoices) {
        var step = quiz.getStep(stepId).orElseThrow(() -> new IllegalArgumentException("Step ID %s not found".formatted(stepId)));
        if (prevChoices.stream().anyMatch(choice -> choice.getFirst().equals(stepId))) {
            var cycleString = prevChoices.stream()
                .map(choice -> "Step: %s, Ans: %s".formatted(choice.getFirst(), choice.getSecond()))
                .collect(Collectors.joining(" -> "));
            throw new IllegalStateException("Cycle found in quiz %s".formatted(cycleString));
        }
        if (step.resultId() != null) {
            quiz.getResult(step.resultId()).orElseThrow(() -> new IllegalStateException("Step %s contains an invalid result %s".formatted(stepId, step.resultId())));
            return;
        }
        if (step.answers() == null) {
            throw new IllegalArgumentException("Malformed question step");
        }
        // Go through all the answers and ensure all the next steps are valid
        // It is possible that the questions.json file can contain cycles. This validation is under the assumption that this is not allowed
        step.answers().forEach((answer, nextStepId) -> {
            var newPrevChoices = Stream.concat(prevChoices.stream(), Stream.of(Pair.of(stepId, answer))).toList();
            validateStep(quiz, nextStepId, newPrevChoices);
        });
    }

}
