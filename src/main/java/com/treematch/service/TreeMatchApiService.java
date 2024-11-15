package com.treematch.service;

import com.treematch.model.ApiAnswerRequest;
import com.treematch.model.ApiAnswerResponse;
import com.treematch.model.ApiBeginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class TreeMatchApiService {

    private final QuizJsonLoaderService quizJsonLoaderService;

    public ApiBeginResponse beginQuiz() {
        var quiz = quizJsonLoaderService.lazyLoadQuiz("questions.json");
        var firstStep = quiz.steps().get(0);
        var firstQuestion = quiz.getQuestion(firstStep.questionId())
            .orElseThrow(() -> new IllegalStateException("No question could be found"));
        return new ApiBeginResponse(new ApiBeginResponse.Question(firstStep.id(), firstQuestion.question(), firstQuestion.validation()));
    }

    public ApiAnswerResponse answerQuestion(@Valid ApiAnswerRequest answer) {
        var quiz = quizJsonLoaderService.lazyLoadQuiz("questions.json");
        // Find the step we are attempting to answer
        var currentStep = quiz.getStep(answer.stepId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid step_id parameter"));
        // And now find the next step based on the chosen answer
        var nextStepId = currentStep.answers().get(answer.answer());
        if (nextStepId == null) {
            throw new IllegalArgumentException("Invalid answer value");
        }
        var nextStep = quiz.getStep(nextStepId)
            .orElseThrow(() -> new IllegalStateException("Could not find next step from answer"));
        // Check if the next step points to another next step or is the final destination (contains a result)
        if (nextStep.resultId() != null) {
            var match = quiz.getResult(nextStep.resultId())
                .orElseThrow(() -> new IllegalStateException("Next step points to an invalid result"));
            return new ApiAnswerResponse(new ApiAnswerResponse.Match(match.name(), match.description()));
        }
        // At this point we are pointed another question
        // Add some sanity guard checks to ensure the data pointing to the next question is present
        if (nextStep.questionId() == null || nextStep.answers() == null) {
            throw new IllegalArgumentException("Malformed step fields");
        }
        var nextQuestion = quiz.getQuestion(nextStep.questionId())
            .orElseThrow(() -> new IllegalStateException("Next step points to an invalid question"));
        return new ApiAnswerResponse(new ApiAnswerResponse.Question(nextStep.id(), nextQuestion.question(), nextQuestion.validation()));
    }
}
