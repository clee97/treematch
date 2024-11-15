package com.treematch.controller;

import com.treematch.model.ApiAnswerRequest;
import com.treematch.model.ApiAnswerResponse;
import com.treematch.model.ApiBeginResponse;
import com.treematch.service.TreeMatchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TreeMatchApiController {

    private final TreeMatchApiService treeMatchApiService;

    @GetMapping("/begin")
    @PreAuthorize("hasRole('ROLE_TREE_MATCH')")
    public ApiBeginResponse beginQuiz() {
        return treeMatchApiService.beginQuiz();
    }

    @PostMapping("/answer")
    @PreAuthorize("hasRole('ROLE_TREE_MATCH')")
    public ApiAnswerResponse answerQuestion(@RequestBody ApiAnswerRequest answer) {
        return treeMatchApiService.answerQuestion(answer);
    }

}
