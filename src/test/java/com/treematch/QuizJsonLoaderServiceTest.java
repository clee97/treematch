package com.treematch;

import com.treematch.service.QuizJsonLoaderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuizJsonLoaderServiceTest {

    @Autowired
    private QuizJsonLoaderService quizJsonLoaderService;

    @Test
    public void testLoad() {
        var quiz = quizJsonLoaderService.lazyLoadQuiz("questions.json");
        Assertions.assertNotNull(quiz);
    }

}
