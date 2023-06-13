package org.example;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuizTest {

    @Test
    public void testQuizCreation() {
        Quiz quiz = new Quiz("Test Quiz");
        assertEquals("Test Quiz", quiz.getTitle());
        assertEquals(0, quiz.getQuestions().size());
    }

    @Test
    public void testQuizAddQuestion() {
        Quiz quiz = new Quiz("Test Quiz");
        Question question = new Question("Question 1", Arrays.asList("Option 1", "Option 2"), 1);
        quiz.addQuestion(question);

        List<Question> questions = quiz.getQuestions();
        assertEquals(1, questions.size());
        assertEquals(question, questions.get(0));
    }

    @Test
    public void testQuizRun() {
        Quiz quiz = new Quiz("Test Quiz");
        Question question1 = new Question("Question 1", Arrays.asList("Option 1", "Option 2"), 1);
        Question question2 = new Question("Question 2", Arrays.asList("Option 1", "Option 2"), 2);
        quiz.addQuestion(question1);
        quiz.addQuestion(question2);

        // TODO: Implement a test to verify the behavior of the runQuiz() method
    }

    @Test
    public void testQuizSaveToJSON() throws IOException {
        Quiz quiz = new Quiz("Test Quiz");
        Question question = new Question("Question 1", Arrays.asList("Option 1", "Option 2"), 1);
        quiz.addQuestion(question);

        // TODO: Implement a test to verify the behavior of the saveToJSON() method
    }

    @Test
    public void testQuizCreator() {
        // TODO: Implement a test for the QuizCreator class
    }
}
