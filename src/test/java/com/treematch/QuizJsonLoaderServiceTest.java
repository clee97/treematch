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
    public void givenValidJson_whenLazyLoadQuiz_thenReturnObject() {
        Assertions.assertNotNull(quizJsonLoaderService.lazyLoadQuiz("questions.json"));
    }

    @Test
    public void givenJsonWithInvalidIds_whenLazyLoadQuiz_thenThrowException() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> quizJsonLoaderService.lazyLoadQuiz("questions-invalid-ids.json")
        );
    }

    @Test
    public void givenJsonWithCycle_whenLazyLoadQuiz_thenThrowException() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> quizJsonLoaderService.lazyLoadQuiz("questions-cycle.json")
        );
    }

}
