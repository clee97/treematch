package com.treematch.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Optional;

/**
 * Represents the whole questions.json string
 * <pre>
 * {
 *     "questions": [
 *         {
 *             "id": 1,
 *             "question": "Do you like fruit?",
 *             "validation": [
 *                 "yes",
 *                 "no"
 *             ]
 *         }
 *     ],
 *     "results": [
 *         {
 *             "id": 1,
 *             "name": "Apple tree",
 *             "description": "The most basic fruit tree"
 *         },
 *         {
 *             "id": 2,
 *             "name": "Willow tree",
 *             "description": "This tree is mysterious and cool"
 *         }
 *     ],
 *     "steps": [
 *         {
 *             "id": 1,
 *             "question_id": 1,
 *             "answers": {
 *                 "yes": 2,
 *                 "no": 3
 *             }
 *         },
 *         {
 *             "id": 2,
 *             "result_id": 1
 *         },
 *         {
 *             "id": 3,
 *             "result_id": 2
 *         }
 *     ]
 * }
 * </pre>
 */
public record Quiz(
    @NotEmpty(message = "Questions cannot be empty")
    List<@Valid Question> questions,
    @NotEmpty(message = "Results cannot be empty")
    List<@Valid Result> results,
    @NotEmpty(message = "Steps cannot be empty")
    List<@Valid Step> steps
) {
    public Optional<Question> getQuestion(long questionId) {
        return questions.stream()
            .filter(question -> question.id().equals(questionId))
            .findFirst();
    }

    public Optional<Step> getStep(long stepId) {
        return steps.stream()
            .filter(step -> step.id().equals(stepId))
            .findFirst();
    }

    public Optional<Result> getResult(long resultId) {
        return results.stream()
            .filter(result -> result.id().equals(resultId))
            .findFirst();
    }

}
